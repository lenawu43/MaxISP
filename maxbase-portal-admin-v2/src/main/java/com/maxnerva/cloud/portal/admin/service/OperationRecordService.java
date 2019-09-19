package com.maxnerva.cloud.portal.admin.service;

import com.maxnerva.cloud.common.base.annotation.MethodHandler;
import com.maxnerva.cloud.common.base.service.AbstractSingleService;
import com.maxnerva.cloud.portal.admin.dao.entity.OperationRecordEntity;
import com.maxnerva.cloud.portal.admin.dao.mapper.OperationRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;


@Service
@Slf4j
public class OperationRecordService extends
        AbstractSingleService<OperationRecordMapper, OperationRecordEntity,Integer> {


    @Transactional(rollbackFor = Exception.class)
    @MethodHandler
    public void addOperationRecord(OperationRecordEntity entity, Integer userId, HttpServletRequest request) {
        //获取远程客户端ip
        String ipString = getIpAddr(request);
        if (StringUtils.isBlank(ipString)) {
            log.error("get user ipAddr failed.");
        }
        System.out.println(ipString);
        entity.setIp(ipString);
        this.addSelective(entity, userId);


    }

    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        log.info("IP地址为： {}", ip);
        return ip;

    }
}
