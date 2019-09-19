package com.maxnerva.cloud.portal.admin.dao.mapper;

import org.apache.ibatis.annotations.Param;

import com.maxnerva.cloud.common.base.dao.mapper.CommonMapper;
import com.maxnerva.cloud.portal.admin.dao.entity.WebNavigationEntity;

public interface WebNavigationMapper extends CommonMapper<WebNavigationEntity> {

	/**
	 * 目标记录往上移动更新中间数据的orderNO
	 * @param orgOrderNo
	 * @param newOrderNo
	 * @param businessTypeId 
	 */
	void updateOldDataOrderNoTargetUp(@Param("orgOrderNo") Integer orgOrderNo, @Param("newOrderNo")Integer newOrderNo, @Param("businessTypeId") Integer businessTypeId);

	/**
	 * 目标记录往下移动更新中间数据的orderNO
	 * @param orgOrderNo
	 * @param newOrderNo
	 */
	void updateOldDataOrderNoTargetDown(@Param("orgOrderNo") Integer orgOrderNo, @Param("newOrderNo")Integer newOrderNo, @Param("businessTypeId") Integer businessTypeId);

	int uniqueEdit(WebNavigationEntity entity);

}
