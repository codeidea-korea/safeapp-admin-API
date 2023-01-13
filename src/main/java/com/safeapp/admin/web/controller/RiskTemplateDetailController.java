package com.safeapp.admin.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.dto.request.RequestRiskTemplateDetailDTO;
import com.safeapp.admin.web.dto.response.ResponseRiskTemplateDetailDTO;
import com.safeapp.admin.web.model.cmmn.BfListResponse;
import com.safeapp.admin.web.model.cmmn.BfPage;
import com.safeapp.admin.web.model.entity.RiskTemplateDetail;
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

import com.safeapp.admin.web.service.RiskTemplateDetailService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/riskTemplateDetail")
@Api(tags = {"RiskTemplateDetail"}, description = "위험체크 템플릿 상세", basePath = "/api/riskTemplateDetail")
public class RiskTemplateDetailController {

    private final RiskTemplateDetailService riskTemplateDetailService;

    @Autowired
    public RiskTemplateDetailController(RiskTemplateDetailService riskTemplateDetailService) {
        this.riskTemplateDetailService = riskTemplateDetailService;
    }

    @PostMapping(value = "")
    @ApiOperation(value = "등록", notes = "등록")
    public ResponseEntity<ResponseRiskTemplateDetailDTO> add(
        @RequestBody RequestRiskTemplateDetailDTO dto,
        HttpServletRequest request) throws Exception {
        RiskTemplateDetail params = riskTemplateDetailService.toEntity(dto);
        RiskTemplateDetail result = riskTemplateDetailService.add(params, request);
        return new ResponseEntity<>(
                ResponseRiskTemplateDetailDTO
                        .builder()
                        .detail(result)
                        .build(), OK);
    }

    @PutMapping(value = "/{id}")
    @ApiOperation(value = "수정", notes = "수정")
    public RiskTemplateDetail modify(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        @RequestBody RiskTemplateDetail params,
        HttpServletRequest request) throws Exception {

        params.setId(id);
        return riskTemplateDetailService.edit(params, request);
    }

    @DeleteMapping(value = "")
    @ApiOperation(value = "삭제", notes = "삭제")
    public void remove(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        riskTemplateDetailService.remove(id, request);
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "조회 (단건)", notes = "조회 (단건)")
    public RiskTemplateDetail find(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        return riskTemplateDetailService.find(id, request);
    }

    @GetMapping(value = "")
    @ApiOperation(value = "목록 조회 (다건)", notes = "목록 조회 (다건)")
    public BfListResponse findAll(
        BfPage bfPage,
        HttpServletRequest request) throws Exception {
        return riskTemplateDetailService.findAll(
            RiskTemplateDetail.builder()
                .build(),
            bfPage,
            request);
    }
}
