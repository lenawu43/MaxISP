package com.maxnerva.cloud.portal.admin.service.vo;

import com.maxnerva.cloud.common.base.search.AbstractVoService;
import com.maxnerva.cloud.common.base.search.PageResult;
import com.maxnerva.cloud.common.base.search.SearchParameter;
import com.maxnerva.cloud.portal.admin.dao.mapper.vo.CustomResourceVoMapper;
import com.maxnerva.cloud.portal.admin.vo.CustomResourceVo;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

@Service
public class CustomResourceVoService extends AbstractVoService<CustomResourceVoMapper, CustomResourceVo,Integer>{

	@Override
	public PageResult<CustomResourceVo> search(Class<CustomResourceVo> customResourceVoClass, SearchParameter parameter) {
		return super.search(customResourceVoClass, parameter);
	}

	@Override
	public PageResult<CustomResourceVo> search(Class<CustomResourceVo> customResourceVoClass, SearchParameter parameter, Example example) {
		return super.search(customResourceVoClass, parameter, example);
	}
}
