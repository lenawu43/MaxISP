/*
 * @copyRight
 * 
 */
package com.maxnerva.cloud.portal.admin.controller;


import com.maxnerva.cloud.common.base.annotation.executor.range.EntityIncludeRange;
import com.maxnerva.cloud.common.base.controller.AbstractSingleController;
import com.maxnerva.cloud.common.base.msg.RestResponse;
import com.maxnerva.cloud.common.util.BaseContextHandler;
import com.maxnerva.cloud.portal.admin.dao.entity.ApplicationGroupItemEntity;
import com.maxnerva.cloud.portal.admin.dao.mapper.ApplicationGroupItemMapper;
import com.maxnerva.cloud.portal.admin.service.ApplicationGroupItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  应用组控制器
 * @date 2019-06-04
 * @version 
 * @author lijianjun
 *
 */
@RestController
@RequestMapping("/application-group-item")
@Api(value = "应用组管理", tags = {"应用组管理"})
@Slf4j
public class ApplicationGroupItemController extends AbstractSingleController<ApplicationGroupItemService,ApplicationGroupItemMapper , ApplicationGroupItemEntity,Integer> {
	
	@ApiOperation(value ="查询全部")
    @GetMapping("/all")
	@Override
    public RestResponse<List<ApplicationGroupItemEntity>> all() {
		log.info("查询全部 应用组");
        return RestResponse.<List<ApplicationGroupItemEntity>>builder()
        		.data(this.service.selectAllByOrderNo());
    }
	
	@ApiOperation(value = "添加应用组")
    @PostMapping("/add")
    public RestResponse<ApplicationGroupItemEntity> addApplicationGroupItem(
			@RequestBody @EntityIncludeRange(range = { "applicationGroupItemName" ,"applicationGroupItemCode"})  ApplicationGroupItemEntity entity, BindingResult error) {
		log.info("addApplicationGroupItem，参数：ApplicationGroupItemEntity:{}",entity);
        service.addApplicationGroupItem(entity, BaseContextHandler.getUserId());
        return RestResponse.<ApplicationGroupItemEntity>builder().data(entity);
    }
	
	@ApiOperation(value = "删除应用组")
    @DeleteMapping("/{id}")
	@Override
    public RestResponse<Boolean> removeById( 
    		@PathVariable Integer id) {
		log.info("deleteApplicationGroupItem，参数：webNavigationId:{}",id);
		return RestResponse.<Boolean>builder().data(service.deleteApplicationGroupItem(id,BaseContextHandler.getUserId()));

    }

	@ApiOperation(value = "全量更新应用组")
    @PutMapping("/{id}")
	@Override
    public RestResponse<Boolean> edit( 
    		@PathVariable Integer id,
    		@RequestBody ApplicationGroupItemEntity entity) {
		log.info("fullUpdateApplicationGroupItem，参数：ApplicationGroupItemEntity:{}",entity);
		service.fullUpdateApplicationGroupItem(id,entity,BaseContextHandler.getUserId());
		return RestResponse.<Boolean>builder().data(true);
    }
	

	@ApiOperation(value = "部分更新应用组")
    @PutMapping("/selective/{id}")
	@Override
    public RestResponse<Boolean> editSelective( 
    		@PathVariable Integer id,
    		@RequestBody ApplicationGroupItemEntity entity ) {
		log.info("partlyUpdateApplicationGroupItem，参数：ApplicationGroupItemEntity:{}",entity);
		service.partlyUpdateApplicationGroupItem(id,entity,BaseContextHandler.getUserId());
		return RestResponse.<Boolean>builder().data(true);
    }
	
	@ApiOperation(value ="根据webNaviationId查询应用组 列表")
	@ApiImplicitParam(name = "webNavigationId", value = "webNavigationId",  dataType = "int", paramType = "query")
    @GetMapping("/get-list-by-web-navigation-id")
	public RestResponse<List<ApplicationGroupItemEntity>> getByWebNavigationId(Integer webNavigationId) {
		log.info("根据webNaviationId查询应用组 列表，参数webNavigationId：{}",webNavigationId);
		ApplicationGroupItemEntity applicationGroupItemEntity =
				new ApplicationGroupItemEntity();
		applicationGroupItemEntity.setWebNavigationId(webNavigationId);
		//applicationGroupItemEntity.setDeleteFlag(false);
		return RestResponse.<List<ApplicationGroupItemEntity>>builder()
        		.data(service.getListOrderBy(applicationGroupItemEntity));
    }
	
}
