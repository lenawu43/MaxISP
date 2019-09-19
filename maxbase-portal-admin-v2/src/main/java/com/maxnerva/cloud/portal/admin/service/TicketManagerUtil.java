package com.maxnerva.cloud.portal.admin.service;

import com.maxnerva.cloud.portal.admin.exception.GetException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

/**
 * 全局静态map 存ticket
 * 
 * ticket 管理
 * @author lijianjun
 *
 */
@Slf4j
public class TicketManagerUtil {
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	 
	 /**
	  * ticket 存入redis
	  * @param userCode
	  * @param unit
	  * @param timeout
	  * @param unit
	  */
	public synchronized String setTicket(String userCode,
			Long timeout, TimeUnit unit ,String applicationCode){
		if(StringUtils.isEmpty(userCode) ){
			throw new GetException("参数不能为空", HttpStatus.INTERNAL_SERVER_ERROR.value(), "");
		}// TODO
		String key = Base64.getEncoder().encodeToString(userCode.concat(String.valueOf(Math.random())).concat(applicationCode).getBytes());
		String returnKey = key.substring(0,key.length()-2);
		redisTemplate.opsForValue().set(returnKey,
				returnKey,timeout,unit);
		return returnKey;
	}

	 /**
	  * 校验值 ticket
	  * @param userCode
	  * @param ticket
	  * @return
	  */
	public Boolean checkTicket(String userCode, String ticket) {
		if(StringUtils.isEmpty(userCode) || StringUtils.isEmpty(ticket)){
			throw new GetException("参数不能为空", HttpStatus.INTERNAL_SERVER_ERROR.value(), "");
		}
		// TODO userCode与ticket 校验
		String decodeKey = Base64.getDecoder().decode(ticket).toString();
		if(!decodeKey.contains(userCode)){
			log.warn("userCode:{}与ticket:{}不匹配",userCode,ticket);
			return false;
		}
		String exsitValue = redisTemplate.opsForValue().get(ticket);
		if(StringUtils.isEmpty(exsitValue)){
			log.warn("key:{}不存在",ticket);
			return false;
		}
		if(!StringUtils.equals(ticket, exsitValue)){
			log.warn("ticket:{}不匹配{}",ticket,exsitValue);
			return false;
		}
		redisTemplate.delete(ticket);
		return true;
	}
	
	 /**
	  * 获取 ticket
	  * @param ticket
	  * @return
	  */
	public String getTicket(String ticket) {
		if(StringUtils.isEmpty(ticket)){
			throw new GetException("参数不能为空", HttpStatus.INTERNAL_SERVER_ERROR.value(), "");
		}
		return redisTemplate.opsForValue().get(ticket);
		
	}
	
	public String base64ToString(String base64String) {
		byte [] decodeByte = Base64.getDecoder().decode(base64String);
		try {
			return new String(decodeByte,"gbk");
		} catch (UnsupportedEncodingException e) {
			log.error("base64 解码失败",e);
			return null;
		}
		
	}
}
