package com.maxnerva.cloud.portal.admin.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserChangePasswordVo {

    private String oldPassword;

    private String newPassword;
}
