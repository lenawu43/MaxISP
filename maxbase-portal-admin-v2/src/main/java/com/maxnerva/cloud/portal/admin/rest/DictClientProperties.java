package com.maxnerva.cloud.portal.admin.rest;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Data
@ConfigurationProperties(prefix = DictClientProperties.DICT_PREFIX)
@Configuration
public class DictClientProperties {

    public static final String DICT_PREFIX = "dict";

    private String serverName = "maxbase-dict-server";
}

