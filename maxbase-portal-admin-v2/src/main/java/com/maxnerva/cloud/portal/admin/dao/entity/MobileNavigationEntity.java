package com.maxnerva.cloud.portal.admin.dao.entity;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

import tk.mybatis.mapper.annotation.KeySql;

import com.maxnerva.cloud.common.base.annotation.executor.unique.Unique;
import com.maxnerva.cloud.common.base.dao.entity.BaseEntity;
import com.maxnerva.cloud.common.base.dao.mapper.annotation.SubSql;

@Data
@Builder
@EqualsAndHashCode(of = "mobileNavigationCode", callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mobile_navigation")
public class MobileNavigationEntity implements BaseEntity<Integer>{



	@ApiModelProperty(value = "编码", example = "TPM-System")
	@Size(min=1,max=50,message="编码长度错误,1-50个字符")
	@Unique
    private String mobileNavigationCode;

    @ApiModelProperty(value = "名称", example = "TPM管理系统")
    @Size(min=1,max=50,message="名称长度错误，1-50个字符")
    private String mobileNavigationName;

    @ApiModelProperty(value = "图标地址", example = "http://picserver.com/123.jpg")
    @Size(min=1,max=50,message="iconPath长度错误,1-255个字符")
    private String iconPath;

    @ApiModelProperty(value = "描述", example = "设备管理平台")
    @Size(min=0,max=100,message="描述长度错误,1-100个字符")
    private String description;

    @ApiModelProperty(value = "Application Code", example = "TPM")
    @Size(min=0,max=100,message="Application Code长度错误,1-100个字符")
    @Unique
    @Pattern(regexp = "^[\\s]?|[A-Za-z0-9]+$",message="applicationCode 只能包含数字和英文字母")
    private String applicationCode;

    @ApiModelProperty(value = "业务分类Id", example = "1")
    private Integer businessTypeId;

    @ApiModelProperty(value = "移动版网页地址", example = "http://www.x.com/home.html")
    @Size(min=1,max=500,message="URL长度错误,1-500个字符")
    private String url;

    @ApiModelProperty(value = "app启动地址", example = "http://www.x.com/v1.apk")
    @Size(min=0,max=500,message="urlScheme长度错误,1-500个字符")
    private String urlScheme;

    @ApiModelProperty(value = "排序", example = "1")
    @Range(min = 0, max = 99999, message = "位于 信息错误")
    @Column(name="order_no")
    private Integer order;
    
    @Id
    @KeySql(useGeneratedKeys = true)
    @ApiModelProperty(value = "主键id", example = "1")
    @Unique
    private Integer id;

    @ApiModelProperty(value = "创建人", example = "1",hidden=true)
    private Integer createdBy;

    @ApiModelProperty(value = "创建时间", example = "2018-06-18 01:02:03", dataType = "Date")
    private Date createdDate;

    @ApiModelProperty(value = "最近修改人", example = "1", notes = "最近修改人",hidden=true)
    
    private Integer lastModifiedBy;

    @ApiModelProperty(value = "最近修改时间", example = "2018-06-18 01:02:03", notes = "")
    //@Version(nextVersion = NewNextVersion.class)
    //@JSONField(format ="yyyy-MM-dd HH:mm:ss.SSS")
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    private Date lastModifiedDate;

    @SubSql
    @Transient
    private String subsql;
    
    @ApiModelProperty(value = "文件下载url", example = "")
    @Transient
    private String iconDownloadPath;
    
    @ApiModelProperty(value = "文件下载校验ticket", example = "")
    @Transient
    private String ticket;
}
