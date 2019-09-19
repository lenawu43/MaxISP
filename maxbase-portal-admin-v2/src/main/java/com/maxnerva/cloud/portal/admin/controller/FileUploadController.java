package com.maxnerva.cloud.portal.admin.controller;


import com.maxnerva.cloud.common.base.msg.RestResponse;
import com.maxnerva.cloud.portal.admin.constants.ErrorCodeConstant;
import com.maxnerva.cloud.portal.admin.exception.AddException;
import com.maxnerva.cloud.portal.admin.service.FileUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/fileUpload")
@Api(value = "上传文件", tags = {"上传文件"})
@Slf4j
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public RestResponse<String> uploadImage(@RequestParam("file")MultipartFile file) throws IOException {

        if(null == file) {
            log.error("upload file is  null.");
            return  RestResponse.<String>builder().data("file can not null.");
        }

        String Res =  fileUploadService.uploadImage(file);

        return RestResponse.<String>builder().data(Res);

    }

    @PostMapping("/uploadByFileName")
    @ApiOperation("文件上传指定文件名")
    public RestResponse<String> uploadImage(@RequestParam("file")MultipartFile file,String fileName) throws IOException {
        if(null == file) {
            log.error("file can not null.");
            throw new AddException("file can not null", ErrorCodeConstant.EX_FILE_UPLOAD_IS＿NULL_FAILED);
        }

        if(StringUtils.isEmpty(fileName) || StringUtils.isBlank(fileName)) {
            log.error("upload fileName can not be null.");
            throw new AddException("upload fileName can not be null.", ErrorCodeConstant.EX_FILE_UPLOAD_IS＿NULL_FAILED);
        }

        fileUploadService.uploadImage(file, fileName);

        return RestResponse.<String>builder().data("upload file ok.");
    }


}
