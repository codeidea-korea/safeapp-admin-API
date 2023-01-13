package com.safeapp.admin.web.service;

import com.safeapp.admin.web.dto.request.RequestChecklistTemplateDetailDTO;
import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.ChecklistTemplateDetail;

public interface ChecklistTemplateDetailService extends CRUDService<ChecklistTemplateDetail> {
    ChecklistTemplateDetail generate(ChecklistTemplateDetail userSeq);

    ChecklistTemplateDetail toEntity(RequestChecklistTemplateDetailDTO dto);
}
