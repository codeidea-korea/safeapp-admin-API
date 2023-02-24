package com.safeapp.admin.web.service.cmmn;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.model.entity.cmmn.Files;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    Files uploadAllowedFile(MultipartFile file, HttpServletRequest request);

    Files findById(long id, HttpServletRequest request);

}