package com.maxnerva.cloud.portal.admin.dao.entity;

import java.util.Date;
import java.util.List;

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

/**
 * @author Solomon Jiang 20190601
 */
@Data
@Builder
@EqualsAndHashCode(of = "webNavigationCode", callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "web_navigation")
public class WebNavigationEntity implements BaseEntity<Integer> {


	@ApiModelProperty(value = "编码", example = "TPM-System")
	@Unique
	@Size(min=1,max=50,message="编码长度错误,1-50个字符")
    private String webNavigationCode;

    @ApiModelProperty(value = "名称", example = "TPM管理系统")
	@Size(min=1,max=50,message="名称长度错误,1-50个字符")
    private String webNavigationName;

    @ApiModelProperty(value = "图标地址", example = "http://picserver.com/123.jpg")
    @Size(min=1,max=255,message="iconPath长度错误,1-255个字符")
    private String iconPath;

    @ApiModelProperty(value = "描述", example = "设备管理平台")
	@Size(min=0,max=100,message="描述长度错误,0-100个字符")
    private String description;

    @ApiModelProperty(value = "Application Code", example = "TPM")
    @Unique	
    @Size(min=0,max=50,message="applicationCode长度错误,0-50个字符")
    @Pattern(regexp = "^[\\s]?|[A-Za-z0-9]+$",message="applicationCode 只能包含数字和英文字母")
    private String applicationCode;

    @ApiModelProperty(value = "业务分类Id", example = "1")
    private Integer businessTypeId;

    @ApiModelProperty(value = "网址", example = "http://www.x.com/home.html")
	@Size(min=0,max=500,message="webUrl长度错误,1-500个字符")
    private String webUrl;

    @ApiModelProperty(value = "app下载路径", example = "http://www.x.com/v1.apk")
    private String appDownloadUrl;

    @ApiModelProperty(value = "导航类型,有三种:application,navigation,applicationGroup", example = "application")
    /**
     * 导航类型,有三种:application,navigation,applicationGroup
     */
    private String navigationType;

    @ApiModelProperty(value = "状态,有两种:normal,developing", example = "normal")
    /**
     * 状态,有两种:normal,developing
     */
    private String status;

    @ApiModelProperty(value = "排序", example = "1")
    @Column(name="order_no")
    private Integer order;
    
    @ApiModelProperty(value = "应用组")
    @Transient
    private List<ApplicationGroupItemEntity> applicationGrpList;
    
    @Id
    @KeySql(useGeneratedKeys = true)
    @ApiModelProperty(value = "主键id", example = "1")
    private Integer id;

    @ApiModelProperty(value = "创建人", example = "1",hidden=true)
    private Integer createdBy;

    @ApiModelProperty(value = "创建时间", example = "2018-06-18 01:02:03", dataType = "Date")
    private Date createdDate;

    @ApiModelProperty(value = "最近修改人", example = "1", notes = "最近修改人",hidden=true)
    private Integer lastModifiedBy;

    @ApiModelProperty(value = "最近修改时间", example = "2018-06-18 01:02:03", notes = "")
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
