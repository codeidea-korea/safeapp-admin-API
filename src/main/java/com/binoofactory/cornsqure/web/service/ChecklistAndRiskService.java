package com.binoofactory.cornsqure.web.service;

import com.binoofactory.cornsqure.web.data.YN;
import com.binoofactory.cornsqure.web.model.cmmn.BfListResponse;
import com.binoofactory.cornsqure.web.model.cmmn.BfPage;

public interface ChecklistAndRiskService {
    BfListResponse findAllUnionChecklistAndRisk(String title, String type, YN createdAtDescended,
        YN nameDescended, YN userIdDescended, Long projectId, BfPage bfPage) throws Exception;

    BfListResponse findAllUnionChecklistTemplateAndRiskTemplate(String title, String type,
                                                                YN createdAtDescended, YN nameDescended, YN userIdDescended, Long projectId, BfPage bfPage) throws Exception;
}
