package com.maxnerva.cloud.portal.admin.service.vo;

import com.maxnerva.cloud.common.base.search.PageResult;
import com.maxnerva.cloud.common.base.search.SearchParameter;
import org.springframework.stereotype.Service;

import com.maxnerva.cloud.common.base.search.AbstractVoService;
import com.maxnerva.cloud.portal.admin.dao.mapper.vo.MobileNavigationVoMapper;
import com.maxnerva.cloud.portal.admin.vo.MobileNavigationVo;
import tk.mybatis.mapper.entity.Example;

@Service
public class MobileNavigationVoService extends AbstractVoService<MobileNavigationVoMapper, MobileNavigationVo,Integer>{

    @Override
    public PageResult<MobileNavigationVo> search(Class<MobileNavigationVo> mobileNavigationVoClass, SearchParameter parameter) {
        return super.search(mobileNavigationVoClass, parameter);
    }

    @Override
    public PageResult<MobileNavigationVo> search(Class<MobileNavigationVo> mobileNavigationVoClass, SearchParameter parameter, Example example) {
        return super.search(mobileNavigationVoClass, parameter, example);
    }
}
