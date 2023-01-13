package com.safeapp.admin.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.utils.ResponseUtil;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.cmmn.BfListResponse;
import com.safeapp.admin.web.model.cmmn.BfPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safeapp.admin.web.service.ChecklistAndRiskService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/union/check-risks")
@Api(tags = {"체크리스트와 위험성평가표"}, description = "체크리스트와 위험성평가표", basePath = "/union/check-risks")
public class ChecklistAndRiskController {

    private final ChecklistAndRiskService service;

    @Autowired
    public ChecklistAndRiskController(ChecklistAndRiskService service) {
        this.service = service;
    }

    @GetMapping(value = "")
    @ApiOperation(value = "목록 조회 (다건)", notes = "목록 조회 (다건)")
    public ResponseEntity<BfListResponse> findAllUnionChecklistAndRisk(
        BfPage bfPage,
        @RequestParam(value = "title", required = false) String title,
        @RequestParam(value = "type", required = false) String type,
        @RequestParam(value = "project_id", required = false) Long project_id,
        @RequestParam(value = "created_at_descended", required = false) YN created_at_descended,
        @RequestParam(value = "name_descended", required = false) YN name_descended,
        @RequestParam(value = "user_id_descended", required = false) YN user_id_descended,
        HttpServletRequest request) throws Exception {
        return ResponseUtil.sendResponse(service.findAllUnionChecklistAndRisk(title, type, created_at_descended,
            name_descended, user_id_descended, project_id, bfPage));
    }

    @GetMapping(value = "/template")
    @ApiOperation(value = "템플릿 목록 조회 (다건)", notes = "템플릿 목록 조회 (다건)")
    public ResponseEntity<BfListResponse> findAllUnionChecklistTemplateAndRiskTemplate(
        BfPage bfPage,
        @RequestParam(value = "title", required = false) String title,
        @RequestParam(value = "type", required = false) String type,
        @RequestParam(value = "project_id", required = false) Long project_id,
        @RequestParam(value = "created_at_descended", required = false) YN created_at_descended,
        @RequestParam(value = "name_descended", required = false) YN name_descended,
        @RequestParam(value = "user_id_descended", required = false) YN user_id_descended,
        HttpServletRequest request) throws Exception {
        return ResponseUtil.sendResponse(service.findAllUnionChecklistTemplateAndRiskTemplate(title, type,
            created_at_descended, name_descended, user_id_descended, project_id, bfPage));
    }
}
