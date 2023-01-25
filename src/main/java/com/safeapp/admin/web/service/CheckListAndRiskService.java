package com.safeapp.admin.web.service;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;

import javax.servlet.http.HttpServletRequest;

public interface CheckListAndRiskService {

    ListResponse findAllUnionCheckListAndRisk(String title, String type, YN createdAtDescended, YN nameDescended,
            YN userIdDescended, Long projectId, Pages pages, HttpServletRequest request) throws Exception;

    ListResponse findAllUnionCheckListTemplateAndRiskTemplate(String title, String type, YN createdAtDescended,
            YN nameDescended, YN userIdDescended, Long projectId, Pages pages, HttpServletRequest request) throws Exception;

}