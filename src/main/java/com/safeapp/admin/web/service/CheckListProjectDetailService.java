package com.safeapp.admin.web.service;

import com.safeapp.admin.web.dto.request.RequestCheckListProjectDetailDTO;
import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.CheckListProjectDetail;

import javax.servlet.http.HttpServletRequest;

public interface CheckListProjectDetailService extends CRUDService<CheckListProjectDetail> {

    CheckListProjectDetail toEntity(RequestCheckListProjectDetailDTO dto);

    CheckListProjectDetail generate(CheckListProjectDetail newChkPrjDet);

    void removeAll(long id, HttpServletRequest request) throws Exception;

}