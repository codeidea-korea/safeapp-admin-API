package com.safeapp.admin.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.dto.request.RequestChecklistProjectResultDTO;
import com.safeapp.admin.web.dto.response.ResponseChecklistProjectResultDTO;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.BfPage;
import com.safeapp.admin.web.model.entity.ChecklistProjectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.safeapp.admin.web.service.ChecklistProjectResultService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/checklistProjectResult")
@Api(tags = {"ChecklistProjectResult"}, description = "체크리스트 내용별 결과", basePath = "/api/checklistProjectResult")
public class ChecklistProjectResultController {

    private final ChecklistProjectResultService checklistProjectResultService;

    @Autowired
    public ChecklistProjectResultController(ChecklistProjectResultService checklistProjectResultService) {
        this.checklistProjectResultService = checklistProjectResultService;
    }

    /*@PostMapping(value = "")
    @ApiOperation(value = "등록", notes = "등록")
    public ChecklistProjectResult add(
        @RequestBody ChecklistProjectResult params,
        HttpServletRequest request) throws Exception {
        System.out.println(1);
        return checklistProjectResultService.add(params, request);
    }*/

    @PostMapping(value = "")
    @ApiOperation(value = "등록", notes = "등록")
    public ResponseEntity add(
            @RequestBody RequestChecklistProjectResultDTO dto,
            HttpServletRequest request) throws Exception {
        System.out.println(1);

        ChecklistProjectResult params = checklistProjectResultService.toEntity(dto);
        ChecklistProjectResult checklistProjectResult = checklistProjectResultService.add(params, request);
        ResponseChecklistProjectResultDTO res = ResponseChecklistProjectResultDTO.builder().checklistProjectResult(checklistProjectResult).build();
        return ResponseEntity.ok().body(res);

    }

    /*@PutMapping(value = "/{id}")
    @ApiOperation(value = "수정", notes = "수정")
    public ChecklistProjectResult modify(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        @RequestBody ChecklistProjectResult params,
        HttpServletRequest request) throws Exception {

        params.setId(id);
        return checklistProjectResultService.edit(params, request);
    }*/

    @PutMapping(value = "/{id}")
    @ApiOperation(value = "수정", notes = "수정")
    public ResponseEntity modify(
            @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
            @RequestBody RequestChecklistProjectResultDTO dto,
            HttpServletRequest request) throws Exception {

        ChecklistProjectResult params = checklistProjectResultService.toEntity(dto);
        params.setId(id);
        ChecklistProjectResult checklistProjectResult = checklistProjectResultService.edit(params, request);
        ResponseChecklistProjectResultDTO res = ResponseChecklistProjectResultDTO.builder().checklistProjectResult(checklistProjectResult).build();
        return ResponseEntity.ok().body(res);
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "삭제", notes = "삭제")
    public ResponseEntity remove(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        checklistProjectResultService.remove(id, request);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "조회 (단건)", notes = "조회 (단건)")
    public ResponseEntity find(
        @PathVariable("id") @ApiParam(value = "checklist_project_result id", required = true) long id,
        HttpServletRequest request) throws Exception {

        ChecklistProjectResult checklistProjectResult = checklistProjectResultService.find(id, request);

        ResponseChecklistProjectResultDTO res = ResponseChecklistProjectResultDTO.builder().checklistProjectResult(checklistProjectResult).build();

        return ResponseEntity.ok().body(res);
        //return checklistProjectResultService.find(id, request);
    }

    @GetMapping(value = "")
    @ApiOperation(value = "목록 조회 (다건) 필요한지 확인 후 수정)", notes = "목록 조회 (다건)")
    public ListResponse findAll(
        BfPage bfPage,
        HttpServletRequest request) throws Exception {
        return checklistProjectResultService.findAll(
            ChecklistProjectResult.builder()
                .build(),
            bfPage,
            request);
    }

}
