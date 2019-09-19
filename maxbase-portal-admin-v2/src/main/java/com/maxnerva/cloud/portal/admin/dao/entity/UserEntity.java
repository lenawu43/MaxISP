package com.maxnerva.cloud.portal.admin.dao.entity;


import com.maxnerva.cloud.common.base.annotation.executor.unique.Unique;
import com.maxnerva.cloud.common.base.dao.entity.BaseEntity;
import com.maxnerva.cloud.common.base.dao.mapper.annotation.SubSql;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Builder
@EqualsAndHashCode(of = "username", callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class UserEntity implements BaseEntity<Integer> {


    @ApiModelProperty(value = "用户名", example = "admin")
    @Unique
    @Size(min=1,max=64,message="用户名编码长度错误,1-64个字符")
    private String username;

    @ApiModelProperty(value = "密码", example = "xxxxxx")
    @Size(min=1,max=255,message="密码长度错误,1-255个字符")
    private String password;

    @Id
    @KeySql(useGeneratedKeys = true)
    @ApiModelProperty(value = "主键id", example = "1")
    private Integer id;

    @ApiModelProperty(value = "创建人id", example = "1",hidden=true)
    private Integer createdBy;

    @ApiModelProperty(value = "创建时间", example = "2018-06-18 01:02:03", dataType = "Date")
    private Date createdDate;

    @ApiModelProperty(value = "最近修改人id", example = "admin", notes = "最近修改人",hidden=true)
    private Integer lastModifiedBy;

    @ApiModelProperty(value = "最近修改时间", example = "2018-06-18 01:02:03", notes = "")
    private Date lastModifiedDate;

    @SubSql
    @Transient
    private String subsql;
}

