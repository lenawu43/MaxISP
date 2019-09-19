package com.maxnerva.cloud.portal.admin.vo;

import com.maxnerva.cloud.common.base.dao.entity.BaseEntity;
import com.maxnerva.cloud.common.base.dao.mapper.annotation.SubSql;
import com.maxnerva.cloud.common.base.search.SearchColumn;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@NoArgsConstructor
@Table(name = "custom_resource")
public class CustomResourceVo implements  BaseEntity<Integer> {


    @SearchColumn
    @ApiModelProperty(value = "关键字", example = "登录logo")
    private String keyWord;

    @SearchColumn
    @ApiModelProperty(value = "关键值", example = "智能工厂六流服务项目")
    private String keyValue;

    @SearchColumn
    @ApiModelProperty(value = "类型，有两种：文字,图片", example = "文字")
    /**
     * 类型,有两种：文字,图片
     */
    private String type;

    @SearchColumn
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
