package com.safeapp.admin.web.service.impl;

import java.util.List;
import java.util.Map;

import com.safeapp.admin.utils.DateUtil;
import com.safeapp.admin.utils.PasswordUtil;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safeapp.admin.web.repos.direct.DirectQuery;
import com.safeapp.admin.web.service.ChecklistAndRiskService;
import com.safeapp.admin.web.service.cmmn.JwtService;

@Service
public class ChecklistAndRiskServiceImpl implements ChecklistAndRiskService {

    private final DirectQuery repos;

    private final PasswordUtil passwordUtil;

    private final DateUtil dateUtil;

    @Autowired
    public ChecklistAndRiskServiceImpl(DirectQuery repos,
                                       PasswordUtil passwordUtil, DateUtil dateUtil) {

        this.repos = repos;
        this.passwordUtil = passwordUtil;
        this.dateUtil = dateUtil;
    }

    @Override
    public ListResponse findAllUnionChecklistAndRisk(String title, String type, YN createdAtDescended,
                                                     YN nameDescended, YN userIdDescended, Long projectId, Pages bfPage) throws Exception {
        List<Map<String, Object>> list = repos.findAllUnionChecklistAndRisk(title, type, createdAtDescended, nameDescended, userIdDescended, projectId,
            bfPage);
        long count = repos.countUnionChecklistAndRisk(title, type, projectId);

        return new ListResponse(count, list, bfPage);
    }

    @Override
    public ListResponse findAllUnionChecklistTemplateAndRiskTemplate(String title, String type,
                                                                     YN createdAtDescended, YN nameDescended, YN userIdDescended, Long projectId, Pages bfPage) throws Exception {
        List<Map<String, Object>> list = repos.findAllUnionChecklistTemplateAndRiskTemplate(title, type, createdAtDescended, nameDescended, 
            userIdDescended, projectId, bfPage);
        long count = repos.countAllUnionChecklistTemplateAndRiskTemplate(title, type, projectId);
        
        return new ListResponse(count, list, bfPage);
    }
}
