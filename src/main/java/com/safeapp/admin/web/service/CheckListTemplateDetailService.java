package com.safeapp.admin.web.service;

import com.safeapp.admin.web.dto.request.RequestCheckListTemplateDetailDTO;
import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.CheckListTemplateDetail;

public interface CheckListTemplateDetailService extends CRUDService<CheckListTemplateDetail> {

    CheckListTemplateDetail toEntity(RequestCheckListTemplateDetailDTO addDto);

    CheckListTemplateDetail generate(CheckListTemplateDetail userSeq);

}