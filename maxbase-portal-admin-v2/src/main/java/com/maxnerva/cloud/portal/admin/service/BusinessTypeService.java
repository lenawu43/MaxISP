package com.maxnerva.cloud.portal.admin.service;


import com.maxnerva.cloud.common.base.annotation.MethodHandler;
import com.maxnerva.cloud.common.base.service.AbstractSingleService;
import com.maxnerva.cloud.portal.admin.constants.ErrorCodeConstant;
import com.maxnerva.cloud.portal.admin.dao.entity.BusinessTypeEntity;
import com.maxnerva.cloud.portal.admin.dao.mapper.BusinessTypeMapper;
import com.maxnerva.cloud.portal.admin.exception.CheckException;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;

/** 
 * 业务类型处理
 * @author Ben
 *
 */
@Service
public class BusinessTypeService extends 
	AbstractSingleService<BusinessTypeMapper, BusinessTypeEntity,Integer>{


	/**
	 * 添加Business Type
	 * @param entity
	 */
	@Transactional(rollbackFor=Exception.class)
	@MethodHandler
	public void addBusinessType(BusinessTypeEntity entity, Integer userId) {
        if(checkUnique(entity)){
			this.addSelective(entity, userId);
		}
		else{
			throw new CheckException("新增操作失败，已存在重复数据",ErrorCodeConstant.EX_ADD_MULTI_DATA);
		}

	}


	/**
	 * 检查唯一性
	 * @param entity
	 */
	public Boolean checkUnique(BusinessTypeEntity entity){

		if(!StringUtils.isEmpty(entity.getBusinessTypeName())){

			if(!StringUtils.isEmpty(entity.getBusinessTypeName())){
				Integer count = this.getCount(BusinessTypeEntity.builder().businessTypeName(entity.getBusinessTypeName()).platform(entity.getPlatform()).build());

				if(count > 0){
					return false;
				}
			}
		}
		return true;
	}

    /**
     * 根据业务类型名字和平台名字检查唯一性
     * @param businessTypeName
	 * @param platform
     */
    public Boolean checkUniqueByName(String businessTypeName, String platform){

        if(!StringUtils.isEmpty(businessTypeName) && !StringUtils.isEmpty(platform)){

			Integer count = this.mapper.checkUniqueByName(businessTypeName,platform);

			if(count == 0 ){
				return true;
			}
        }else{
            throw new CheckException("查询失败，传入参数为空",ErrorCodeConstant.EX_PARA_IS_EMPTY);
        }
        return false;
    }


	/**
	 * 获取业务类型列表by platform
	 * @param platform
	 */
	public List<BusinessTypeEntity> getBusinessTypeListByPlatform(String platform){


        if(!StringUtils.isEmpty(platform))
        {
            return this.mapper.getBusinessTypeListByPlatform(platform);
        }else
        {
            throw new CheckException("查询失败，传入参数为空",ErrorCodeConstant.EX_PARA_IS_EMPTY);
        }

	}

	/**
	 * 全量更新平台类型
	 * @param businessTypeEntities
	 */
	@Transactional(rollbackFor=Exception.class)
	public void updateBusinessType(List<BusinessTypeEntity> businessTypeEntities,Integer userId) {
		if(!CollectionUtils.isEmpty(businessTypeEntities)){
			mapper.deleteAllData();
			businessTypeEntities.forEach(businessTypeEntity->{
				businessTypeEntity.setCreatedBy(userId);
				businessTypeEntity.setCreatedDate(new Date());
				businessTypeEntity.setLastModifiedBy(userId);
				businessTypeEntity.setLastModifiedDate(new Date());
				//businessTypeEntity.setDeleteFlag(false);
				mapper.insert(businessTypeEntity);
			});
		}
	}

	/**
	 * 部分更新 BusinessType
	 * @param entity
	 * @param id
	 */
	@Transactional(rollbackFor=Exception.class)
	public void updatePartBusinessType(BusinessTypeEntity entity, Integer id) {

		Integer count1 = this.getCount(BusinessTypeEntity.builder().businessTypeName(entity.getBusinessTypeName()).platform(entity.getPlatform()).build());

		BusinessTypeEntity oldEntity = this.getById(id);
		if(oldEntity.getBusinessTypeName().equals(entity.getBusinessTypeName())  && oldEntity.getPlatform().equals(entity.getPlatform()))
		{
			if(count1 < 2){
				this.mapper.updateByPrimaryKey(entity);

			}else{
				throw new CheckException("更新操作失败，数据重复",ErrorCodeConstant.EX_BUSINESS_TYPE_DUPLICATE);
			}
		}else{
			if(count1 < 1){
				this.mapper.updateByPrimaryKey(entity);
			}else{
				throw new CheckException("更新操作失败，数据重复",ErrorCodeConstant.EX_BUSINESS_TYPE_DUPLICATE);
			}
		}

	}

	/**
	 * 删除业务类型
	 * 逻辑删除
	 * @param id
	 */
	@Transactional(rollbackFor=Exception.class)
	public Boolean deleteBusinessType(Integer id,Integer userId) {

		// 校验数据是否存在
		BusinessTypeEntity entity = this.getById(id);
		if(ObjectUtils.isEmpty(entity)){
			return false;
		}
		this.removeById(id, userId);
		return true;
	}

	/**
	 * 编辑校验是否唯一
	 * @param entity
	 * @return
	 */
	public Boolean uniqueEdit(BusinessTypeEntity entity) {
		return mapper.uniqueEdit(entity)==0;
	}

}
