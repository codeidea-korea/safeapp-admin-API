package com.safeapp.admin.web.service;

import com.safeapp.admin.web.dto.request.RequestRiskTemplateDetailDTO;
import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.RiskTemplateDetail;
import org.apache.ibatis.javassist.NotFoundException;

public interface RiskTemplateDetailService extends CRUDService<RiskTemplateDetail> {
    RiskTemplateDetail generate(RiskTemplateDetail userSeq);

    RiskTemplateDetail toEntity(RequestRiskTemplateDetailDTO dto) throws NotFoundException;
}
