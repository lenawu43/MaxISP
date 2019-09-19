package com.maxnerva.cloud.portal.admin.service;


import com.maxnerva.cloud.common.base.service.AbstractSingleService;
import com.maxnerva.cloud.portal.admin.dao.entity.UserEntity;
import com.maxnerva.cloud.portal.admin.dao.mapper.UserMapper;
import org.springframework.stereotype.Service;

/** 
 * 用户管理处理
 * @author Ben
 *
 */
@Service
public class UserService extends
	AbstractSingleService<UserMapper, UserEntity,Integer>{


}
