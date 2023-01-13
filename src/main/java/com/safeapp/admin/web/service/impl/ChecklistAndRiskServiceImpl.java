package com.safeapp.admin.web.service.impl;

import java.util.List;
import java.util.Map;

import com.safeapp.admin.utils.DateUtil;
import com.safeapp.admin.utils.PasswordUtil;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.cmmn.BfListResponse;
import com.safeapp.admin.web.model.cmmn.BfPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safeapp.admin.web.repos.direct.DirectQuery;
import com.safeapp.admin.web.service.ChecklistAndRiskService;
import com.safeapp.admin.web.service.cmmn.JwtService;

@Service
public class ChecklistAndRiskServiceImpl implements ChecklistAndRiskService {

    private final DirectQuery repos;

    private final JwtService jwtService;

    private final PasswordUtil passwordUtil;

    private final DateUtil dateUtil;

    @Autowired
    public ChecklistAndRiskServiceImpl(DirectQuery repos,
                                       JwtService jwtService, PasswordUtil passwordUtil, DateUtil dateUtil) {

        this.repos = repos;
        this.jwtService = jwtService;
        this.passwordUtil = passwordUtil;
        this.dateUtil = dateUtil;
    }

    @Override
    public BfListResponse findAllUnionChecklistAndRisk(String title, String type, YN createdAtDescended,
                                                       YN nameDescended, YN userIdDescended, Long projectId, BfPage bfPage) throws Exception {
        List<Map<String, Object>> list = repos.findAllUnionChecklistAndRisk(title, type, createdAtDescended, nameDescended, userIdDescended, projectId,
            bfPage);
        long count = repos.countUnionChecklistAndRisk(title, type, projectId);

        return new BfListResponse(list, count, bfPage);
    }

    @Override
    public BfListResponse findAllUnionChecklistTemplateAndRiskTemplate(String title, String type,
        YN createdAtDescended, YN nameDescended, YN userIdDescended, Long projectId, BfPage bfPage) throws Exception {
        List<Map<String, Object>> list = repos.findAllUnionChecklistTemplateAndRiskTemplate(title, type, createdAtDescended, nameDescended, 
            userIdDescended, projectId, bfPage);
        long count = repos.countAllUnionChecklistTemplateAndRiskTemplate(title, type, projectId);
        
        return new BfListResponse(list, count, bfPage);
    }
}
