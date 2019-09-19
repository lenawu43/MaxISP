package com.maxnerva.cloud.portal.admin.service;


import com.alibaba.druid.util.StringUtils;
import com.maxnerva.cloud.common.base.service.AbstractSingleService;
import com.maxnerva.cloud.common.base.service.strategy.Backup2LocalSchemaDataRecoveryStrategy;
import com.maxnerva.cloud.portal.admin.constants.ErrorCodeConstant;
import com.maxnerva.cloud.portal.admin.dao.entity.ApplicationGroupItemEntity;
import com.maxnerva.cloud.portal.admin.dao.entity.WebNavigationEntity;
import com.maxnerva.cloud.portal.admin.dao.mapper.ApplicationGroupItemMapper;
import com.maxnerva.cloud.portal.admin.exception.CheckException;
import com.maxnerva.cloud.portal.admin.exception.EditException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.Weekend;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/** 
 * 应用组业务处理
 * @author john
 *
 */
@Service
@Slf4j
public class ApplicationGroupItemService extends 
	AbstractSingleService<ApplicationGroupItemMapper, ApplicationGroupItemEntity,Integer>{

	// orderNo 默认初始值
	private static final Integer ORDER_NO_DEFAULT_VAL = 0;
	
	@PostConstruct
    public void init() {
        dataRecoveryStrategy = new Backup2LocalSchemaDataRecoveryStrategy<>(mapper, thisEntityClass);
    }
	/**
	 * 添加应用组
	 * @param entity
	 */
	@Transactional(rollbackFor=Exception.class)
	public void addApplicationGroupItem(ApplicationGroupItemEntity entity,Integer userId) {
		checkParam(entity);
		log.info("addApplicationGroupItem,无重复数据，将执行插入操作");
		Integer orgOrderNo = entity.getOrder();
		Integer maxOrderNo = queryMaxOrderNo(entity.getWebNavigationId());
		entity.setOrder(maxOrderNo);
		//entity = setDefaultValue(entity);
		this.add(entity,userId);
		Integer id  = entity.getId();
		//更新数据库原有数据的orderNo
		updateOldDataOrderNo(entity.getWebNavigationId(),
				maxOrderNo,orgOrderNo,id);
		
	}
	/**
	 * 校验数据是否重复
	 * @param entity
	 */
	private ApplicationGroupItemEntity checkParam(ApplicationGroupItemEntity entity) {
		ApplicationGroupItemEntity paramEntity = new ApplicationGroupItemEntity();
		// 校验数据是否重复
		if(!StringUtils.isEmpty(entity.getApplicationCode())){
			paramEntity.setApplicationCode(entity.getApplicationCode());
			paramEntity.setWebNavigationId(entity.getWebNavigationId());
			//paramEntity.setDeleteFlag(false);
			List<ApplicationGroupItemEntity> multiEnties = 
					this.getList(paramEntity);
			if(!CollectionUtils.isEmpty(multiEnties)){
				log.warn("已存在重复数据");
				throw new CheckException("操作失败，组内已存在重复数据",HttpStatus.INTERNAL_SERVER_ERROR.value());
			}
		}
		Integer orgOrderNo = entity.getOrder();
		Integer maxOrderNo = queryMaxOrderNo(entity.getWebNavigationId());
		if(orgOrderNo>maxOrderNo){
			throw new CheckException("操作失败，orderNo 无效",HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
//		if(null != entity.getId()){
//			ApplicationGroupItemEntity oldEntity = this.getById(entity.getId());
//			if(null == oldEntity){
//				throw new BusinessException("操作失败，id"+entity.getId()+"不已存在",HttpStatus.INTERNAL_SERVER_ERROR.value());
//			}
//			return oldEntity;
//		}
		return null;
	}
	/**
	 * 更新原有数据的 orderNo
	 * @param mobileNavigationId
	 * @param orgOrderNo
	 * @param newOrderNo
	 */
	@Transactional(rollbackFor=Exception.class)
	public void updateOldDataOrderNo(Integer mobileNavigationId,
			Integer orgOrderNo, Integer newOrderNo,Integer targetId) {
		if(orgOrderNo > newOrderNo){
			// update table t set t.order_no =t.order_no +1 where t.order_no>=#{newOrderNo} and t.order_no<#{orgOrderNo} and t.bizTypeId=#{businessTypeId}
			this.mapper.updateOldDataOrderNoTargetUp(orgOrderNo,newOrderNo,mobileNavigationId);
		}else if(orgOrderNo < newOrderNo){
			// update table t set t.order_no =t.order_no -1 where t.order_no>#{orgOrderNo} ND t.order_no<=#{newOrderNo} and t.bizTypeId=#{businessTypeId}
			this.mapper.updateOldDataOrderNoTargetDown(orgOrderNo,newOrderNo,mobileNavigationId);
		}
		// update table t set t.order_no =newOrderNo where t.id=#{id}
		ApplicationGroupItemEntity entity  = new ApplicationGroupItemEntity();
		entity.setId(targetId);
		entity.setOrder(newOrderNo);
		this.mapper.updateByPrimaryKeySelective(entity);
	
	}

	/**
	 * 根据businessTypeId 查询最大orderNO
	 * @param webNavigationId
	 * @return
	 */
	private Integer queryMaxOrderNo(Integer webNavigationId) {
		log.info("查询{}业务分类的maxOrderNo",webNavigationId);
		Example example = new Example(ApplicationGroupItemEntity.class);
		example.orderBy("order").desc();
		example.and(new Weekend<>(ApplicationGroupItemEntity.class)
				.weekendCriteria()
				.andEqualTo("webNavigationId", webNavigationId)
				//.andEqualTo("deleteFlag", false)
				);
		List<ApplicationGroupItemEntity> oldEntities = 
				this.mapper.selectByExample(example);
		if(CollectionUtils.isEmpty(oldEntities)){
			return ORDER_NO_DEFAULT_VAL;
		}else{
			return oldEntities.get(0).getOrder()+1;
		}
		
	}

	/**
	 * 删除移动导航
	 * 逻辑删除
	 * @param id
	 */
	@Transactional(rollbackFor=Exception.class)
	public Boolean deleteApplicationGroupItem(Integer id,Integer userId) {
		
		// 校验数据是否存在
		ApplicationGroupItemEntity entity = this.getById(id);
		if(null == entity){
			log.error("deleteApplicationGroupItem 失败,id {} 无对应的数据",id);
			return false;
		}
		// 设置deleteFlag
		//entity.setDeleteFlag(true);
		entity.setLastModifiedBy(userId);
		// 更新全部 orderNo  把要删除数据的orderNo设为最大
		updateOldDataOrderNo(entity.getWebNavigationId(),entity.getOrder(),
				queryMaxOrderNo(entity.getWebNavigationId()),entity.getId());
		return this.remove(entity,userId);
	}
	/**
	 * 全量更新 ApplicationGroupItem
	 * 如果更改了业务分类需要处理原业务分类下数据的orderno，以及新业务分内下数据的orderNo
	 * 如果没有更新业务分类只需处理当前业务分类下数据的orderNo
	 * @param entity
	 */
	@Transactional(rollbackFor=Exception.class)
	public void fullUpdateApplicationGroupItem(Integer id,ApplicationGroupItemEntity entity,Integer userId) {
		ApplicationGroupItemEntity oldEntity = checkParam(entity);
		log.info("fullUpdateApplicationGroupItem,无重复数据，将执行更新操作");
		if(null==oldEntity){
			throw new EditException("操作失败，id"+id+"不已存在",ErrorCodeConstant.EX_ID_VALIDATION_FAILED);
		}
		updateOrderNoByBizType(oldEntity,entity,id);
		// 更新数据
		this.removeById(id,userId);
	}
	/**
	 * 根据业务分类更新orderNo
	 * @param oldEntity
	 * @param entity
	 * @param id
	 */
	@Transactional(rollbackFor=Exception.class)
	public void updateOrderNoByBizType(ApplicationGroupItemEntity oldEntity,
			ApplicationGroupItemEntity entity, Integer id) {
		// 处理原业务分类ordreNo
		if(oldEntity.getWebNavigationId().equals(entity.getWebNavigationId())){
			updateOldDataOrderNo(oldEntity.getWebNavigationId(),
					oldEntity.getOrder(), entity.getOrder(), id);
		}else {
			// 原业务分类 (可以理解为把原来业务分类下的那条数据删除)
			updateOldDataOrderNo(oldEntity.getWebNavigationId(),oldEntity.getOrder(),
					queryMaxOrderNo(entity.getWebNavigationId()),  id);
			
			// 现有业务分类(可以理解为在当前业务分类新增一条数据)
			updateOldDataOrderNo(entity.getWebNavigationId(),
					queryMaxOrderNo(entity.getWebNavigationId())+1, entity.getOrder(), id);
		}
	}
	/**
	 * 部分更新 ApplicationGroupItem
	 * @param mobileNavigationId
	 * @param entity
	 */
	@Transactional(rollbackFor=Exception.class)
	public void partlyUpdateApplicationGroupItem(Integer mobileNavigationId,
			ApplicationGroupItemEntity entity,Integer userId) {
		entity.setId(mobileNavigationId);
		ApplicationGroupItemEntity oldEntity = checkParam(entity);
		log.info("partlyUpdateApplicationGroupItem,无重复数据，将执行更新操作");
		updateOrderNoByBizType(oldEntity,entity,mobileNavigationId);
		// 更新数据
		this.editByIdSelective(entity,userId);
	}
	/**
	 * 自定义排序查询list
	 * @param
	 * @return
	 */
	public List<ApplicationGroupItemEntity> selectAllByOrderNo() {
		return mapper.selectByExample(
				Example.builder(ApplicationGroupItemEntity.class).orderByAsc("order").build()
		);
	}
	
	/**
	 * 批量更新
	 * @param applicationGroupItemEntities
	 * @param oldApplicationGroupItemEntities
	 */
	public void updateRelationship(
			List<ApplicationGroupItemEntity> applicationGroupItemEntities,
			List<ApplicationGroupItemEntity> oldApplicationGroupItemEntities,Integer userId) {
		// TODO single 批量更新
		updateRelationship(applicationGroupItemEntities, oldApplicationGroupItemEntities ,true, true, null,userId);
		
	}
	/**
	 * copy  from abstractBusinessService
	 * @param entities
	 * @param oldList
	 * @param needUpdate
	 * @param needDelete
	 * @param comparator
	 */
	protected void updateRelationship(List<ApplicationGroupItemEntity> entities
            , List<ApplicationGroupItemEntity> oldList
            , boolean needUpdate
            , boolean needDelete
            , Comparator<ApplicationGroupItemEntity> comparator ,Integer userId) {

        List<ApplicationGroupItemEntity> newList = new ArrayList<>(entities);

        // 浜ら泦杩涜update鎿嶄綔锛堥潪蹇呰锛�
        if (needUpdate && !ObjectUtils.isEmpty(newList)) {
            if (!ObjectUtils.isEmpty(comparator)) {
                newList = newList.stream().filter(n -> {
                    for (ApplicationGroupItemEntity o : oldList) {
                        if (o.equals(n) && comparator.compare(o, n) != 0) {
                            return true;
                        }
                    }

                    return false;

                }).collect(Collectors.toList());
            } else {
                newList.retainAll(oldList);
            }

            if (!ObjectUtils.isEmpty(newList)) {
                batchEdit(newList,userId);
            }
        }

        // new -> old 
        newList.clear();
        newList.addAll(entities);
        newList.removeAll(oldList);
        if (!ObjectUtils.isEmpty(newList)) {
            batchAdd(newList,userId);
        }

        // old -> new 
        if (needDelete) {
            newList.clear();
            newList.addAll(entities);
            oldList.removeAll(newList);
            if (!ObjectUtils.isEmpty(oldList)) {
            	List<Integer> ids = new ArrayList<Integer>();
            	oldList.forEach(entity->{
            		ids.add(entity.getId());
            	});
                batchRemove(ids,userId);
            }
        }
    }
	/**
	 * 根据webNavigationId查询
	 * @param applicationGroupItemEntity
	 * @return
	 */
	public List<ApplicationGroupItemEntity> getListOrderBy(
			ApplicationGroupItemEntity applicationGroupItemEntity) {
		
		Example example = new Example(ApplicationGroupItemEntity.class);
		example.orderBy("order").asc();
		example.and(new Weekend<>(ApplicationGroupItemEntity.class)
				.weekendCriteria()
				.andEqualTo("webNavigationId", applicationGroupItemEntity.getWebNavigationId())
				//.andEqualTo("deleteFlag", false)
				);
//		List<ApplicationGroupItemEntity> result = mapper.selectByExample(example);
//		if(!CollectionUtils.isEmpty(result)){
//			result.forEach(appGrp->{
//				appGrp.setIconPathContent(fileService.downLoadByFileId(appGrp.getIconPath()));
//			});
//		}
		return mapper.selectByExample(example);
	}
	/**
	 * 根据webNavigationId 删除
	 * @param webNavigationId
	 */
	public void deleteByWebNavigationId(Integer webNavigationId) {
		mapper.delete(ApplicationGroupItemEntity.builder().webNavigationId(webNavigationId).build());
	}
	
	/**
	 * 更新应用组
	 * @param entity
	 * @param id
	 */
	@Transactional(rollbackFor=Exception.class) 
	void updateApplicationGroupByNavigation(WebNavigationEntity entity,
			Integer id,Integer userId) {
		// 更新应用组
		List<ApplicationGroupItemEntity> oldApplicationGroupItemEntities = 
				this.getList(
						ApplicationGroupItemEntity.builder().webNavigationId(id).build());
		if(!CollectionUtils.isEmpty(oldApplicationGroupItemEntities)){
			this.deleteByWebNavigationId(entity.getId());
		}
		List<ApplicationGroupItemEntity> applicationGroupItemEntities =
				entity.getApplicationGrpList();
		
		if(!CollectionUtils.isEmpty(applicationGroupItemEntities)){
			applicationGroupItemEntities.sort(new Comparator<ApplicationGroupItemEntity>() {
				@Override
				public int compare(ApplicationGroupItemEntity o1, ApplicationGroupItemEntity o2) {
					return o1.getOrder().compareTo(o2.getOrder());
				}
			});
			applicationGroupItemEntities.forEach(applicationGroup->{
				applicationGroup.setWebNavigationId(id);
				this.addApplicationGroupItem(applicationGroup,userId);
			});
		}
		
		//applicationGroupItemService.batchAdd(applicationGroupItemEntities);
		//applicationGroupItemService.updateRelationship(applicationGroupItemEntities, oldApplicationGroupItemEntities);
	}
	
}
