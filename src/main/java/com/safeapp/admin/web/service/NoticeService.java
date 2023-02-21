package com.safeapp.admin.web.service;

import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.Notice;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface NoticeService extends CRUDService<Notice> {

    void addFiles(Long id, List<MultipartFile> images, HttpServletRequest request) throws NotFoundException;

    Notice generate(Notice newNotice);

}