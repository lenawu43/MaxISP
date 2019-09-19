/*
 * @copyRight
 * 
 */
package com.maxnerva.cloud.portal.admin.controller;





import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.maxnerva.cloud.common.base.msg.RestResponse;
import com.maxnerva.cloud.common.util.BaseContextHandler;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;


import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.maxnerva.cloud.portal.admin.service.TicketManagerUtil;

/**
 * 
 * 上传文件ticket管理控制器
 * 上传文件时，文件可能比较大，影响网关性能。所以上传文件没有走网关进行鉴权，需要重新鉴权
 * @date 2019-06-04
 * @version 
 * @author lijianjun
 *
 */
@RestController
@RequestMapping("/ticket")
@Api(value = "上传文件ticket管理", tags = {"上传文件ticket管理"})
@Slf4j
public class UploadFileTicketsController {
    
	private static final Long TEN_MINUTE_IN_MILLISECONDS = 600L;
	
	@Value(value = "${storage.context-path}")
	private String storageContextPath;
	
	@Value(value = "${storage.file-upload-path}")
	private String fileUploadPath;
	
	@Autowired
	private TicketManagerUtil ticketManagerUtil;
	
	@ApiOperation(value ="获取上传文件ticket")
    @GetMapping("/get-ticket")
    public RestResponse<Map<String,String>> getUploadFileTicket() {
		log.info("获取上传文件ticket");
		String ticket = 	ticketManagerUtil.setTicket(BaseContextHandler.getUserCode(),
				TEN_MINUTE_IN_MILLISECONDS,TimeUnit.SECONDS,BaseContextHandler.getApplicationCode());
		Map<String,String> result = new HashMap<String, String>();
		result.put("ticket", ticket);
		result.put("iconUploadPath", storageContextPath+fileUploadPath);
		return RestResponse.<Map<String,String>>builder()
        		.data(result);
    }

}
