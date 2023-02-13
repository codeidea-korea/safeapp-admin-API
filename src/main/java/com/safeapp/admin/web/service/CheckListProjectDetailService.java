package com.safeapp.admin.web.service;

import com.safeapp.admin.web.dto.request.RequestCheckListProjectDetailDTO;
import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.CheckListProjectDetail;

public interface CheckListProjectDetailService extends CRUDService<CheckListProjectDetail> {

    CheckListProjectDetail toEntity(RequestCheckListProjectDetailDTO addDto);

    CheckListProjectDetail generate(CheckListProjectDetail oldChkPrjDet);

}