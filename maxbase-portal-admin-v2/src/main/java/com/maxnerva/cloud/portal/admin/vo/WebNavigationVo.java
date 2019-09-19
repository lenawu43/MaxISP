package com.maxnerva.cloud.portal.admin.vo;

import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;












import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import tk.mybatis.mapper.annotation.KeySql;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.maxnerva.cloud.common.base.search.SearchColumn;
import com.maxnerva.cloud.common.base.dao.entity.BaseEntity;
import com.maxnerva.cloud.common.base.dao.mapper.annotation.SubSql;
import com.maxnerva.cloud.portal.admin.dao.entity.ApplicationGroupItemEntity;
/**
 * web Navigation 页面返回数据
 * @author lijianjun
 *
 */
@Data
@EqualsAndHashCode(of = "webNavigationCode", callSuper = false)
@NoArgsConstructor
@Table(name = "webNavigation_vo")
public class WebNavigationVo  implements BaseEntity<Integer>
{
	
	@ApiModelProperty(value = "编码", example = "TPM-System")
    private String webNavigationCode;

    @ApiModelProperty(value = "名称", example = "TPM管理系统")
    @SearchColumn
    private String webNavigationName;

    @ApiModelProperty(value = "图标地址", example = "http://picserver.com/123.jpg")
    private String iconPath;

    @ApiModelProperty(value = "描述", example = "设备管理平台")
    @SearchColumn
    private String description;

    @ApiModelProperty(value = "Application Code", example = "TPM")
    @SearchColumn
    private String applicationCode;

    @ApiModelProperty(value = "业务分类Id", example = "1")
    private Integer businessTypeId;
    
    @ApiModelProperty(value = "业务分类名称", example = "平台")
    private String businessTypeName;

    @ApiModelProperty(value = "网址", example = "http://www.x.com/home.html")
    private String webUrl;

    @ApiModelProperty(value = "app下载路径", example = "http://www.x.com/v1.apk")
    private String appDownloadUrl;

    @ApiModelProperty(value = "导航类型,有三种:application,navigation,applicationGroup", example = "application")
    /**
     * 导航类型,有三种:application,navigation,applicationGroup
     */
    private String navigationType;
    
    @ApiModelProperty(value = "导航分类名称", example = "应用")
    @Transient
    private String navigationTypeName;

    @ApiModelProperty(value = "状态,有两种:normal,developing", example = "normal")
    /**
     * 状态,有两种:normal,developing
     */
    private String status;
    
    @ApiModelProperty(value = "状态名称", example = "正常")
    @Transient
    private String statusName;

    @ApiModelProperty(value = "排序", example = "1")
    @Column(name="order_no")
    private Integer order;
    
    @ApiModelProperty(value = "业务分类排序", example = "1")
    @Column(name="business_type_order_no")
    private Integer businessTypeOrder;
    
    @ApiModelProperty(value = "文件下载url", example = "")
    @Transient
    private String iconDownloadPath;
    
    @ApiModelProperty(value = "文件下载校验ticket", example = "")
    @Transient
    private String ticket;
    
    @ApiModelProperty(value = "应用组")
    @Transient
    private List<ApplicationGroupItemEntity> applicationGrpList;
    
    @Id
    @KeySql(useGeneratedKeys = true)
    @ApiModelProperty(value = "主键id", example = "1")
    private Integer id;

//    @ApiModelProperty(value = "删除标志", example = "false", notes = "")
//    private Boolean deleteFlag;

    @ApiModelProperty(value = "创建人", example = "admin")
    private Integer createdBy;

    @ApiModelProperty(value = "创建时间", example = "2018-06-18 01:02:03", dataType = "Date")
    private Date createdDate;

    @ApiModelProperty(value = "最近修改人", example = "admin", notes = "最近修改人")
    private Integer lastModifiedBy;

    @ApiModelProperty(value = "最近修改时间", example = "2018-06-18 01:02:03", notes = "")
    private Date lastModifiedDate;

    @SubSql
    @Transient
    private String subsql;
    
    @Transient
    private Boolean available;
}
