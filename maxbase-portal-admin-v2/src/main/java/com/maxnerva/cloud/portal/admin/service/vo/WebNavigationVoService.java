package com.maxnerva.cloud.portal.admin.service.vo;

import com.maxnerva.cloud.common.base.search.PageResult;
import com.maxnerva.cloud.common.base.search.SearchParameter;
import org.springframework.stereotype.Service;

import com.maxnerva.cloud.common.base.search.AbstractVoService;
import com.maxnerva.cloud.portal.admin.dao.mapper.vo.WebNavigationVoMapper;
import com.maxnerva.cloud.portal.admin.vo.WebNavigationVo;
import tk.mybatis.mapper.entity.Example;

@Service
public class WebNavigationVoService extends AbstractVoService<WebNavigationVoMapper, WebNavigationVo,Integer>{

    @Override
    public PageResult<WebNavigationVo> search(Class<WebNavigationVo> webNavigationVoClass, SearchParameter parameter) {
        return super.search(webNavigationVoClass, parameter);
    }

    @Override
    public PageResult<WebNavigationVo> search(Class<WebNavigationVo> webNavigationVoClass, SearchParameter parameter, Example example) {
        return super.search(webNavigationVoClass, parameter, example);
    }
}
