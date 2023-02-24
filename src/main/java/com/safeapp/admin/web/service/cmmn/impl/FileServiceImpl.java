package com.safeapp.admin.web.service.cmmn.impl;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.utils.DateUtil;
import com.safeapp.admin.web.components.FileUploadProvider;
import com.safeapp.admin.web.model.entity.cmmn.Files;
import com.safeapp.admin.web.repos.jpa.FileRepos;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;

import com.safeapp.admin.web.service.cmmn.FileService;

@Service
@AllArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepos fileRepos;

    private final FileUploadProvider fileUploadProvider;

    @Override
    public Files uploadAllowedFile(MultipartFile file, HttpServletRequest request) {
        Files inputFile;

        try {
            inputFile = fileUploadProvider.save(file);

        } catch (Exception e) {
            e.printStackTrace();

            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "파일 업로드에 실패하였습니다.");
        }

        return fileRepos.save(inputFile);
    }

    @Override
    public Files findById(long id, HttpServletRequest request) { return fileRepos.getById(id); }

}