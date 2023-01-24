package com.safeapp.admin.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.dto.request.RequestCheckListTemplateDetailDTO;
import com.safeapp.admin.web.dto.response.ResponseCheckListTemplateDetailDTO;
import com.safeapp.admin.web.service.CheckListTemplateService;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.CheckListTemplateDetail;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.safeapp.admin.web.service.CheckListTemplateDetailService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import static org.springframework.http.HttpStatus.OK;

//@RestController
@RequestMapping("/api/checklistTemplateDetail")
@AllArgsConstructor
@Api(tags = {"CheckListTemplateDetail"}, description = "체크리스트 템플릿 상세", basePath = "/api/checklistTemplateDetail")
public class CheckListTemplateDetailController {

    private final CheckListTemplateDetailService checklistTemplateDetailService;
    private final CheckListTemplateService checklistTemplateService;


    @PostMapping(value = "/{id}")
    @ApiOperation(value = "등록", notes = "등록")
    public ResponseEntity<ResponseCheckListTemplateDetailDTO> add(
        @RequestBody RequestCheckListTemplateDetailDTO dto,
        @PathVariable("id") @ApiParam(value = "체크리스트 템플릿 본문 ID", readOnly = true)Long id,
        HttpServletRequest request) throws Exception {
        CheckListTemplateDetail params = checklistTemplateDetailService.toEntity(dto);
        params.setCheckListTemplate(checklistTemplateService.find(id,request));
        CheckListTemplateDetail result = checklistTemplateDetailService.add(params, request);
        return new ResponseEntity<>(ResponseCheckListTemplateDetailDTO
                .builder()
                .detail(result)
                .build(),OK);
    }

    @PutMapping(value = "/{id}")
    @ApiOperation(value = "수정", notes = "수정")
    public CheckListTemplateDetail modify(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        @RequestBody CheckListTemplateDetail params,
        HttpServletRequest request) throws Exception {

        params.setId(id);
        return checklistTemplateDetailService.edit(params, request);
    }

    @DeleteMapping(value = "")
    @ApiOperation(value = "삭제", notes = "삭제")
    public void remove(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        checklistTemplateDetailService.remove(id, request);
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "조회 (단건)", notes = "조회 (단건)")
    public CheckListTemplateDetail find(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        return checklistTemplateDetailService.find(id, request);
    }

    @GetMapping(value = "")
    @ApiOperation(value = "목록 조회 (다건)", notes = "목록 조회 (다건)")
    public ListResponse findAll(
        Pages bfPage,
        HttpServletRequest request) throws Exception {
        return checklistTemplateDetailService.findAll(
            CheckListTemplateDetail.builder()
                .build(),
            bfPage,
            request);
    }
}
