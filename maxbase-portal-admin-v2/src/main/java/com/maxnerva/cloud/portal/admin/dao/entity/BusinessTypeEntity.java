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

import tk.mybatis.mapper.annotation.KeySql;

/**
 * @author Solomon Jiang 20190601
 */
@Data
@Builder
@EqualsAndHashCode(of = {"businessTypeName", "platform"}, callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "business_type")
public class BusinessTypeEntity implements  BaseEntity<Integer> {


	@ApiModelProperty(value = "业务分类的名称", example = "工管")
    private String businessTypeName;

    @ApiModelProperty(value = "平台，有两种：web,mobile", example = "web")
    /**
     * 平台,有两种：web,mobile
     */
    private String platform;

    @ApiModelProperty(value = "排序", example = "1")
    @Column(name="order_no")
    private Integer order;
    
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
