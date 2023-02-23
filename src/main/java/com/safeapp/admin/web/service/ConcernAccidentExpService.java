package com.safeapp.admin.web.service;

import com.safeapp.admin.web.dto.request.RequestConcernAccidentDTO;
import com.safeapp.admin.web.dto.request.RequestConcernAccidentEditDTO;
import com.safeapp.admin.web.dto.response.ResponseAccidentCaseDTO;
import com.safeapp.admin.web.dto.response.ResponseConcernAccidentDTO;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.ConcernAccidentExp;
import com.safeapp.admin.web.model.entity.Reports;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface ConcernAccidentExpService extends CRUDService<ConcernAccidentExp> {

    ConcernAccidentExp toAddEntity(RequestConcernAccidentDTO addDto) throws NotFoundException;

    ConcernAccidentExp toEditEntity(RequestConcernAccidentEditDTO editDto) throws NotFoundException;

    void addReport(long id, String reportReason, HttpServletRequest request);

    List<Reports> findReport(long id, HttpServletRequest request) throws Exception;

    void removeReport(long id, HttpServletRequest request);

    ListResponse<ConcernAccidentExp> findAllReports(ConcernAccidentExp conExp, Pages pages, HttpServletRequest request) throws Exception;

}
