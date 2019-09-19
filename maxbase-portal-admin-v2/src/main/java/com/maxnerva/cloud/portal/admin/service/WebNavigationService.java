package com.maxnerva.cloud.portal.admin.service;

import com.maxnerva.cloud.common.base.annotation.MethodHandler;
import com.maxnerva.cloud.common.base.annotation.executor.range.EntityIncludeRange;
import com.maxnerva.cloud.common.base.exception.NullFieldValueException;
import com.maxnerva.cloud.common.base.service.AbstractSingleService;
import com.maxnerva.cloud.common.base.service.strategy.Backup2LocalSchemaDataRecoveryStrategy;
import com.maxnerva.cloud.portal.admin.constants.ErrorCodeConstant;
import com.maxnerva.cloud.portal.admin.dao.entity.ApplicationGroupItemEntity;
import com.maxnerva.cloud.portal.admin.dao.entity.BusinessTypeEntity;
import com.maxnerva.cloud.portal.admin.dao.entity.WebNavigationEntity;
import com.maxnerva.cloud.portal.admin.dao.mapper.WebNavigationMapper;
import com.maxnerva.cloud.portal.admin.enums.NavigationTypeEnum;
import com.maxnerva.cloud.portal.admin.enums.PlatformTypeEnum;
import com.maxnerva.cloud.portal.admin.exception.AddException;
import com.maxnerva.cloud.portal.admin.exception.CheckException;
import com.maxnerva.cloud.portal.admin.exception.DeleteException;
import com.maxnerva.cloud.portal.admin.exception.EditException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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

//import com.maxnerva.cloud.portal.admin.rest.DictClientService;

/**
 * 移动导航业务处理
 * @author john
 *
 */
@Service
@Slf4j
public class WebNavigationService extends 
		AbstractSingleService<WebNavigationMapper, WebNavigationEntity,Integer>{

	//orderNo 默认初始值
	private static final Integer ORDER_NO_DEFAULT_VAL = 0;
	
	private static final String KEY_ORDER_NO = "order";
	private static final String KEY_DESCROPTION = "description";
	private static final String VAL_DESCROPTION_FORMAT = "%s(%s)之前";
	private static final Object VAL_LAST = "最后";

	private static final String DICT_CODE_BIZ_TYPE = "PortalBusinessType";
	
	@Autowired
	private ApplicationGroupItemService applicationGroupItemService;
	
	@Autowired
	private BusinessTypeService businessTypeService;
	
	@PostConstruct
    public void init() {
        dataRecoveryStrategy = new Backup2LocalSchemaDataRecoveryStrategy<>(mapper, thisEntityClass);
    }
	/**
	 * 添加web导航
	 * @param entity
	 */
	@Transactional(rollbackFor=Exception.class)
	@MethodHandler
	public void addWebNavigation(@EntityIncludeRange(required=true,range = { "webNavigationCode","webNavigationName","businessTypeId","order" })WebNavigationEntity entity,Integer userId) {
		checkParam(entity);
		log.info("addWebNavigation,无重复数据，将执行插入操作");
		Integer orgOrderNo = entity.getOrder();
		Integer maxOrderNo = queryMaxOrderNo(entity.getBusinessTypeId());
		entity.setOrder(maxOrderNo);
		this.add(entity,userId);
		Integer id  = entity.getId()/*this.selectOne(entity).getId()*/;
		//更新数据库原有数据的orderNo
		updateOldDataOrderNo(entity.getBusinessTypeId(),
				maxOrderNo,orgOrderNo,id);
		if(NavigationTypeEnum.APPLICATION_GROUP.getType().equals(entity.getNavigationType())){
			//应用组
			List<ApplicationGroupItemEntity> applicationGroupItemEntities 
				= entity.getApplicationGrpList();
			if(CollectionUtils.isEmpty(applicationGroupItemEntities)){
				throw new AddException("应用组为空",ErrorCodeConstant.EX_APPLICATION_GGP_IS＿NULL_FAILED);
			}
			applicationGroupItemService.updateApplicationGroupByNavigation(entity,id,userId);
		}
	}
	/**
	 * 校验数据是否重复
	 * @param entity
	 */
	private WebNavigationEntity checkParam(WebNavigationEntity entity) {
		if(entity.getNavigationType().equals(NavigationTypeEnum.NAVIGATION.getType())
				&& StringUtils.isEmpty(entity.getWebUrl()) && StringUtils.isEmpty(entity.getAppDownloadUrl())){
			 throw new NullFieldValueException("The field name named [{},{}] is null!", "webUrl","appDownloadUrl");
		}
		// 校验数据是否重复
		if(!StringUtils.isEmpty(entity.getWebNavigationCode())){

			if(!StringUtils.isEmpty(entity.getApplicationCode())){
				Integer count = this.getCount(WebNavigationEntity.builder().applicationCode(entity.getApplicationCode()).build());
				
				if(count > 0  && null == entity.getId()){
					log.warn("已存在重复数据");
					throw new CheckException("新增操作失败，已存在重复数据",ErrorCodeConstant.EX_ADD_MULTI_DATA);
				}
				if(count >1  && null !=entity.getId()){
					log.warn("已存在重复数据");
					throw new CheckException("更新操作失败，已存在重复数据",ErrorCodeConstant.EX_UPD_MULTI_DATA);
				}
			}
		}
		Integer orgOrderNo = entity.getOrder();
		Integer maxOrderNo = queryMaxOrderNo(entity.getBusinessTypeId());
		if((null==orgOrderNo ? 0 : orgOrderNo)> (null==maxOrderNo?0:maxOrderNo)){
			throw new CheckException("操作失败，orderNo 无效",ErrorCodeConstant.EX_ORDER_VALIDATION_FAILED);
		}
		// 校验businessType
		if(!ObjectUtils.isEmpty(entity.getBusinessTypeId())
				&& businessTypeService.getOne(BusinessTypeEntity.builder().id(entity.getBusinessTypeId()).platform(PlatformTypeEnum.WEB.getType()).build())==null){
			throw new CheckException("操作失败，BusinessTypeId [{}] 不存在",ErrorCodeConstant.EX_BIZ_TYPE_ID_VALIDATION_FAILED,entity.getBusinessTypeId());
		}
		if(!ObjectUtils.isEmpty(entity.getId())){
			WebNavigationEntity oldEntity = this.getById(entity.getId());
			if(ObjectUtils.isEmpty(oldEntity)){
				throw new CheckException("操作失败，id:{}不已存在",ErrorCodeConstant.EX_ID_VALIDATION_FAILED,entity.getId());
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
		// TODO  mapper.updateAdvanceSetByExampleSelectiveMapper
		if(orgOrderNo > newOrderNo){
			// update table t set t.order_no =t.order_no +1 where t.order_no>=#{newOrderNo} and t.order_no<#{orgOrderNo} and t.bizTypeId=#{businessTypeId}
			this.mapper.updateOldDataOrderNoTargetUp(orgOrderNo,newOrderNo,businessTypeId);
		}else if(orgOrderNo < newOrderNo){
			// update table t set t.order_no =t.order_no -1 where t.order_no>#{orgOrderNo} ND t.order_no<=#{newOrderNo} and t.bizTypeId=#{businessTypeId}
			this.mapper.updateOldDataOrderNoTargetDown(orgOrderNo,newOrderNo,businessTypeId);
		}
		// update table t set t.order_no =newOrderNo where t.id=#{id}
		WebNavigationEntity entity  = new WebNavigationEntity();
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
		Example example = new Example(WebNavigationEntity.class);
		example.orderBy("order").desc();
		example.and(new Weekend<>(WebNavigationEntity.class)
				.weekendCriteria()
				.andEqualTo("businessTypeId", businessTypeId)
				//.andEqualTo("deleteFlag", false)
				);
		List<WebNavigationEntity> oldEntities = 
				this.mapper.selectByExample(example);
		if(CollectionUtils.isEmpty(oldEntities)){
			return ORDER_NO_DEFAULT_VAL;
		}else{
			return oldEntities.get(0).getOrder()+1;
		}
		
	}

	/**
	 * 删除web导航
	 * 逻辑删除
	 * @param webNavigationId
	 */
	@Transactional(rollbackFor=Exception.class)
	public Boolean deleteWebNavigation(Integer webNavigationId,Integer userId) {
		
		// 校验数据是否存在
		WebNavigationEntity entity = this.getById(webNavigationId);
		if(ObjectUtils.isEmpty(entity)){
			log.error("deleteWebNavigation 失败,id {} 无对应的数据",webNavigationId);
			return false;
		}
		
		// 更新应用组   
		// item 为0 才能删 ,没有依赖才能删，统一提示
		List<ApplicationGroupItemEntity> applicationGroupItemEntities = 
				applicationGroupItemService.getList(
						ApplicationGroupItemEntity.builder().webNavigationId(webNavigationId).build());
		if(!CollectionUtils.isEmpty(applicationGroupItemEntities)){
			// 有数据不能删
			throw new DeleteException("应用组下存在应用，无法删除", ErrorCodeConstant.EX_ADD_MULTI_DATA);
			/*List<Integer> ids = new ArrayList<Integer>();
			applicationGroupItemEntities.forEach(e->{
        		ids.add(e.getId());
        	});
            batchRemove(ids,userId);*/
			//applicationGroupItemService.batchEditDeleteFlag(applicationGroupItemEntities);
		}
		// 更新全部 orderNo  把要删除数据的orderNo设为最大
		updateOldDataOrderNo(entity.getBusinessTypeId(),entity.getOrder(),
				queryMaxOrderNo(entity.getBusinessTypeId()),entity.getId());
		this.removeById(webNavigationId,userId);
		return true;
	}
	/**
	 * 全量更新 WebNavigation
	 * 如果更改了业务分类需要处理原业务分类下数据的orderno，以及新业务分内下数据的orderNo
	 * 如果没有更新业务分类只需处理当前业务分类下数据的orderNo
	 * @param entity
	 */
	@Transactional(rollbackFor=Exception.class)
	public void fullUpdateWebNavigation(Integer id,
			@EntityIncludeRange(required=true,range = { "webNavigationCode","webNavigationName","businessTypeId","order_no" })WebNavigationEntity entity,Integer userId) {
		WebNavigationEntity oldEntity = checkParam(entity);
		log.info("fullUpdateWebNavigation,无重复数据，将执行更新操作");
		if(ObjectUtils.isEmpty(oldEntity)){
			throw new EditException("操作失败，id{}不已存在",ErrorCodeConstant.EX_ID_VALIDATION_FAILED,id);
		}
//		// 比对文件id，不一致删除老的
//		if(!oldEntity.getIconPath().equals(entity.getIconPath())
//				&& !StringUtils.isEmpty(oldEntity.getIconPath())){
//			fileService.deleteByFileId(oldEntity.getIconPath());
//		}
		updateOrderNoByBizType(oldEntity,entity,id);
		// 更新数据
		this.editById(entity,userId);
		if(NavigationTypeEnum.APPLICATION_GROUP.getType().equals(entity.getNavigationType())
				){
			applicationGroupItemService.updateApplicationGroupByNavigation(entity,id,userId);
		}
		
				
	}
	
	/**
	 * 根据业务分类更新orderNo
	 * @param oldEntity
	 * @param entity
	 * @param id
	 */
	@Transactional(rollbackFor=Exception.class)
	public void updateOrderNoByBizType(WebNavigationEntity oldEntity,
			WebNavigationEntity entity, Integer id) {
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
	 * 部分更新 WebNavigation
	 * @param webNavigationId
	 * @param entity
	 */
	@Transactional(rollbackFor=Exception.class)
	public void partlyUpdateWebNavigation(Integer webNavigationId,
			WebNavigationEntity entity,Integer userId) {
		entity.setId(webNavigationId);
		WebNavigationEntity oldEntity = checkParam(entity);
		log.info("partlyUpdateWebNavigation,无重复数据，将执行更新操作");
		if(ObjectUtils.isEmpty(oldEntity)){
			throw new EditException("操作失败，id:{}不已存在",ErrorCodeConstant.EX_ID_VALIDATION_FAILED,webNavigationId);
		}
		// 比对文件id，不一致删除老的
//		if(!oldEntity.getIconPath().equals(entity.getIconPath())
//				&& !StringUtils.isEmpty(oldEntity.getIconPath())){
//			fileService.deleteByFileId(oldEntity.getIconPath());
//		}
		updateOrderNoByBizType(oldEntity,entity,webNavigationId);
		// 更新数据
		this.mapper.updateByPrimaryKeySelective(entity);
		if(NavigationTypeEnum.APPLICATION_GROUP.getType().equals(entity.getNavigationType())){
			applicationGroupItemService.updateApplicationGroupByNavigation(
					entity,webNavigationId,userId);
		}
		
	}
	/**
	 * 查询orderNo
	 * 新增时:返回xxx之前，order_no就是当前记录的order_no
	 * 修改时:返回xxx之前，order_no就是当前记录的order_no-1
	 * @param businessTypeId
	 * @param webNavigationId
	 * @return
	 */
	public List<Map<String, Object>> quryOrderNo(Integer businessTypeId,
			Integer webNavigationId,String type) {
		List<Map<String, Object>> result = new ArrayList<>();
		
		// 当前记录order
		Integer tagetOrderNo = null;
		WebNavigationEntity webNavigationEntity = new WebNavigationEntity();
		if(null != webNavigationId){
			webNavigationEntity = this.getById(webNavigationId);
			tagetOrderNo = webNavigationEntity.getOrder();
		}
		Example example = new Example(WebNavigationEntity.class);
		example.orderBy("order").asc();
		example.and(new Weekend<>(WebNavigationEntity.class)
				.weekendCriteria()
				.andEqualTo("businessTypeId", businessTypeId)
				//.andEqualTo("deleteFlag", false)
				);
		List<WebNavigationEntity> entities =
				this.mapper.selectByExample(example);
		Integer lastOderNo;
		if(CollectionUtils.isEmpty(entities)){
			lastOderNo = ORDER_NO_DEFAULT_VAL;
		}
		if("update".equals(type) && businessTypeId == webNavigationEntity.getBusinessTypeId()){
			lastOderNo = queryMaxOrderNo(businessTypeId)-1;
			log.info("lastOderNo:{},tagetOrderNo:{}",lastOderNo,tagetOrderNo);
			for(WebNavigationEntity entity:entities){
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
				for(WebNavigationEntity nextEntity:entities){
					if(nextEntity.getOrder()-1 == entity.getOrder()){
						map.put(KEY_DESCROPTION, String.format(VAL_DESCROPTION_FORMAT, nextEntity.getWebNavigationName(),nextEntity.getWebNavigationCode()));
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
			for(WebNavigationEntity entity:entities){
				Map<String, Object> map = new HashMap<>();
				map.put(KEY_ORDER_NO, entity.getOrder());
				map.put(KEY_DESCROPTION, String.format(VAL_DESCROPTION_FORMAT, entity.getWebNavigationName(),entity.getWebNavigationCode()));
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
	 * 查询应用组导航类型下的应用组
	 * @param id
	 * @return
	 */
	public List<ApplicationGroupItemEntity> getApplicationGroupListByNavigationId(
			Integer id) {
		ApplicationGroupItemEntity applicationGroupItemEntity =
				new ApplicationGroupItemEntity();
		applicationGroupItemEntity.setWebNavigationId(id);
		//applicationGroupItemEntity.setDeleteFlag(false);
		return applicationGroupItemService.getList(applicationGroupItemEntity);
	}
	/**
	 * 更新平台类型
	 */
//	@Transactional(rollbackFor=Exception.class)
//	public void updateBusinessType(Integer userId) {
//		List<BusinessTypeEntity> dictList =
//				dictClientService.getDictByCode(DICT_CODE_BIZ_TYPE);
//		if(CollectionUtils.isEmpty(dictList)){
//			log.warn("从数据字典微服务获取业务分类({})数据为空",DICT_CODE_BIZ_TYPE);
//			return;
//		}
//		try {
//			businessTypeService.updateBusinessType(dictList,userId);
//		} catch (Exception e) {
//			log.error("更新业务类型失败",e);
//		}
//	}
	/**
	 * 编辑校验是否唯一
	 * @param entity
	 * @return
	 */
	public Boolean uniqueEdit(WebNavigationEntity entity) {
		return mapper.uniqueEdit(entity)==0;
	}
}
