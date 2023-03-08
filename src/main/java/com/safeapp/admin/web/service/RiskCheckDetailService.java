package com.safeapp.admin.web.service;

import com.safeapp.admin.web.dto.request.RequestRiskCheckDetailDTO;
import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.RiskCheckDetail;
import org.apache.ibatis.javassist.NotFoundException;

import javax.servlet.http.HttpServletRequest;

public interface RiskCheckDetailService extends CRUDService<RiskCheckDetail> {

    RiskCheckDetail generate(RiskCheckDetail newRiskChkDet);

    RiskCheckDetail toEntity(RequestRiskCheckDetailDTO dto) throws NotFoundException;

    void removeAll(long id, HttpServletRequest request) throws Exception;

}