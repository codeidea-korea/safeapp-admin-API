package com.safeapp.admin.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.utils.ResponseUtil;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safeapp.admin.web.service.CheckListAndRiskService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/union")
@AllArgsConstructor
@Api(tags = {"CheckList & RiskCheck"}, description = "체크리스트 & 위험성평가표")
public class CheckListAndRiskController {

    private final CheckListAndRiskService chkAndRiskService;

    @GetMapping(value = "/list")
    @ApiOperation(value = "체크리스트 & 위험성평가표 목록 조회", notes = "체크리스트 & 위험성평가표 목록 조회")
    public ResponseEntity<ListResponse> findAllUnionCheckListAndRisk(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "project_id", required = false) Long project_id,
            @RequestParam(value = "created_at_descended", required = false) YN created_at_descended,
            @RequestParam(value = "name_descended", required = false) YN name_descended,
            @RequestParam(value = "user_id_descended", required = false) YN user_id_descended,
            Pages pages, HttpServletRequest request) throws Exception {

        return
            ResponseUtil.sendResponse(chkAndRiskService.findAllUnionCheckListAndRisk(title, type, created_at_descended, name_descended, user_id_descended, project_id, pages, request));
    }

    @GetMapping(value = "/template/list")
    @ApiOperation(value = "체크리스트 & 위험성평가표 템플릿 목록 조회", notes = "체크리스트 & 위험성평가표 템플릿 목록 조회")
    public ResponseEntity<ListResponse> findAllUnionCheckListTemplateAndRiskTemplate(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "project_id", required = false) Long project_id,
            @RequestParam(value = "created_at_descended", required = false) YN created_at_descended,
            @RequestParam(value = "name_descended", required = false) YN name_descended,
            @RequestParam(value = "user_id_descended", required = false) YN user_id_descended,
            Pages pages, HttpServletRequest request) throws Exception {

        return
            ResponseUtil.sendResponse(chkAndRiskService.findAllUnionCheckListTemplateAndRiskTemplate(title, type, created_at_descended, name_descended, user_id_descended, project_id, pages, request));
    }
}
