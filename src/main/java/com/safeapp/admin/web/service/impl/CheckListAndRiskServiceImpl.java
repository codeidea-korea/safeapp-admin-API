package com.safeapp.admin.web.service.impl;

import java.util.List;
import java.util.Map;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.service.CheckListAndRiskService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safeapp.admin.web.repos.direct.DirectQuery;
import com.safeapp.admin.web.service.cmmn.JwtService;

import javax.servlet.http.HttpServletRequest;

@Service
@AllArgsConstructor
public class CheckListAndRiskServiceImpl implements CheckListAndRiskService {

    private final DirectQuery dirRepos;

    @Override
    public ListResponse findAllUnionCheckListAndRisk(String title, String type, YN createdAtDescended, YN nameDescended,
            YN userIdDescended, Long projectId, Pages pages, HttpServletRequest request) {

        long count = dirRepos.countUnionCheckListAndRisk(title, type, projectId);
        List<Map<String, Object>> list =
            dirRepos.findAllUnionCheckListAndRisk(title, type, createdAtDescended, nameDescended, userIdDescended, projectId, pages);

        return new ListResponse(count, list, pages);
    }

    @Override
    public ListResponse findAllUnionCheckListTemplateAndRiskTemplate(String title, String type, YN createdAtDescended,
            YN nameDescended, YN userIdDescended, Long projectId, Pages pages, HttpServletRequest request) {

        long count = dirRepos.countAllUnionCheckListTemplateAndRiskTemplate(title, type, projectId);
        List<Map<String, Object>> list =
            dirRepos.findAllUnionCheckListTemplateAndRiskTemplate(title, type, createdAtDescended, nameDescended, userIdDescended, projectId, pages);
        
        return new ListResponse(count, list, pages);
    }

}