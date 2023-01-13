package com.safeapp.admin.web.service;

import com.safeapp.admin.web.dto.request.RequestChecklistProjectResultDTO;
import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.ChecklistProjectResult;

public interface ChecklistProjectResultService extends CRUDService<ChecklistProjectResult> {
    ChecklistProjectResult generate(ChecklistProjectResult userSeq);
    ChecklistProjectResult toEntity(RequestChecklistProjectResultDTO dto);
}
