package com.safeapp.admin.web.service;

import com.safeapp.admin.web.dto.request.RequestAccidentCaseDTO;
import com.safeapp.admin.web.dto.request.RequestAccidentCaseEditDTO;
import com.safeapp.admin.web.dto.response.ResponseAccidentCaseDTO;
import com.safeapp.admin.web.dto.response.ResponseRiskCheckDTO;
import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.AccidentExp;
import org.apache.ibatis.javassist.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface AccidentExpService extends CRUDService<AccidentExp> {

    AccidentExp toAddEntity(RequestAccidentCaseDTO addDto) throws NotFoundException;

    AccidentExp toEditEntity(RequestAccidentCaseEditDTO editDto) throws NotFoundException;

}