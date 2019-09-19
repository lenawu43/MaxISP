package com.maxnerva.cloud.portal.admin.dao.mapper;

import com.maxnerva.cloud.common.base.dao.mapper.CommonMapper;
import com.maxnerva.cloud.portal.admin.dao.entity.CustomResourceEntity;

public interface CustomResourceMapper extends CommonMapper<CustomResourceEntity>{

    int uniqueEdit(CustomResourceEntity entity);
}
