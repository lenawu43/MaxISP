package com.maxnerva.cloud.portal.admin.enums;

import java.util.ArrayList;
import java.util.List;

import com.maxnerva.cloud.portal.admin.vo.DictionaryVo;

/**
 * 导航类型枚举
 * @author lijianun
 *
 */
public enum PlatformTypeEnum {
	
	WEB("web","web"),
	MOBILE("mobile","mobile");
	
	// 字段类型
    private String type;
    // 字段类型描述
    private String typeDescription;
    
    private PlatformTypeEnum(String type,String typeDescription){
    	this.type = type;
    	this.typeDescription = typeDescription;
    }
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTypeDescription() {
		return typeDescription;
	}
	public void setTypeDescription(String typeDescription) {
		this.typeDescription = typeDescription;
	}
    
    public static String getDescByType(String type){
    	for(PlatformTypeEnum tEnum : PlatformTypeEnum.values()) {
    		if(tEnum.getType().equals(type)) {
    			return tEnum.typeDescription ;
    		}
    	}
    	return null;
    }
    // 查询所有key,value
 	public static List<DictionaryVo> queryAll() {
 		List<DictionaryVo> list = new ArrayList<DictionaryVo>();
 		for (PlatformTypeEnum t : PlatformTypeEnum.values()) {
 			// System.out.println(t.getKey()+"==="+t.getValue());
 			DictionaryVo tempMap = new DictionaryVo();
 			tempMap.setType(t.getType());
 			tempMap.setTypeDescription(t.getTypeDescription());
 			list.add(tempMap);
 		}
 		return list;
 	}
    public static void main(String[] args) {
		System.out.println(PlatformTypeEnum.getDescByType("navigation"));
	}

}
