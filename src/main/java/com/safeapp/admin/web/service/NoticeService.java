package com.safeapp.admin.web.service;

import com.safeapp.admin.web.dto.request.RequestNoticeDTO;
import com.safeapp.admin.web.dto.request.RequestNoticeEditDTO;
import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.AccidentExp;
import com.safeapp.admin.web.model.entity.Notice;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface NoticeService extends CRUDService<Notice> {

    Notice toAddEntity(RequestNoticeDTO addDto) throws NotFoundException;

    Notice toEditEntity(RequestNoticeEditDTO editDto) throws NotFoundException;

}