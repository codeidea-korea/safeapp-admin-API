package com.binoofactory.cornsqure.web.service;

import com.binoofactory.cornsqure.web.dto.request.RequestRiskTemplateDetailDTO;
import com.binoofactory.cornsqure.web.dto.request.RequestRiskcheckDetailDTO;
import com.binoofactory.cornsqure.web.model.cmmn.service.BfCRUDService;
import com.binoofactory.cornsqure.web.model.entity.RiskCheckDetail;
import com.binoofactory.cornsqure.web.model.entity.RiskTemplateDetail;
import org.apache.ibatis.javassist.NotFoundException;

public interface RiskTemplateDetailService extends BfCRUDService<RiskTemplateDetail> {
    RiskTemplateDetail generate(RiskTemplateDetail userSeq);

    RiskTemplateDetail toEntity(RequestRiskTemplateDetailDTO dto) throws NotFoundException;
}
