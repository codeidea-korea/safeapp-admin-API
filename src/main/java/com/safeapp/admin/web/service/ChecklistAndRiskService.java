package com.safeapp.admin.web.service;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;

public interface ChecklistAndRiskService {
    ListResponse findAllUnionChecklistAndRisk(String title, String type, YN createdAtDescended,
                                              YN nameDescended, YN userIdDescended, Long projectId, Pages bfPage) throws Exception;

    ListResponse findAllUnionChecklistTemplateAndRiskTemplate(String title, String type,
                                                              YN createdAtDescended, YN nameDescended, YN userIdDescended, Long projectId, Pages bfPage) throws Exception;
}
