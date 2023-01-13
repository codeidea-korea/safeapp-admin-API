package com.safeapp.admin.web.service.cmmn;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.model.entity.cmmn.BfFile;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    BfFile uploadAllowedFile(MultipartFile file, HttpServletRequest httpServletRequest);

    BfFile findById(long id, HttpServletRequest httpServletRequest);
}
