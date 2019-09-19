package com.maxnerva.cloud.portal.admin.dao.mapper;

import com.maxnerva.cloud.common.base.dao.mapper.CommonMapper;
import com.maxnerva.cloud.portal.admin.dao.entity.BusinessTypeEntity;
import com.maxnerva.cloud.portal.admin.dao.entity.CustomResourceEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BusinessTypeMapper extends CommonMapper<BusinessTypeEntity>{

	void deleteAllData();

	List<BusinessTypeEntity> getBusinessTypeListByPlatform(@Param(value = "platform") String platform);

	Integer checkUniqueByName(@Param(value = "businessTypeName") String businessTypeName, @Param(value = "platform") String platform);

	Integer uniqueEdit(BusinessTypeEntity entity);

}
