package com.binoofactory.cornsqure.web.service.cmmn;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.binoofactory.cornsqure.web.model.cmmn.service.BfCRUDService;
import com.binoofactory.cornsqure.web.model.entity.cmmn.BfFile;

public interface FileService {

    BfFile uploadAllowedFile(MultipartFile file, HttpServletRequest httpServletRequest);

    BfFile findById(long id, HttpServletRequest httpServletRequest);
}
