/*
 * @copyRight
 * 
 */
package com.maxnerva.cloud.portal.admin.controller;


import com.maxnerva.cloud.common.base.annotation.MethodHandler;
import com.maxnerva.cloud.common.base.controller.AbstractSingleController;
import com.maxnerva.cloud.common.base.msg.PageableResponse;
import com.maxnerva.cloud.common.base.msg.RestResponse;
import com.maxnerva.cloud.common.base.search.SearchParameter;
import com.maxnerva.cloud.common.util.BaseContextHandler;
import com.maxnerva.cloud.portal.admin.dao.entity.MobileNavigationEntity;
import com.maxnerva.cloud.portal.admin.dao.mapper.MobileNavigationMapper;
import com.maxnerva.cloud.portal.admin.service.MobileNavigationService;
import com.maxnerva.cloud.portal.admin.service.TicketManagerUtil;
import com.maxnerva.cloud.portal.admin.service.vo.MobileNavigationVoService;
import com.maxnerva.cloud.portal.admin.vo.MobileNavigationVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 移动端 导航控制器
 * @date 2019-06-04
 * @version 
 * @author lijianjun
 *
 */
@RestController
@RequestMapping("/mobile-navigation")
@Api(value = "Mobile Portal导航管理", tags = {"移动导航管理"})
@Slf4j
public class MobileNavigationController extends AbstractSingleController
        <MobileNavigationService, MobileNavigationMapper, MobileNavigationEntity, Integer>{
	
	private static final Long TEN_MINUTE_IN_MILLISECONDS = 600L;
	
	@Value(value = "${storage.context-path}")
	private String storageContextPath;
	
	@Value(value = "${storage.file-download-path}")
	private String fileDownloadPath;
	
	@Autowired
	private MobileNavigationVoService mobileNavigationVoService;
    
//	@Autowired
//	private StorageClientService fileService;
	
	@Autowired
	private TicketManagerUtil ticketManagerUtil;

	@GetMapping("/searchMobileNavigation")
	@ApiOperation("混合模糊搜素MobileNavigation列表")
		@ApiImplicitParams({
		@ApiImplicitParam(name = "keywords", value = "搜索关键字", required =false, dataType = "string", paramType = "query", defaultValue = ""),
		@ApiImplicitParam(name = "page", value = "当前页码", required =true, dataType = "int", paramType = "query", defaultValue = "1"),
		@ApiImplicitParam(name = "limit", value = "页面大小",required =true, dataType = "int", paramType="query", defaultValue = "10")
	})
	/**
	 * 根据关键字按名称、描述、ApplicationCode模糊搜索符合条件的MobileNavigation数据,按order_no 升序，
     * 并限定于page和limit的数据范围
     * @param SearchParameter
	 * @return PageableResponse<MobileNavigationVo>
	 */
	public PageableResponse<MobileNavigationVo> searchMobileNavigation(@ApiIgnore SearchParameter param){
		log.info("条件查询mobileNavigation列表，参数：SearchParameter:{}",param);
		Example otherExample =new Example(MobileNavigationVo.class);
		otherExample.orderBy("businessTypeOrder").asc().orderBy("order").asc();
		// 默认不显示已经逻辑删除的数据
		PageableResponse<MobileNavigationVo> pageableResponse 
			= PageableResponse.<MobileNavigationVo>builder().data(mobileNavigationVoService.search(MobileNavigationVo.class, param,otherExample)) ;
		List<MobileNavigationVo> mobileNavigationVos = pageableResponse.getData().getRows();
//		if(!CollectionUtils.isEmpty(mobileNavigationVos)){
//			mobileNavigationVos.forEach(mobileNavigationVo->{
//				mobileNavigationVo.setTicket(ticketManagerUtil.setTicket(BaseContextHandler.getUserCode(),
//				TEN_MINUTE_IN_MILLISECONDS,TimeUnit.SECONDS,BaseContextHandler.getApplicationCode()));
//				mobileNavigationVo.setIconDownloadPath(storageContextPath+fileDownloadPath+mobileNavigationVo.getIconPath());
//			});
//		}
		return pageableResponse;
		
	}
	
	@ApiOperation(value = "添加移动导航")
	@PostMapping("")
	@Override
	public RestResponse<Integer> addSelective(
			@Validated @RequestBody  MobileNavigationEntity entity , HttpServletResponse response
			) {
		log.info("addMobileNavigation，参数：MobileNavigationEntity:{}",entity);
//        if(br.hasErrors()) {
//        	StringBuilder err = new StringBuilder();
//        	br.getFieldErrors().forEach(e->{
//        		log.info(e.getDefaultMessage());
//        		err.append(","+e.getField()+":"+e.getDefaultMessage());
//        	});
//        	
//            throw new BusinessException(err.toString().substring(1, err.toString().length()), 500);
//        }
		response.setStatus(HttpStatus.CREATED.value());
        service.addMobileNavigation(entity, BaseContextHandler.getUserId());
        return RestResponse.<Integer>builder().data(entity.getId());
    }
	
	@ApiOperation(value = "删除移动导航")
    @DeleteMapping("/{id}")
	@Override
    public RestResponse<Boolean> removeById( 
    		@PathVariable Integer id) {
		log.info("deleteMobileNavigation，参数：mobileNavigationId:{}",id);
		return RestResponse.<Boolean>builder().data(service.deleteMobileNavigation(id,BaseContextHandler.getUserId()));
    }

	@ApiOperation(value = "全量更新移动导航")
    @PutMapping("/{id}")
	@Override
    public RestResponse<Boolean> edit( 
    		@PathVariable Integer id,
    		@Validated @RequestBody MobileNavigationEntity entity) {
		log.info("fullUpdateMobileNavigation，参数：MobileNavigationEntity:{}",entity);
		service.fullUpdateMobileNavigation(id,entity,BaseContextHandler.getUserId());
		return RestResponse.<Boolean>builder().data(true);
    }
	

	@ApiOperation(value = "部分更新移动导航")
    @PutMapping("/selective/{id}")
	@Override
    public RestResponse<Boolean> editSelective( 
    		@PathVariable Integer id,
    		@Validated @RequestBody  MobileNavigationEntity entity ) {
		log.info("partlyUpdateMobileNavigation，参数：MobileNavigationEntity:{}",entity);
		service.partlyUpdateMobileNavigation(id,entity,BaseContextHandler.getUserId());
		return RestResponse.<Boolean>builder().data(true);
    }

	@ApiOperation(value = "查询位置信息,返回orderNo和description")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "mobileNavigationId", value = "当前记录id,添加时不传",  dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "type", value = "类型 ,添加传add,修改传update",  dataType = "String", paramType = "query")
	})
	
    @GetMapping("/query-order-no/{businessTypeId}")
    public RestResponse<List<Map<String,Object>>> quryOrderNo( 
    		@PathVariable Integer businessTypeId,
    		Integer mobileNavigationId,String type) {
		log.info("queryOrderNo，参数：mobileNavigationId:{},type:{}",mobileNavigationId,type);
		List<Map<String,Object>> result = service.quryOrderNo(businessTypeId,mobileNavigationId,type);
		return RestResponse.<List<Map<String,Object>>>builder().data(result);
    }
	@Override
    @ApiOperation(value = "根据id查询实体")
    @GetMapping("/{id}")
    public RestResponse<MobileNavigationEntity> get(@PathVariable Integer id) { 
		MobileNavigationEntity entity = service.getById(id);
//		entity.setTicket(ticketManagerUtil.setTicket(BaseContextHandler.getUserCode(),
//				TEN_MINUTE_IN_MILLISECONDS,TimeUnit.SECONDS,BaseContextHandler.getApplicationCode()));
//		entity.setIconDownloadPath(storageContextPath+fileDownloadPath+entity.getIconPath());
        return RestResponse.<MobileNavigationEntity>builder().data(entity);
    }
	
	@Override
    @MethodHandler
    @ApiOperation(value = "通过Entity条件获取数据记录是否唯一。id的条件为不等于，用于在update操作")
    @GetMapping("/unique/{id}")
    public RestResponse<Boolean> unique(@PathVariable Integer id,  MobileNavigationEntity entity) {
		log.info("编辑校验唯一，参数id:{},entity:{}",id,entity);
		entity.setId(id);
		return RestResponse.<Boolean>builder().data(service.uniqueEdit(entity));
    }
}
