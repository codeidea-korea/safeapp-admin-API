package com.binoofactory.cornsqure.web.service;

import com.binoofactory.cornsqure.web.dto.request.RequestChecklistProjectDetailDTO;
import com.binoofactory.cornsqure.web.dto.request.RequestRiskcheckDetailDTO;
import com.binoofactory.cornsqure.web.model.cmmn.service.BfCRUDService;
import com.binoofactory.cornsqure.web.model.entity.ChecklistProjectDetail;
import com.binoofactory.cornsqure.web.model.entity.RiskCheckDetail;
import org.apache.ibatis.javassist.NotFoundException;

public interface RiskCheckDetailService extends BfCRUDService<RiskCheckDetail> {
    RiskCheckDetail generate(RiskCheckDetail userSeq);

    RiskCheckDetail toEntity(RequestRiskcheckDetailDTO dto) throws NotFoundException;
}
