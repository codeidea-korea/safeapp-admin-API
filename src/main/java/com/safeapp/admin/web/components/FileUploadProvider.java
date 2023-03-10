package com.safeapp.admin.web.components;

import java.io.File;

import com.safeapp.admin.utils.DateUtil;
import com.safeapp.admin.web.model.entity.cmmn.Files;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;

@Component
@Slf4j
public class FileUploadProvider {

    @Value("${user.upload.path}")
    public String uploadPath;

    @Value("${user.upload.web.path}")
    public String webPath;
    
    @Value("${user.upload.allow.type}")
    public String allowType;

    private final DateUtil dateUtil;

    @Autowired
    public FileUploadProvider(DateUtil dateUtil) {
        this.dateUtil = dateUtil;
    }

    public String getExt(MultipartFile file) {
        return FilenameUtils.getExtension(file.getOriginalFilename());
    }

    public boolean chkFileType(MultipartFile file) {
        String[] allowExtTypes = allowType.split(",");
        String ext = ("" + getExt(file)).toLowerCase();

        for(String allowExtType : allowExtTypes) {
            if(ext.equals(allowExtType)) {
                return true;
            }
        }

        return false;
    }

    public Files save(MultipartFile file) throws Exception {
        if(!chkFileType(file)) {
            throw new Exception("업로드가 허용된 파일 확장자가 아닙니다.");
        }

        StringBuilder serverFilePath = new StringBuilder();
        String now = dateUtil.getDatetimeDetail();
        serverFilePath.append(uploadPath);
        serverFilePath.append("/");
        serverFilePath.append(now);
        serverFilePath.append("/");
        if(!new File(serverFilePath.toString()).exists()) {
            new File(serverFilePath.toString()).mkdirs();
        }

        serverFilePath.append(now);
        serverFilePath.append("." + getExt(file));
        file.transferTo(new File(serverFilePath.toString()));

        return
            Files.builder()
            .grpFileNo(1)
            .webFileNm(webPath + "/" + now + "/" + now + "." + getExt(file))
            .fileNm(serverFilePath.toString())
            .realName(file.getOriginalFilename())
            .uploadType(getExt(file))
            .build();
    }

    public boolean delete(File file) {
        if(!file.exists() || !file.isFile() || !file.canWrite()) {
            throw new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE, "파일 삭제가 불가능합니다.");
        }

        return file.delete();
    }

}