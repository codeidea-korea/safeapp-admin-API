package com.binoofactory.cornsqure.web.components;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;

import com.binoofactory.cornsqure.utils.DateUtil;
import com.binoofactory.cornsqure.web.model.entity.cmmn.BfFile;

@Component
public class BfFileUploadProvider {

    @Value("${user.upload.path}")
    public String uploadPath;

    @Value("${user.upload.web.path}")
    public String webPath;
    
    @Value("${user.upload.allow.type}")
    public String allowType;

    private final DateUtil dateUtil;

    @Autowired
    public BfFileUploadProvider(DateUtil dateUtil) {
        this.dateUtil = dateUtil;
    }

    public String getExt(MultipartFile file) {
        return FilenameUtils.getExtension(file.getOriginalFilename());
    }

    public boolean chkFileType(MultipartFile file) {
        String[] allowExtTypes = allowType.split(",");
        String ext = ("" + getExt(file)).toLowerCase();

        for (String allowExtType : allowExtTypes) {
            if (ext.equals(allowExtType)) {
                return true;
            }
        }
        return false;
    }
    
    public String getOriginName(MultipartFile file) {
        return FilenameUtils.getBaseName(file.getOriginalFilename());
    }

    public BfFile save(MultipartFile file) throws Exception {
        if (!chkFileType(file)) {
            throw new Exception("허용된 파일 타입이 아닙니다.");
        }

        StringBuilder serverFilePath = new StringBuilder();
        String now = dateUtil.getDatetimeDetail();
        serverFilePath.append(uploadPath);
        serverFilePath.append("/");
        serverFilePath.append(now);
        serverFilePath.append("/");

        if (!new File(serverFilePath.toString()).exists()) {
            new File(serverFilePath.toString()).mkdirs();
        }

//        serverFilePath.append(getOriginName(file));
        serverFilePath.append(now);
        serverFilePath.append("." + getExt(file));
        file.transferTo(new File(serverFilePath.toString()));

        return BfFile.builder()
            .fileGroupNo(1)
            .webPath(webPath + "/" + now + "/" + now + "." + getExt(file))
            .fileName(serverFilePath.toString())
            .type(getExt(file))
            .build();
    }

    public boolean delete(File file) throws Exception {
        if (!file.exists() || !file.isFile() || !file.canWrite()) {
            throw new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE, "삭제가 불가능합니다.");
        }
        return file.delete();
    }
}
