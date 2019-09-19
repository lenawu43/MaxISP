/*
 * @copyRight
 * 
 */
package com.maxnerva.cloud.portal.admin.api;


import com.maxnerva.cloud.common.base.api.AbstractSingleApi;
import com.maxnerva.cloud.common.base.msg.PageableResponse;
import com.maxnerva.cloud.common.base.msg.RestResponse;
import com.maxnerva.cloud.common.base.search.SearchParameter;
import com.maxnerva.cloud.common.util.QRCodeUtil;
import com.maxnerva.cloud.portal.admin.dao.entity.WebNavigationEntity;
import com.maxnerva.cloud.portal.admin.dao.mapper.WebNavigationMapper;
import com.maxnerva.cloud.portal.admin.service.WebNavigationService;
import com.maxnerva.cloud.portal.admin.service.vo.WebNavigationVoService;
import com.maxnerva.cloud.portal.admin.vo.WebNavigationVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * web 导航控制器
 * @date 2019-06-04
 * @version 
 * @author lijianjun
 *
 */
@SuppressWarnings("restriction")
@RestController
@RequestMapping("/api/web-navigation-api")
@Api(value = "web Portal导航管理", tags = {"Web导航管理API"})
public class WebNavigationApi extends AbstractSingleApi
        <WebNavigationService, WebNavigationMapper, WebNavigationEntity, Integer>{
	@Autowired
	private WebNavigationVoService webNavigationVoService;
	
	@ApiOperation(value = "查询全部web navigation", notes = "")
    @GetMapping("/all")
    public RestResponse<List<WebNavigationVo>> all() {
		SearchParameter pram  = new SearchParameter();
		pram.setLimit(100000);// 省掉查询总数
		Example otherExample =new Example(WebNavigationVo.class);
		//otherExample.and().andNotEqualTo("navigationType", "applicationGroup");// 应用组不要
		otherExample.orderBy("businessTypeOrder").asc().orderBy("order").asc();
		PageableResponse<WebNavigationVo> pageResponse = PageableResponse.<WebNavigationVo>builder().data(webNavigationVoService.search(
				WebNavigationVo.class, pram,otherExample)) ;
		return RestResponse.<List<WebNavigationVo>>builder().data(pageResponse.getData().getRows());
    }

	
	@ApiOperation(value = "获取app下载地址二维码", notes = "")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "downloadUrl", value = "url", required =false, dataType = "string", paramType = "query", defaultValue = ""),
		@ApiImplicitParam(name = "width", value = "二维码宽度", required =true, dataType = "int", paramType = "query", defaultValue = "250"),
		@ApiImplicitParam(name = "height", value = "二维码高度",required =true, dataType = "int", paramType="query", defaultValue = "250")
	})
    @GetMapping("/get-app-download-url-qrcode")
    public RestResponse<String> getAppDowanLoadQRCode(String downloadUrl,Integer width,Integer height) {
		byte[] imgByte = QRCodeUtil.generator(downloadUrl, width, height, "png");
		return RestResponse.<String>builder().data("data:image/png;base64,"+new BASE64Encoder().encode(imgByte).replaceAll("\r\n", ""));
    }
}
