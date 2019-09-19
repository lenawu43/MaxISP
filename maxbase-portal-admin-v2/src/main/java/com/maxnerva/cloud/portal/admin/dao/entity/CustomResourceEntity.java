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

/**
 * @author Solomon Jiang 20190601
 */
@Data
@Builder
@EqualsAndHashCode(of = {"keyWord", "keyValue"}, callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "custom_resource")
public class CustomResourceEntity implements  BaseEntity<Integer> {


	@ApiModelProperty(value = "关键字", example = "登录logo")
    @Size(min=1,max=255,message="关键字编码长度错误,1-255个字符")
    @Unique
    private String keyWord;

    @ApiModelProperty(value = "关键值", example = "智能工厂六流服务项目")
    @Size(min=1,max=255,message="关键值编码长度错误,1-255个字符")
    private String keyValue;

    @ApiModelProperty(value = "类型，有两种：文字,图片", example = "文字")
    /**
     * 平台,有两种：web,mobile
     */
    private String type;


    @ApiModelProperty(value = "描述", example = "图片size 1920*1440")
    @Size(min=1,max=255,message="描述编码长度错误,1-255个字符")
    private String description;
    
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
