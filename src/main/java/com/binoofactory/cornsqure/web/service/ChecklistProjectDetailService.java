package com.binoofactory.cornsqure.web.service;

import com.binoofactory.cornsqure.web.dto.request.RequestChecklistProjectDetailDTO;
import com.binoofactory.cornsqure.web.model.cmmn.service.BfCRUDService;
import com.binoofactory.cornsqure.web.model.entity.ChecklistProjectDetail;

public interface ChecklistProjectDetailService extends BfCRUDService<ChecklistProjectDetail> {
    ChecklistProjectDetail generate(ChecklistProjectDetail userSeq);

    ChecklistProjectDetail toEntity(RequestChecklistProjectDetailDTO dto);
}
