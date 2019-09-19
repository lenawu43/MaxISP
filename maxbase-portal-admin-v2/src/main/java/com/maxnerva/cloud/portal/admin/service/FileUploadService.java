package com.maxnerva.cloud.portal.admin.service;

import com.maxnerva.cloud.portal.admin.constants.ErrorCodeConstant;
import com.maxnerva.cloud.portal.admin.exception.AddException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.util.Arrays;
import java.util.UUID;

@Slf4j
@Service
public class FileUploadService {


    @Value("${storage.assignation-root-path}")
    private String assignationRootPath;

    @Value("${storage.random-root-path}")
    private String randomRootPath;

    private String[] suffixArr = {".png", ".jpg", ".jpeg", ".bmp" };

    /**
     * 根据文件，全路径创建文件
     * @param file
     * @param fileFullName
     * @throws IOException
     */
    private void upload(MultipartFile file, String fileFullName) throws IOException {
        //fileFullName是  根文件夹路径+文件名+后缀；fileFullName文件存在则覆盖。
        // 不用考虑断点续传，分块传输。限制文件大小2MB。
        if (file.getSize() > 2097152) {
          log.info("file sizes is more than 2MB.");
          throw new AddException("ufile sizes is more than 2MB.", ErrorCodeConstant.EX_FILE_UPLOAD_IS＿SIZE_FAILED);
        }

        String fullPath = FilenameUtils.getFullPath(fileFullName);
        log.info("fileFullName" + fileFullName);
        File directory = new File(fullPath);
        if (!directory.exists() && !directory.mkdirs()) {
            throw new FileSystemException("Create directory failed!");
        }

        //文件写入相应文件路径
        file.transferTo(new File(fileFullName));
    }

    /**
     *随机上传文件图片
     * @param file
     * @return
     * @throws IOException
     */
    public String uploadImage(MultipartFile file) throws IOException {

        String BaseName = UUID.randomUUID().toString();
        //构造随机文件名
        String fileFullName = randomRootPath +  BaseName + ".jpg";
        log.info("randomRootPath:" + randomRootPath);
        log.info("fileFullName: " + fileFullName);

        upload(file, fileFullName);

        return BaseName;
    }

    /**
     * 指定文件名上传文件
     * @param file
     * @param fileName
     */
    public void uploadImage(MultipartFile file,String fileName) throws IOException {
        //构造指定文件的全路径文件
        String fileFullName = assignationRootPath + fileName;
        log.info("assignation  fileFullName is {}", fileFullName);

        //获取文件后缀
        String  suffixName = getExtName(fileName, '.');
        log.info("uploadImage suffixName is" +  suffixName);

       // 为了避免攻击性文件上传，要校验存储路径必须是图片扩展名
        if (!Arrays.asList(suffixArr).contains(suffixName)) {
            log.error("uploadImage suffixName  is incorrect.");
            throw new AddException("upload file suffixName is  incorrect", ErrorCodeConstant.EX_FILE_UPLOAD_IS＿NULL_FAILED);
        }

        upload(file, fileFullName);

    }

    /**
     * 根绝分隔符获取文件后缀名
     * @param s
     * @param split
     * @return
     */
    private static String getExtName(String s, char split) {
        int i = s.indexOf(split);
        int leg = s.length();
        return (i > 0 ? (i + 1) == leg ? " " : s.substring(i, s.length()) : " ");
    }

}


