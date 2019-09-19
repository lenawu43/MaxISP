package com.maxnerva.cloud.portal.admin.dao.entity;

import java.util.Date;

import com.maxnerva.cloud.common.base.annotation.executor.unique.Unique;
import com.maxnerva.cloud.common.base.dao.entity.BaseEntity;
import com.maxnerva.cloud.common.base.dao.mapper.annotation.SubSql;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import tk.mybatis.mapper.annotation.KeySql;

@Data
@Builder
@EqualsAndHashCode(of = "applicationCode", callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "application_group_item")
public class ApplicationGroupItemEntity implements BaseEntity<Integer> {

    @ApiModelProperty(value = "Web导航Id", example = "1")
    private Integer webNavigationId;

    @ApiModelProperty(value = "Application Code", example = "TPM")
    @Unique(group={"add","edit"})
    @Size(min=1,max=50,message="applicationCode长度错误,1-50个字符")
    @Pattern(regexp = "^[\\s]?|[A-Za-z0-9]+$",message="applicationCode 只能包含数字和英文字母")
    private String applicationCode;

    @ApiModelProperty(value = "应用组项目名称", example = "工管")
    @Size(min=1,max=50,message="applicationGroupItemName长度错误,1-50个字符")
    private String applicationGroupItemName;

    @ApiModelProperty(value = "网址", example = "http://www.x.com")
    private String url;

    @ApiModelProperty(value = "排序", example = "1")
    @Column(name="order_no")
    private Integer order;  
    
    @ApiModelProperty(value = "图标地址", example = "http://picserver.com/123.jpg")
    @Size(min=1,max=50,message="iconPath长度错误,1-255个字符")
    private String iconPath;
    
    @Id
    @KeySql(useGeneratedKeys = true)
    @ApiModelProperty(value = "主键id", example = "1")
    @Unique(group={"edit"})
    private Integer id;

    @ApiModelProperty(value = "创建人id", example = "1",hidden=true)
    private Integer createdBy;

    @ApiModelProperty(value = "创建时间", example = "2018-06-18 01:02:03", dataType = "Date")
    private Date createdDate;

    @ApiModelProperty(value = "最近修改人id", example = "1", notes = "最近修改人",hidden=true)
    private Integer lastModifiedBy;

    @ApiModelProperty(value = "最近修改时间", example = "2018-06-18 01:02:03", notes = "")
    private Date lastModifiedDate;

    @SubSql
    @Transient
    private String subsql;
    
    @ApiModelProperty(value = "图标文件内容", example = "")
    @Transient
    private String iconPathContent;


}
