package com.safeapp.admin.web.service;

import com.safeapp.admin.web.dto.request.RequestRiskCheckDetailDTO;
import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.RiskCheckDetail;
import org.apache.ibatis.javassist.NotFoundException;

public interface RiskCheckDetailService extends CRUDService<RiskCheckDetail> {
    RiskCheckDetail generate(RiskCheckDetail userSeq);

    RiskCheckDetail toEntity(RequestRiskCheckDetailDTO dto) throws NotFoundException;
}
