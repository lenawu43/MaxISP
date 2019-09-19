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
import java.util.Date;


@Data
@Builder
@EqualsAndHashCode(of = "ip", callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "operation_record")
public class OperationRecordEntity  implements BaseEntity<Integer> {

    @ApiModelProperty(value = "url", example = "\"http://www.x.com/home.html\"")
    private String url;

    @ApiModelProperty(value = "ip", example = "127.0.0.1", hidden=true)
    private String ip;

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

}
