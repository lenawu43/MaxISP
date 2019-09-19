package com.maxnerva.cloud.portal.admin.api;






import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maxnerva.cloud.common.base.msg.RestResponse;
import com.maxnerva.cloud.portal.admin.exception.GetException;
import com.maxnerva.cloud.portal.admin.service.TicketManagerUtil;


/**
 * 
 * 上传文件ticket校验控制器
 * 上传文件时，文件可能比较大，影响网关性能。所以上传文件没有走网关进行鉴权，需要重新鉴权
 * @date 2019-06-04
 * @version 
 * @author lijianjun
 *
 */
@RestController
@RequestMapping("/api/api-ticket")
@Api(value = "上传文件ticket校验", tags = {"上传文件ticket校验"})
@Slf4j
public class CheckUploadFileTicketApi {
	@Autowired
	private TicketManagerUtil ticketManagerUtil;
	
	@ApiOperation(value ="获取上传文件ticket")
    @GetMapping("/check-ticket/{userCode}/{ticket}/{role}")
    public RestResponse<Boolean> checkTicket(
    		@PathVariable String userCode,
    		@PathVariable String ticket,
    		@PathVariable String role) {
		log.info("校验上传文件ticket，参数ticket：{}",ticket);
		if(StringUtils.isEmpty(ticket)){
			throw new GetException("ticket参数为空", HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
        return RestResponse.<Boolean>builder()
        		.data(ticketManagerUtil.checkTicket(userCode, ticket));
    }

}
