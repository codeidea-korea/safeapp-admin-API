package com.safeapp.admin.web.service;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.cmmn.BfListResponse;
import com.safeapp.admin.web.model.cmmn.BfPage;

public interface ChecklistAndRiskService {
    BfListResponse findAllUnionChecklistAndRisk(String title, String type, YN createdAtDescended,
                                                YN nameDescended, YN userIdDescended, Long projectId, BfPage bfPage) throws Exception;

    BfListResponse findAllUnionChecklistTemplateAndRiskTemplate(String title, String type,
                                                                YN createdAtDescended, YN nameDescended, YN userIdDescended, Long projectId, BfPage bfPage) throws Exception;
}
