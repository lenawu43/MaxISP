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
import com.maxnerva.cloud.portal.admin.dao.entity.BusinessTypeEntity;
import com.maxnerva.cloud.portal.admin.dao.mapper.BusinessTypeMapper;
import com.maxnerva.cloud.portal.admin.service.BusinessTypeService;
import com.maxnerva.cloud.portal.admin.service.vo.BusinessTypeVoService;
import com.maxnerva.cloud.portal.admin.vo.BusinessTypeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 业务分类控制器
 * @date 2019-06-04
 * @version 
 * @author Ben
 *
 */
@RestController
@RequestMapping("/business-type")
@Api(value = "businessType", tags = {"businessTypeManagement"})
@Slf4j
public class BusinessTypeController extends AbstractSingleController<BusinessTypeService, BusinessTypeMapper,  BusinessTypeEntity, Integer> {

    @Autowired
    private BusinessTypeVoService businessTypeVoService;

    @GetMapping("/searchBusinessType")
    @ApiOperation("混合模糊搜素businessType列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keywords", value = "搜索关键字", required =false, dataType = "string", paramType = "query", defaultValue = ""),
            @ApiImplicitParam(name = "page", value = "当前页码", required =true, dataType = "int", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "limit", value = "页面大小",required =true, dataType = "int", paramType="query", defaultValue = "10")
    })
    /**
     * 根据关键字按业务类型名字模糊搜索符合条件的BusinessType,platform数据,按order升序排列，
     * 并限定于page和limit的数据范围
     * @param SearchParameter
     * @return PageableResponse<BusinessTypeVo>
     */
    public PageableResponse<BusinessTypeVo> searchBusinessType(@ApiIgnore SearchParameter param){

        Example otherExample =new Example(BusinessTypeVo.class);
        otherExample.orderBy("order").orderBy("lastModifiedDate").desc();
        PageableResponse<BusinessTypeVo> pageableResponse
                = PageableResponse.<BusinessTypeVo>builder().data(businessTypeVoService.search(BusinessTypeVo.class, param,otherExample));
        return pageableResponse;
    }

    @ApiOperation(value = "添加BusinessType")
    @PostMapping("")
    @Override
    public RestResponse<Integer> addSelective(@Validated @RequestBody BusinessTypeEntity entity, HttpServletResponse response) {
        log.info("addBusinessType，参数：BusinessTypeEntity:{}",entity);
        response.setStatus(HttpStatus.CREATED.value());
        service.addBusinessType(entity, BaseContextHandler.getUserId());
        return RestResponse.<Integer>builder().data(entity.getId());
    }

    
	@ApiOperation(value ="根据平台查询业务分类列表")
    @ApiImplicitParam(name = "platform", value = "web,mobile",  dataType = "String", paramType = "query")
    @GetMapping("/get-business-type-list-by-platform")
    public RestResponse<List<BusinessTypeEntity>> getBusinessTypeListByPlatform(String platform) {

        return RestResponse.<List<BusinessTypeEntity>>builder().data(this.service.getBusinessTypeListByPlatform(platform));
    }

    @ApiOperation(value = "全量更新业务类型")
    @PutMapping("/{id}")
    @Override
    public RestResponse<Boolean> edit(
            @PathVariable Integer id,
            @Validated @RequestBody BusinessTypeEntity entity ) {

        this.service.updatePartBusinessType(entity, id);
        return RestResponse.<Boolean>builder().data(true);
    }

    @Override
    @ApiOperation(value = "根据id查询实体")
    @GetMapping("/{id}")
    public RestResponse<BusinessTypeEntity> get(@PathVariable Integer id) {
        BusinessTypeEntity entity = service.getById(id);
        return RestResponse.<BusinessTypeEntity>builder().data(entity);
    }

    @ApiOperation(value = "根据id删除实体")
    @DeleteMapping("/{id}")
    @Override
    public RestResponse<Boolean> removeById(
            @PathVariable Integer id) {
        return RestResponse.<Boolean>builder().data(service.deleteBusinessType(id,BaseContextHandler.getUserId()));
    }

    @ApiOperation(value = "根据业务类型名字和platform查询是否唯一")
    @GetMapping("/unique-validate")
    public RestResponse<Boolean> get(@RequestParam String businessTypeName, @RequestParam String platform) {

        return RestResponse.<Boolean>builder().data(this.service.checkUniqueByName(businessTypeName,platform));
    }

    @MethodHandler
    @ApiOperation(value = "通过Entity条件获取数据记录是否唯一。id的条件为不等于，用于在update操作")
    @GetMapping("/unique/{id}")
    @Override
    public RestResponse<Boolean> unique(@PathVariable Integer id,  BusinessTypeEntity entity) {
        entity.setId(id);
        return RestResponse.<Boolean>builder().data(service.uniqueEdit(entity));
    }

}
