package com.maxnerva.cloud.portal.admin.vo;

import io.swagger.annotations.ApiModelProperty;


import lombok.Data;

/**
 * dict返回数据
 * @author lijianjun
 *
 */
@Data
public class DictionaryVo {
	
	@ApiModelProperty(value = "编码", example = "normal")
    private String type;

    @ApiModelProperty(value = "名称", example = "正常")
    private String typeDescription;

}
