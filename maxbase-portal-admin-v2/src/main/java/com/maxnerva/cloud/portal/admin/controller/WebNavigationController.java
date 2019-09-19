/*
 * @copyRight
 * 
 */
package com.maxnerva.cloud.portal.admin.controller;

import com.maxnerva.cloud.common.base.annotation.MethodHandler;
import com.maxnerva.cloud.common.base.annotation.executor.unique.ValidateEditUnique;
import com.maxnerva.cloud.common.base.controller.AbstractSingleController;
import com.maxnerva.cloud.common.base.msg.PageableResponse;
import com.maxnerva.cloud.common.base.msg.RestResponse;
import com.maxnerva.cloud.common.base.search.SearchParameter;
import com.maxnerva.cloud.common.util.BaseContextHandler;
import com.maxnerva.cloud.portal.admin.dao.entity.WebNavigationEntity;
import com.maxnerva.cloud.portal.admin.dao.mapper.WebNavigationMapper;
import com.maxnerva.cloud.portal.admin.enums.FunctionDevelopStatusEnum;
import com.maxnerva.cloud.portal.admin.enums.NavigationTypeEnum;
import com.maxnerva.cloud.portal.admin.service.TicketManagerUtil;
import com.maxnerva.cloud.portal.admin.service.WebNavigationService;
import com.maxnerva.cloud.portal.admin.service.vo.WebNavigationVoService;
import com.maxnerva.cloud.portal.admin.vo.DictionaryVo;
import com.maxnerva.cloud.portal.admin.vo.WebNavigationVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * web 导航控制器
 * @date 2019-06-04
 * @version 
 * @author lijianjun
 *
 */
@RestController
@RequestMapping("/web-navigation")
@Api(value = "web Portal导航管理", tags = {"Web导航管理"})
@Slf4j
public class WebNavigationController extends AbstractSingleController<WebNavigationService, WebNavigationMapper, WebNavigationEntity, Integer> {
	
	private static final Long TEN_MINUTE_IN_MILLISECONDS = 600L;
	
	@Value(value = "${storage.context-path}")
	private String storageContextPath;
	
	@Value(value = "${storage.file-download-path}")
	private String fileDownloadPath;
	
	@Autowired
	private WebNavigationVoService webNavigationVoService;
	
	@Autowired
	private TicketManagerUtil ticketManagerUtil;

	@GetMapping("/searchWebNavigation")
	@ApiOperation("混合模糊搜素WebNavigation列表")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "keywords", value = "搜索关键字", required =false, dataType = "string", paramType = "query", defaultValue = ""),
		@ApiImplicitParam(name = "page", value = "当前页码", required =true, dataType = "int", paramType = "query", defaultValue = "1"),
		@ApiImplicitParam(name = "limit", value = "页面大小",required =true, dataType = "int", paramType="query", defaultValue = "10")
	})
	/**
	 * 根据关键字按名称、描述、ApplicationCode模糊搜索符合条件的WebNavigation数据,按order_no 升序，
     * 并限定于page和limit的数据范围
     * @param SearchParameter
	 * @return PageableResponse<WebNavigationVo>
	 */
	public PageableResponse<WebNavigationVo> searchWebNavigation(@ApiIgnore SearchParameter param){
		log.info("条件查询webNavigation列表，参数：SearchParameter:{}",param);
		Example otherExample =new Example(WebNavigationVo.class);
		otherExample.orderBy("order").orderBy("order").asc();
		// 默认不显示已经逻辑删除的数据
		PageableResponse<WebNavigationVo> pageableResponse 
			= PageableResponse.<WebNavigationVo>builder().data(webNavigationVoService.search(WebNavigationVo.class, param,otherExample));
		List<WebNavigationVo> webNavigationVos = pageableResponse.getData().getRows();
		if(!CollectionUtils.isEmpty(webNavigationVos)){
			webNavigationVos.forEach(webNavigationVo->{
				webNavigationVo.setNavigationTypeName(NavigationTypeEnum.getDescByType(webNavigationVo.getNavigationType()));
				webNavigationVo.setStatusName(FunctionDevelopStatusEnum.getDescByType(webNavigationVo.getStatus()));
//				webNavigationVo.setTicket(ticketManagerUtil.setTicket(BaseContextHandler.getUserCode(),
//						TEN_MINUTE_IN_MILLISECONDS,TimeUnit.SECONDS,BaseContextHandler.getApplicationCode()));
//				webNavigationVo.setIconDownloadPath(storageContextPath+fileDownloadPath+webNavigationVo.getIconPath());
//				if(webNavigationVo.getNavigationType().equals(NavigationTypeEnum.APPLICATION_GROUP.getType())){
//					webNavigationVo.setApplicationGrpList(service.getApplicationGroupListByNavigationId(webNavigationVo.getId()));
//				}
			});
		}
		// 更新业务分类数据字典
//		service.updateBusinessType(BaseContextHandler.getUserId());
		// pageableResponse.getData().setRows(webNavigationVos);
		log.info("条件查询webNavigation列表，返回结果：data:{}",webNavigationVos);
		return pageableResponse;
	}
	
	@ApiOperation(value = "添加web导航")
	@PostMapping("")
	@Override
	public RestResponse<Integer> addSelective(
			@Validated @RequestBody WebNavigationEntity entity, HttpServletResponse response) {
		log.info("addWebNavigation，参数：WebNavigationEntity:{}",entity);
		response.setStatus(HttpStatus.CREATED.value());
		service.addWebNavigation(entity,BaseContextHandler.getUserId());
		return RestResponse.<Integer>builder().data(entity.getId());
	}
	
	@ApiOperation(value = "删除移动导航")
    @DeleteMapping("/{id}")
	@Override
    public RestResponse<Boolean> removeById( 
    		@PathVariable Integer id) {
		log.info("deleteWebNavigation，参数：webNavigationId:{}",id);
		return RestResponse.<Boolean>builder().data(service.deleteWebNavigation(id,BaseContextHandler.getUserId()));
    }

	@ApiOperation(value = "全量更新移动导航")
    @PutMapping("/{id}")
	@Override
    public RestResponse<Boolean> edit( 
    		@PathVariable Integer id,
    		@Validated @RequestBody @ValidateEditUnique(group="edit") WebNavigationEntity entity) {
		log.info("fullUpdateWebNavigation，参数：WebNavigationEntity:{}",entity);
		service.fullUpdateWebNavigation(id,entity,BaseContextHandler.getUserId());
		return RestResponse.<Boolean>builder().data(true);
    }
	

	@ApiOperation(value = "部分更新移动导航")
    @PutMapping("/selective/{id}")
	@Override
    public RestResponse<Boolean> editSelective( 
    		@PathVariable Integer id,
    		@Validated @RequestBody @ValidateEditUnique(group="edit") WebNavigationEntity entity ) {
		log.info("partlyUpdateWebNavigation，参数：WebNavigationEntity:{}",entity);
		service.partlyUpdateWebNavigation(id,entity,BaseContextHandler.getUserId());
		return RestResponse.<Boolean>builder().data(true);
    }

	@ApiOperation(value = "查询位置信息,返回orderNo和description")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "webNavigationId", value = "当前记录id,添加时不传",  dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "type", value = "类型 ,添加传add,修改传update",  dataType = "String", paramType = "query")
	})
	@GetMapping("/query-order-no/{businessTypeId}")
    public RestResponse<List<Map<String,Object>>> quryOrderNo( 
    		@PathVariable Integer businessTypeId,
    		Integer webNavigationId,String type) {
		log.info("queryOrderNo，参数：webNavigationId:{},type:{}",webNavigationId,type);
		List<Map<String,Object>> result = service.quryOrderNo(businessTypeId,webNavigationId,type);
		return RestResponse.<List<Map<String,Object>>>builder().data(result);
    }
	@ApiOperation(value ="查询数据字典")
	@ApiImplicitParam(name = "type", value = "查询导航类型传navigationType,查询状态传status",  dataType = "String", paramType = "query")
    @GetMapping("/get-dict-by-type")
    public RestResponse<List<DictionaryVo>> all(String type) {
		log.info("查询数据字典信息，参数type：{}",type);
		if("navigationType".equals(type)){
			return RestResponse.<List<DictionaryVo>>builder()
		        		.data(NavigationTypeEnum.queryAll());
			
		}else if("status".equals(type)){
			 return RestResponse.<List<DictionaryVo>>builder()
		        		.data(FunctionDevelopStatusEnum.queryAll());
		}
        return RestResponse.<List<DictionaryVo>>builder()
        		.data(null);
    }
	

    @Override
    @ApiOperation(value = "根据id查询实体")
    @GetMapping("/{id}")
    public RestResponse<WebNavigationEntity> get(@PathVariable Integer id) { 
    	WebNavigationEntity entity = service.getById(id);
//    	entity.setTicket(ticketManagerUtil.setTicket(BaseContextHandler.getUserCode(),
//				TEN_MINUTE_IN_MILLISECONDS,TimeUnit.SECONDS,BaseContextHandler.getApplicationCode()));
//		entity.setIconDownloadPath(storageContextPath+fileDownloadPath+entity.getIconPath());
        return RestResponse.<WebNavigationEntity>builder().data(entity);
    }
    

	@Override
    @MethodHandler
    @ApiOperation(value = "通过Entity条件获取数据记录是否唯一。id的条件为不等于，用于在update操作")
    @GetMapping("/unique/{id}")
    public RestResponse<Boolean> unique(@PathVariable Integer id,  WebNavigationEntity entity) {
		log.info("编辑校验唯一，参数id:{},entity:{}",id,entity);
		entity.setId(id);
		return RestResponse.<Boolean>builder().data(service.uniqueEdit(entity));
    }
}
