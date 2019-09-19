/*
 * @copyRight
 * 
 */
package com.maxnerva.cloud.portal.admin.controller;


import com.maxnerva.cloud.common.base.controller.AbstractSingleController;
import com.maxnerva.cloud.common.base.msg.RestResponse;
import com.maxnerva.cloud.common.util.BaseContextHandler;
import com.maxnerva.cloud.portal.admin.constants.ErrorCodeConstant;
import com.maxnerva.cloud.portal.admin.dao.entity.UserEntity;
import com.maxnerva.cloud.portal.admin.dao.mapper.UserMapper;
import com.maxnerva.cloud.portal.admin.exception.CheckException;
import com.maxnerva.cloud.portal.admin.service.UserService;
import com.maxnerva.cloud.portal.admin.vo.UserChangePasswordVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * 用户管理制器
 * @date 2019-07-29
 * @version 
 * @author Ben
 *
 */
@RestController
@RequestMapping("/user")
@Api(value = "user", tags = {"userManagement"})
@Slf4j
public class UserController extends AbstractSingleController
        <UserService, UserMapper, UserEntity, Integer>{


    @ApiOperation(value = "添加用户")
    @PostMapping("")
    @Override
    public RestResponse<Integer> addSelective(
            @Validated @RequestBody UserEntity entity, HttpServletResponse response) {
        response.setStatus(HttpStatus.CREATED.value());
        service.addSelective(entity, BaseContextHandler.getUserId());
        return RestResponse.<Integer>builder().data(entity.getId());
    }

    @ApiOperation(value = "用户登录验证")
    @PostMapping("/login")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "string", paramType = "query", defaultValue = "admin"),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "string", paramType = "query", defaultValue = "123456")
    })
    public RestResponse<Boolean> loginValidae(
            @Validated @RequestBody UserEntity userEntity) {
        if (StringUtils.isAnyBlank(userEntity.getUsername())) {
            log.warn("username or password is null or blank");
            return RestResponse.<Boolean>builder().data(false);
        }
        Integer count = this.service.getCount(userEntity);
        if (count == 1){
            return RestResponse.<Boolean>builder().data(true);
        }else {
            return RestResponse.<Boolean>builder().data(false);
        }
    }

    @PutMapping("/changePassword")
    @ApiOperation("修改用户密码")
    public RestResponse<String> changePassword(@RequestBody UserChangePasswordVo vo) {
        UserEntity userEntity = this.service.getById(1);
        if (!vo.getOldPassword().equals(userEntity.getPassword())) {
            log.warn("用户[{}]");
            throw new CheckException("原密码不正确！",ErrorCodeConstant.EX_OLD_PASS_IS_WRONG);
        }

        if (vo.getNewPassword().equals(userEntity.getPassword())) {

            throw new CheckException("新密码不能和旧密码一样！",ErrorCodeConstant.EX_OLD_NEW_NOT_MATCH);
        }

        userEntity.setPassword(vo.getNewPassword());

        service.editByIdSelective(userEntity,BaseContextHandler.getUserId());

        return RestResponse.<String>builder().data("ok");
    }

}
