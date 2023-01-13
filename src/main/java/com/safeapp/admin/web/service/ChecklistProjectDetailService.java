package com.safeapp.admin.web.service;

import com.safeapp.admin.web.dto.request.RequestChecklistProjectDetailDTO;
import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.ChecklistProjectDetail;

public interface ChecklistProjectDetailService extends CRUDService<ChecklistProjectDetail> {
    ChecklistProjectDetail generate(ChecklistProjectDetail userSeq);

    ChecklistProjectDetail toEntity(RequestChecklistProjectDetailDTO dto);
}
