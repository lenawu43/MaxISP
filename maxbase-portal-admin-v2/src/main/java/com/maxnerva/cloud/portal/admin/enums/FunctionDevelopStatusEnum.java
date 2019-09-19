package com.maxnerva.cloud.portal.admin.enums;

import java.util.ArrayList;
import java.util.List;

import com.maxnerva.cloud.portal.admin.vo.DictionaryVo;

/**
 * 开发状态枚举
 * @author lijianun
 *
 */
public enum FunctionDevelopStatusEnum {
	
	NORMAL("normal","正常"),
	DEVELOPING("developing","开发中");
	
	// 字段类型
    private String type;
    // 字段类型描述
    private String typeDescription;
    
    private FunctionDevelopStatusEnum(String type,String typeDescription){
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
    /**
     * type转描述
     * @param type
     * @return
     */
    public static String getDescByType(String type){
    	for(FunctionDevelopStatusEnum tEnum : FunctionDevelopStatusEnum.values()) {
    		if(tEnum.getType().equals(type)) {
    			return tEnum.typeDescription ;
    		}
    	}
    	return null;
    }
    /**
     * 枚举转map
     * @return
     */
 	public static List<DictionaryVo> queryAll() {
 		List<DictionaryVo> list = new ArrayList<DictionaryVo>();
 		for (FunctionDevelopStatusEnum t : FunctionDevelopStatusEnum.values()) {
 			// System.out.println(t.getKey()+"==="+t.getValue());
 			DictionaryVo tempMap = new DictionaryVo();
 			tempMap.setType(t.getType());
 			tempMap.setTypeDescription( t.getTypeDescription());
 			list.add(tempMap);
 		}
 		return list;
 	}
    public static void main(String[] args) {
		System.out.println(FunctionDevelopStatusEnum.getDescByType("navigation"));
	}

}
