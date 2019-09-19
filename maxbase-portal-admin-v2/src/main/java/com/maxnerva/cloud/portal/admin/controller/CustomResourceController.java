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
import com.maxnerva.cloud.portal.admin.dao.entity.CustomResourceEntity;
import com.maxnerva.cloud.portal.admin.dao.mapper.CustomResourceMapper;
import com.maxnerva.cloud.portal.admin.service.CustomResourceService;
import com.maxnerva.cloud.portal.admin.service.vo.CustomResourceVoService;
import com.maxnerva.cloud.portal.admin.vo.CustomResourceVo;
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

/**
 * 客户资源自定义制器
 * @date 2019-07-29
 * @version 
 * @author Ben
 *
 */
@RestController
@RequestMapping("/custom-resource")
@Api(value = "customResource", tags = {"customResourceManagement"})
@Slf4j
public class CustomResourceController extends AbstractSingleController
        <CustomResourceService, CustomResourceMapper, CustomResourceEntity, Integer>{


    @Autowired
    CustomResourceVoService customResourceVoService;

    @GetMapping("/searchCustomResource")
    @ApiOperation("混合模糊搜素customResource列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keywords", value = "搜索关键字", required =false, dataType = "string", paramType = "query", defaultValue = ""),
            @ApiImplicitParam(name = "page", value = "当前页码", required =true, dataType = "int", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "limit", value = "页面大小",required =true, dataType = "int", paramType="query", defaultValue = "10")
    })
    /**
     * 根据关键字按业务类型名字模糊搜索符合条件的keyWord,keyValue,type,description数据,按id升序排列，
     * 并限定于page和limit的数据范围
     * @param SearchParameter
     * @return PageableResponse<CustomResourceVo>
     */
    public PageableResponse<CustomResourceVo> searchCustomResource(@ApiIgnore SearchParameter param){

        Example otherExample =new Example(CustomResourceVo.class);
        otherExample.orderBy("id");
        PageableResponse<CustomResourceVo> pageableResponse
                = PageableResponse.<CustomResourceVo>builder().data(customResourceVoService.search(CustomResourceVo.class, param,otherExample));
        return pageableResponse;
    }


    @ApiOperation(value = "添加customResource")
    @PostMapping("")
    @Override
    public RestResponse<Integer> addSelective(@Validated @RequestBody CustomResourceEntity entity, HttpServletResponse response) {

        response.setStatus(HttpStatus.CREATED.value());
        this.service.addCustomResource(entity, BaseContextHandler.getUserId());
        return RestResponse.<Integer>builder().data(entity.getId());
    }

    @ApiOperation(value = "全量更新客户自定义资源")
    @PutMapping("/{id}")
    @Override
    public RestResponse<Boolean> edit(
            @PathVariable Integer id,
            @Validated @RequestBody CustomResourceEntity entity ) {

        this.service.updatePartCustomResource(entity, id);
        return RestResponse.<Boolean>builder().data(true);
    }

    @Override
    @ApiOperation(value = "根据id查询实体")
    @GetMapping("/{id}")
    public RestResponse<CustomResourceEntity> get(@PathVariable Integer id) {
        CustomResourceEntity entity = service.getById(id);
        return RestResponse.<CustomResourceEntity>builder().data(entity);
    }

    @ApiOperation(value = "根据id删除实体")
    @DeleteMapping("/{id}")
    @Override
    public RestResponse<Boolean> removeById(
            @PathVariable Integer id) {
        return RestResponse.<Boolean>builder().data(service.deleteCustomResource(id,BaseContextHandler.getUserId()));
    }


    @MethodHandler
    @ApiOperation(value = "通过Entity条件获取数据记录是否唯一。id的条件为不等于，用于在update操作")
    @GetMapping("/unique/{id}")
    @Override
    public RestResponse<Boolean> unique(@PathVariable Integer id,  CustomResourceEntity entity) {
        entity.setId(id);
        return RestResponse.<Boolean>builder().data(service.uniqueEdit(entity));
    }
}
