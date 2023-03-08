package com.safeapp.admin.web.service;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.dto.request.RequestRiskCheckDTO;
import com.safeapp.admin.web.dto.response.ResponseCheckListProjectDTO;
import com.safeapp.admin.web.dto.response.ResponseRiskCheckDTO;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.RiskCheck;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface RiskCheckService extends CRUDService<RiskCheck> {

    RiskCheck toEntity(RequestRiskCheckDTO dto) throws NotFoundException;

    ListResponse<ResponseRiskCheckDTO> findAllByCondition(RiskCheck riskChk, Pages pages, HttpServletRequest request);

}