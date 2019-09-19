package com.maxnerva.cloud.portal.admin.service;

import com.alibaba.druid.util.StringUtils;
import com.maxnerva.cloud.common.base.annotation.MethodHandler;
import com.maxnerva.cloud.common.base.annotation.executor.range.EntityIncludeRange;
import com.maxnerva.cloud.common.base.service.AbstractSingleService;
import com.maxnerva.cloud.common.base.service.strategy.Backup2LocalSchemaDataRecoveryStrategy;
import com.maxnerva.cloud.portal.admin.constants.ErrorCodeConstant;
import com.maxnerva.cloud.portal.admin.dao.entity.BusinessTypeEntity;
import com.maxnerva.cloud.portal.admin.dao.entity.MobileNavigationEntity;
import com.maxnerva.cloud.portal.admin.dao.mapper.MobileNavigationMapper;
import com.maxnerva.cloud.portal.admin.enums.PlatformTypeEnum;
import com.maxnerva.cloud.portal.admin.exception.CheckException;
import com.maxnerva.cloud.portal.admin.exception.EditException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.Weekend;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 移动导航业务处理
 * @author john
 *
 */
@Service
@Slf4j
public class MobileNavigationService extends 
	AbstractSingleService<MobileNavigationMapper, MobileNavigationEntity,Integer>{

	//orderNo 默认初始值
	private static final Integer ORDER_NO_DEFAULT_VAL = 0;
	
	private static final String KEY_ORDER_NO = "order";
	private static final String KEY_DESCROPTION = "description";
	private static final String VAL_DESCROPTION_FORMAT = "%s(%s)之前";
	private static final Object VAL_LAST = "最后";

	@Autowired
	private BusinessTypeService businessTypeService;
	
	@PostConstruct
    public void init() {
        dataRecoveryStrategy = new Backup2LocalSchemaDataRecoveryStrategy<>(mapper, thisEntityClass);
    }
	/**
	 * 添加移动端导航
	 * @param entity
	 */
	@Transactional(rollbackFor=Exception.class)
	@MethodHandler
	public void addMobileNavigation(@EntityIncludeRange(required=false,range = { "mobileNavigationCode","mobileNavigationName","businessTypeId","url","order" }) MobileNavigationEntity entity,Integer userId) {
		checkParam(entity);
		log.info("addMobileNavigation,无重复数据，将执行插入操作");
		Integer orgOrderNo = entity.getOrder();
		Integer maxOrderNo = queryMaxOrderNo(entity.getBusinessTypeId());
		this.add(entity,userId);
		Integer id  = entity.getId();
		//更新数据库原有数据的orderNo
		updateOldDataOrderNo(entity.getBusinessTypeId(),
				maxOrderNo,orgOrderNo,id);
		
	}
	/**
	 * 校验数据是否重复
	 * @param entity
	 */
	private MobileNavigationEntity checkParam(MobileNavigationEntity entity) {
		// 校验数据是否重复
		if(!StringUtils.isEmpty(entity.getMobileNavigationCode())){

			if(!StringUtils.isEmpty(entity.getApplicationCode())){
				Integer count = this.getCount(MobileNavigationEntity.builder().applicationCode(entity.getApplicationCode()).build());
				
				if(count > 0  && ObjectUtils.isEmpty(entity.getId())){
					log.warn("已存在重复数据");
					throw new CheckException("新增操作失败，已存在重复数据",ErrorCodeConstant.EX_ADD_MULTI_DATA);
				}
				if(count >1  &&!ObjectUtils.isEmpty(entity.getId())){
					log.warn("已存在重复数据");
					throw new CheckException("更新操作失败，已存在重复数据",ErrorCodeConstant.EX_UPD_MULTI_DATA);
				}
			}
		}
		// 校验orderNo
		Integer orgOrderNo = entity.getOrder();
		Integer maxOrderNo = queryMaxOrderNo(entity.getBusinessTypeId());
		if(orgOrderNo> (ObjectUtils.isEmpty(maxOrderNo)?0:maxOrderNo)){
			throw new CheckException("操作失败，orderNo 无效",ErrorCodeConstant.EX_ORDER_VALIDATION_FAILED);
		}
		
		// 校验businessType
		if(!ObjectUtils.isEmpty(entity.getBusinessTypeId())
				&& businessTypeService.getOne(BusinessTypeEntity.builder().id(
						entity.getBusinessTypeId()).platform(PlatformTypeEnum.MOBILE.getType()).build())==null){
			throw new CheckException("操作失败，BusinessTypeId"+entity.getBusinessTypeId()+"不存在",ErrorCodeConstant.EX_BIZ_TYPE_ID_VALIDATION_FAILED);
		}
	
		if(!ObjectUtils.isEmpty(entity.getId())){
			MobileNavigationEntity oldEntity = this.getById(entity.getId());
			if(ObjectUtils.isEmpty(oldEntity)){
				throw new CheckException("操作失败，id:{}不存在",ErrorCodeConstant.EX_ID_VALIDATION_FAILED,entity.getId());
			}
			return oldEntity;
		}
		return null;
	}
	/**
	 * 更新原有数据的 orderNo
	 * @param businessTypeId
	 * @param orgOrderNo
	 * @param newOrderNo
	 */
	@Transactional(rollbackFor=Exception.class)
	public void updateOldDataOrderNo(Integer businessTypeId,
			Integer orgOrderNo, Integer newOrderNo,Integer targetId) {
		if(orgOrderNo > newOrderNo){
			// update table t set t.order_no =t.order_no +1 where t.order_no>=#{newOrderNo} and t.order_no<#{orgOrderNo} and t.bizTypeId=#{businessTypeId}
			this.mapper.updateOldDataOrderNoTargetUp(orgOrderNo,newOrderNo,businessTypeId);
		}else if(orgOrderNo < newOrderNo){
			// update table t set t.order_no =t.order_no -1 where t.order_no>#{orgOrderNo} ND t.order_no<=#{newOrderNo} and t.bizTypeId=#{businessTypeId}
			this.mapper.updateOldDataOrderNoTargetDown(orgOrderNo,newOrderNo,businessTypeId);
		}
		// update table t set t.order_no =newOrderNo where t.id=#{id}
		MobileNavigationEntity entity  = new MobileNavigationEntity();
		entity.setId(targetId);
		entity.setOrder(newOrderNo);
		this.mapper.updateByPrimaryKeySelective(entity);
	
	}

	/**
	 * 根据businessTypeId 查询最大orderNO
	 * @param businessTypeId
	 * @return
	 */
	private Integer queryMaxOrderNo(Integer businessTypeId) {
		log.info("查询{}业务分类的maxOrderNo",businessTypeId);
		Example example = new Example(MobileNavigationEntity.class);
		example.orderBy("order").desc();
		example.and(new Weekend<>(MobileNavigationEntity.class)
				.weekendCriteria()
				.andEqualTo("businessTypeId", businessTypeId)
				//.andEqualTo("deleteFlag", false)
				);
		List<MobileNavigationEntity> oldEntities = 
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
	 * @param mobileNavigationId
	 */
	@Transactional(rollbackFor=Exception.class)
	public Boolean deleteMobileNavigation(Integer mobileNavigationId,Integer userId) {
		
		// 校验数据是否存在
		MobileNavigationEntity entity = this.getById(mobileNavigationId);
		if(ObjectUtils.isEmpty(entity)){
			log.error("deleteMobileNavigation 失败,id {} 无对应的数据",mobileNavigationId);
			return false;
		}
		// 更新全部 orderNo  把要删除数据的orderNo设为最大
		updateOldDataOrderNo(entity.getBusinessTypeId(),entity.getOrder(),
				queryMaxOrderNo(entity.getBusinessTypeId()),entity.getId());
		//this.mapper.updateByPrimaryKeySelective(entity);
		return this.removeById(entity.getId(),userId)>0;
	}
	/**
	 * 全量更新 MobileNavigation
	 * 如果更改了业务分类需要处理原业务分类下数据的orderno，以及新业务分内下数据的orderNo
	 * 如果没有更新业务分类只需处理当前业务分类下数据的orderNo
	 * @param entity
	 */
	@Transactional(rollbackFor=Exception.class)
	@MethodHandler
	public void fullUpdateMobileNavigation(Integer id,
			@EntityIncludeRange(required=false,range = { "mobileNavigationCode","mobileNavigationName","businessTypeId","url","order" })MobileNavigationEntity entity,Integer userId) {
		MobileNavigationEntity oldEntity = checkParam(entity);
		log.info("fullUpdateMobileNavigation,无重复数据，将执行更新操作");
		if(ObjectUtils.isEmpty(oldEntity)){
			throw new EditException("操作失败，id:{}不已存在",ErrorCodeConstant.EX_ID_VALIDATION_FAILED,id);
		}
		// 比对文件id，不一致删除老的
//		if(!oldEntity.getIconPath().equals(entity.getIconPath())
//				&& !StringUtils.isEmpty(oldEntity.getIconPath())){
//			fileService.deleteByFileId(oldEntity.getIconPath());
//		}
		updateOrderNoByBizType(oldEntity,entity,id);
		// 更新数据
		this.editById(entity,userId);
	}
	/**
	 * 根据业务分类更新orderNo
	 * @param oldEntity
	 * @param entity
	 * @param id
	 */
	private void updateOrderNoByBizType(MobileNavigationEntity oldEntity,
			MobileNavigationEntity entity, Integer id) {
		// 处理原业务分类ordreNo
		if(oldEntity.getBusinessTypeId().equals(entity.getBusinessTypeId())){
			updateOldDataOrderNo(oldEntity.getBusinessTypeId(),
					oldEntity.getOrder(), entity.getOrder(), id);
		}else {
			// 原业务分类 (可以理解为把原来业务分类下的那条数据删除)
			updateOldDataOrderNo(oldEntity.getBusinessTypeId(),oldEntity.getOrder(),
					queryMaxOrderNo(entity.getBusinessTypeId()),  id);
			
			// 现有业务分类(可以理解为在当前业务分类新增一条数据)
			updateOldDataOrderNo(entity.getBusinessTypeId(),
					queryMaxOrderNo(entity.getBusinessTypeId())+1, entity.getOrder(), id);
		}
	}
	/**
	 * 部分更新 MobileNavigation
	 * @param mobileNavigationId
	 * @param entity
	 */
	@Transactional(rollbackFor=Exception.class)
	public void partlyUpdateMobileNavigation(Integer mobileNavigationId,
			MobileNavigationEntity entity,Integer userId) {
		entity.setId(mobileNavigationId);
		MobileNavigationEntity oldEntity = checkParam(entity);
		log.info("partlyUpdateMobileNavigation,无重复数据，将执行更新操作");
		if(ObjectUtils.isEmpty(oldEntity)){
			throw new EditException("操作失败，id:{}不已存在",ErrorCodeConstant.EX_ID_VALIDATION_FAILED,mobileNavigationId);
		}
		// 比对文件id，不一致删除老的
//		if(!oldEntity.getIconPath().equals(entity.getIconPath())
//				&& !StringUtils.isEmpty(oldEntity.getIconPath())){
//			fileService.deleteByFileId(oldEntity.getIconPath());
//		}
		updateOrderNoByBizType(oldEntity,entity,mobileNavigationId);
		// 更新数据
		this.editByIdSelective(entity,userId);
	}
	/**
	 * 查询orderNo
	 * @param businessTypeId
	 * @param mobileNavigationId
	 * @param type 
	 * @return
	 */
	public List<Map<String, Object>> quryOrderNo(Integer businessTypeId,
			Integer mobileNavigationId, String type) {
		List<Map<String, Object>> result = new ArrayList<>();
		
		// 当前记录order
		Integer tagetOrderNo = null;
		MobileNavigationEntity mobileNavigationEntity = new MobileNavigationEntity();
		if(!ObjectUtils.isEmpty(mobileNavigationId)){
			mobileNavigationEntity = this.getById(mobileNavigationId);
			tagetOrderNo = mobileNavigationEntity.getOrder();
		}
		Example example = new Example(MobileNavigationEntity.class);
		example.orderBy("order").asc();
		example.and(new Weekend<>(MobileNavigationEntity.class)
				.weekendCriteria()
				.andEqualTo("businessTypeId", businessTypeId)
				//.andEqualTo("deleteFlag", false)
				);
		List<MobileNavigationEntity> entities =
				this.mapper.selectByExample(example);
		Integer lastOderNo;
		if(CollectionUtils.isEmpty(entities)){
			lastOderNo = ORDER_NO_DEFAULT_VAL;
		}
		if("update".equals(type) && businessTypeId == mobileNavigationEntity.getBusinessTypeId()){
			lastOderNo = queryMaxOrderNo(businessTypeId)-1;
			log.info("lastOderNo:{},tagetOrderNo:{}",lastOderNo,tagetOrderNo);
			for(MobileNavigationEntity entity:entities){
				Map<String, Object> map = new HashMap<>();
				if(tagetOrderNo == entity.getOrder()){
					map.put(KEY_ORDER_NO, entity.getOrder());
					map.put(KEY_DESCROPTION, "当前位置");
					result.add(map);
					continue;
				}
				if((tagetOrderNo - entity.getOrder()<= 0 && lastOderNo <=1) // 当前值显示当前位置
						|| lastOderNo == entity.getOrder()){// 当前记录序号最大，不用显示，用最后替换
					continue;
				}
				map.put(KEY_ORDER_NO, entity.getOrder());
				for(MobileNavigationEntity nextEntity:entities){
					if(nextEntity.getOrder()-1 == entity.getOrder()){
						map.put(KEY_DESCROPTION, String.format(VAL_DESCROPTION_FORMAT, nextEntity.getMobileNavigationName(),nextEntity.getMobileNavigationCode()));
					}
				}
				result.add(map);
			}
			if(lastOderNo - tagetOrderNo >0
					&& lastOderNo>=1){ // 超过一条记录，不是修改最大排序的记录时，显示最后
				Map<String, Object> map = new HashMap<>();
				map.put(KEY_ORDER_NO, lastOderNo);
				map.put(KEY_DESCROPTION, VAL_LAST);
				result.add(map);
			}
		}else{ // 新增或者选择其他平台时
			lastOderNo = queryMaxOrderNo(businessTypeId);
			for(MobileNavigationEntity entity:entities){
				Map<String, Object> map = new HashMap<>();
				map.put(KEY_ORDER_NO, entity.getOrder());
				map.put(KEY_DESCROPTION, String.format(VAL_DESCROPTION_FORMAT, entity.getMobileNavigationName(),entity.getMobileNavigationCode()));
				result.add(map);
			}
			Map<String, Object> map = new HashMap<>();
			map.put(KEY_ORDER_NO, lastOderNo);
			map.put(KEY_DESCROPTION, VAL_LAST);
			result.add(map);
		}
		
		return result;
	}
	/**
	 * 编辑校验是否唯一
	 * @param entity
	 * @return
	 */
	public Boolean uniqueEdit(MobileNavigationEntity entity) {
		return mapper.uniqueEdit(entity)==0;
	}
}
