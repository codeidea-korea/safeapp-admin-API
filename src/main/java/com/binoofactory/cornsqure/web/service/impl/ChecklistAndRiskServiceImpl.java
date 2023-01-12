package com.binoofactory.cornsqure.web.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.binoofactory.cornsqure.utils.DateUtil;
import com.binoofactory.cornsqure.utils.PasswordUtil;
import com.binoofactory.cornsqure.web.data.YN;
import com.binoofactory.cornsqure.web.model.cmmn.BfListResponse;
import com.binoofactory.cornsqure.web.model.cmmn.BfPage;
import com.binoofactory.cornsqure.web.repos.direct.DirectQuery;
import com.binoofactory.cornsqure.web.service.ChecklistAndRiskService;
import com.binoofactory.cornsqure.web.service.cmmn.UserJwtService;

@Service
public class ChecklistAndRiskServiceImpl implements ChecklistAndRiskService {

    private final DirectQuery repos;

    private final UserJwtService userJwtService;

    private final PasswordUtil passwordUtil;

    private final DateUtil dateUtil;

    @Autowired
    public ChecklistAndRiskServiceImpl(DirectQuery repos,
        UserJwtService userJwtService, PasswordUtil passwordUtil, DateUtil dateUtil) {

        this.repos = repos;
        this.userJwtService = userJwtService;
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
