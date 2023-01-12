package com.binoofactory.cornsqure.web.service;

import com.binoofactory.cornsqure.web.dto.request.RequestChecklistProjectResultDTO;
import com.binoofactory.cornsqure.web.model.cmmn.service.BfCRUDService;
import com.binoofactory.cornsqure.web.model.entity.ChecklistProjectResult;

public interface ChecklistProjectResultService extends BfCRUDService<ChecklistProjectResult> {
    ChecklistProjectResult generate(ChecklistProjectResult userSeq);
    ChecklistProjectResult toEntity(RequestChecklistProjectResultDTO dto);
}
