package com.binoofactory.cornsqure.web.service;

import com.binoofactory.cornsqure.web.dto.request.RequestChecklistTemplateDetailDTO;
import com.binoofactory.cornsqure.web.model.cmmn.service.BfCRUDService;
import com.binoofactory.cornsqure.web.model.entity.ChecklistTemplateDetail;

public interface ChecklistTemplateDetailService extends BfCRUDService<ChecklistTemplateDetail> {
    ChecklistTemplateDetail generate(ChecklistTemplateDetail userSeq);

    ChecklistTemplateDetail toEntity(RequestChecklistTemplateDetailDTO dto);
}
