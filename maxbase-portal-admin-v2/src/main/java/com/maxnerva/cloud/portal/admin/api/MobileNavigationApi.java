/*
 * @copyRight
 * 
 */
package com.maxnerva.cloud.portal.admin.api;







import java.util.List;

import com.maxnerva.cloud.common.base.search.SearchParameter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tk.mybatis.mapper.entity.Example;

import com.maxnerva.cloud.common.base.api.AbstractSingleApi;
import com.maxnerva.cloud.common.base.msg.RestResponse;
import com.maxnerva.cloud.portal.admin.dao.entity.MobileNavigationEntity;
import com.maxnerva.cloud.portal.admin.dao.mapper.MobileNavigationMapper;
import com.maxnerva.cloud.portal.admin.service.MobileNavigationService;
import com.maxnerva.cloud.portal.admin.service.vo.MobileNavigationVoService;
import com.maxnerva.cloud.portal.admin.vo.MobileNavigationVo;

/**
 * 移动端 导航控制器
 * @date 2019-06-04
 * @version 
 * @author lijianjun
 *
 */
@RestController
@RequestMapping("/api/mobile-navigation-api")
@Api(value = "Mobile Portal导航管理", tags = {"移动导航管理API"})
public class MobileNavigationApi extends AbstractSingleApi
        <MobileNavigationService, MobileNavigationMapper, MobileNavigationEntity, Integer>{
	
	
	@Autowired
	private MobileNavigationVoService webNavigationVoService;
	
    @ApiOperation(value = "查询全部mobile navigation", notes = "")
    @GetMapping("/all")
    public RestResponse<List<MobileNavigationVo>> all() {
    	SearchParameter pram  = new SearchParameter();
		pram.setLimit(100000);// 省掉查询总数
		Example otherExample =new Example(MobileNavigationVo.class);
		otherExample.orderBy("businessTypeOrder").asc().orderBy("order").asc();
        return RestResponse.<List<MobileNavigationVo>>builder().data(webNavigationVoService.search(MobileNavigationVo.class, pram,otherExample).getRows());
    }
}
