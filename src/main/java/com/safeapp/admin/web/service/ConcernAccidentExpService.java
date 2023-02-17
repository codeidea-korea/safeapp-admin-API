package com.safeapp.admin.web.service;

import com.safeapp.admin.web.dto.request.RequestConcernAccidentDTO;
import com.safeapp.admin.web.dto.request.RequestConcernAccidentEditDTO;
import com.safeapp.admin.web.dto.response.ResponseAccidentCaseDTO;
import com.safeapp.admin.web.dto.response.ResponseConcernAccidentDTO;
import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.ConcernAccidentExp;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface ConcernAccidentExpService extends CRUDService<ConcernAccidentExp> {

    ConcernAccidentExp toAddEntity(RequestConcernAccidentDTO addDto) throws NotFoundException;

    ConcernAccidentExp toEditEntity(RequestConcernAccidentEditDTO editDto) throws NotFoundException;

    void report(long id, String reportReason, HttpServletRequest request);

}
