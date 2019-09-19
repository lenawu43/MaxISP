package com.maxnerva.cloud.portal.admin.controller;


import com.maxnerva.cloud.common.base.controller.AbstractSingleController;
import com.maxnerva.cloud.common.base.msg.RestResponse;
import com.maxnerva.cloud.common.util.BaseContextHandler;
import com.maxnerva.cloud.portal.admin.constants.ErrorCodeConstant;
import com.maxnerva.cloud.portal.admin.dao.entity.OperationRecordEntity;
import com.maxnerva.cloud.portal.admin.dao.mapper.OperationRecordMapper;
import com.maxnerva.cloud.portal.admin.exception.CheckException;
import com.maxnerva.cloud.portal.admin.service.OperationRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("/operation-record")
@Api(value = "用户操作记录", tags = {"用户操作记录"})
@Slf4j
public class OperationRecordController extends AbstractSingleController <OperationRecordService, OperationRecordMapper, OperationRecordEntity, Integer> {

    @PostMapping("/visit-url")
    @ApiOperation("用户存储操作信息")
    public RestResponse<Boolean> visitUrl(@RequestBody OperationRecordEntity entity , HttpServletRequest request){

        if (StringUtils.isBlank(entity.getUrl())) {
            log.error("visit url is null.");
            throw new CheckException("查询失败，传入参数为空",
                    ErrorCodeConstant.EX_PARA_IS_EMPTY);
        }
        OperationRecordEntity operationRecordEntity =
                OperationRecordEntity.builder().url(entity.getUrl()).createdDate(new Date())
                .build();
        service.addOperationRecord(operationRecordEntity, BaseContextHandler.getUserId(), request);
        return RestResponse.<Boolean>builder().data(true);

    }

}
