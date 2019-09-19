package com.maxnerva.cloud.portal.admin.service.vo;

import com.maxnerva.cloud.common.base.search.AbstractVoService;
import com.maxnerva.cloud.common.base.search.PageResult;
import com.maxnerva.cloud.common.base.search.SearchParameter;
import com.maxnerva.cloud.portal.admin.dao.mapper.vo.BusinessTypeVoMapper;
import com.maxnerva.cloud.portal.admin.vo.BusinessTypeVo;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

@Service
public class BusinessTypeVoService extends AbstractVoService<BusinessTypeVoMapper, BusinessTypeVo,Integer>{

    @Override
    public PageResult<BusinessTypeVo> search(Class<BusinessTypeVo> businessTypeVoClass, SearchParameter parameter) {
        return super.search(businessTypeVoClass, parameter);
    }

    @Override
    public PageResult<BusinessTypeVo> search(Class<BusinessTypeVo> businessTypeVoClass, SearchParameter parameter, Example example) {
        return super.search(businessTypeVoClass, parameter, example);
    }
}
