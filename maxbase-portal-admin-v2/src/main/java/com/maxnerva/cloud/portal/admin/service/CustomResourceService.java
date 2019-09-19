package com.maxnerva.cloud.portal.admin.service;


import com.maxnerva.cloud.common.base.annotation.MethodHandler;
import com.maxnerva.cloud.common.base.service.AbstractSingleService;
import com.maxnerva.cloud.portal.admin.constants.ErrorCodeConstant;
import com.maxnerva.cloud.portal.admin.dao.entity.CustomResourceEntity;
import com.maxnerva.cloud.portal.admin.dao.mapper.CustomResourceMapper;
import com.maxnerva.cloud.portal.admin.exception.CheckException;
import com.maxnerva.cloud.portal.admin.exception.EditException;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

/** 
 * 客户资源自定义处理
 * @author Ben
 *
 */
@Service
public class CustomResourceService extends
	AbstractSingleService<CustomResourceMapper, CustomResourceEntity,Integer>{

	/**
	 * 校验数据是否重复
	 * @param entity
	 */
	private CustomResourceEntity checkParam(CustomResourceEntity entity) {

		// 校验数据是否重复
		if(!StringUtils.isEmpty(entity.getKeyWord())){

			if(!StringUtils.isEmpty(entity.getKeyValue())){

				if(!StringUtils.isEmpty(entity.getType())){
					Integer count = this.getCount(CustomResourceEntity.builder().keyWord(entity.getKeyWord()).build());

					if(count > 0  && null == entity.getId()){
						throw new CheckException("新增操作失败，已存在重复数据",ErrorCodeConstant.EX_ADD_MULTI_DATA);
					}
					if(count > 1  && null !=entity.getId()){
						throw new CheckException("更新操作失败，已存在重复数据",ErrorCodeConstant.EX_UPD_MULTI_DATA);
					}

					return entity;
				}

			}
		}
		return null;
	}


	/**
	 * 添加客户自定义资源
	 * @param entity
	 */
	@Transactional(rollbackFor=Exception.class)
	@MethodHandler
	public void addCustomResource(CustomResourceEntity entity, Integer userId) {

		CustomResourceEntity oldEntity = checkParam(entity);
		if(ObjectUtils.isEmpty(oldEntity)){
			throw new EditException("操作失败，必填项没填",ErrorCodeConstant.EX_MUST_PARA_IS_EMPTY);
		}
		this.add(entity,userId);
	}


	/**
	 * 部分更新 CustomResource
	 * @param entity
	 * @param id
	 */
	@Transactional(rollbackFor=Exception.class)
	public void updatePartCustomResource(CustomResourceEntity entity, Integer id) {

		entity.setId(id);
		CustomResourceEntity oldEntity = checkParam(entity);
		if(ObjectUtils.isEmpty(oldEntity)){
			throw new EditException("操作失败，必填项没填",ErrorCodeConstant.EX_MUST_PARA_IS_EMPTY);
		}
		this.mapper.updateByPrimaryKey(entity);
	}

	/**
	 * 删除客户自定义资源
	 * 逻辑删除
	 * @param id
	 */
	@Transactional(rollbackFor=Exception.class)
	public Boolean deleteCustomResource(Integer id,Integer userId) {

		// 校验数据是否存在
		CustomResourceEntity entity = this.getById(id);
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
	public Boolean uniqueEdit(CustomResourceEntity entity) {
		return mapper.uniqueEdit(entity)==0;
	}
}
