package com.safeapp.admin.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.ChecklistProjectResultImg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.safeapp.admin.web.service.ChecklistProjectResultImgService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/checklistProjectResultImg")
@Api(tags = {"ChecklistProjectResultImg"}, description = "체크리스트 내용별 결과 이미지", basePath = "/api/checklistProjectResultImg")
public class ChecklistProjectResultImgController {

    private final ChecklistProjectResultImgService checklistProjectResultImgService;

    @Autowired
    public ChecklistProjectResultImgController(ChecklistProjectResultImgService checklistProjectResultImgService) {
        this.checklistProjectResultImgService = checklistProjectResultImgService;
    }

    @PostMapping(value = "")
    @ApiOperation(value = "등록", notes = "등록")
    public ChecklistProjectResultImg add(
        @RequestBody ChecklistProjectResultImg params,
        HttpServletRequest request) throws Exception {
        return checklistProjectResultImgService.add(params, request);
    }

    @PutMapping(value = "/{id}")
    @ApiOperation(value = "수정", notes = "수정")
    public ChecklistProjectResultImg modify(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        @RequestBody ChecklistProjectResultImg params,
        HttpServletRequest request) throws Exception {

        params.setId(id);
        return checklistProjectResultImgService.edit(params, request);
    }

    @DeleteMapping(value = "")
    @ApiOperation(value = "삭제", notes = "삭제")
    public void remove(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        checklistProjectResultImgService.remove(id, request);
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "조회 (단건)", notes = "조회 (단건)")
    public ChecklistProjectResultImg find(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        return checklistProjectResultImgService.find(id, request);
    }

    @GetMapping(value = "")
    @ApiOperation(value = "목록 조회 (다건)", notes = "목록 조회 (다건)")
    public ListResponse findAll(
        Pages bfPage,
        HttpServletRequest request) throws Exception {
        return checklistProjectResultImgService.findAll(
            ChecklistProjectResultImg.builder()
                .build(),
            bfPage,
            request);
    }
}
