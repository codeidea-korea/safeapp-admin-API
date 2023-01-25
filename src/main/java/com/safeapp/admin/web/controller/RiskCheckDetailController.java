package com.safeapp.admin.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.dto.request.RequestRiskcheckDetailDTO;
import com.safeapp.admin.web.dto.response.ResponseRiskCheckDetailDTO;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.RiskCheckDetail;
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

import com.safeapp.admin.web.service.RiskCheckDetailService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import static org.springframework.http.HttpStatus.OK;

//@RestController
@RequestMapping("/api/riskCheckDetail")
@Api(tags = {"RiskCheckDetail"}, description = "위험체크 상세", basePath = "/api/riskCheckDetail")
public class RiskCheckDetailController {

    private final RiskCheckDetailService riskCheckDetailService;

    @Autowired
    public RiskCheckDetailController(RiskCheckDetailService riskCheckDetailService) {
        this.riskCheckDetailService = riskCheckDetailService;
    }

    @PostMapping(value = "")
    @ApiOperation(value = "등록", notes = "등록")
    public ResponseEntity<ResponseRiskCheckDetailDTO> add(
        @RequestBody RequestRiskcheckDetailDTO dto,
        HttpServletRequest request) throws Exception {
        RiskCheckDetail params = riskCheckDetailService.toEntity(dto);
        RiskCheckDetail result = riskCheckDetailService.add(params, request);
        return new ResponseEntity<>(ResponseRiskCheckDetailDTO
                .builder()
                .detail(result)
                .build(), OK);
    }

    @PutMapping(value = "/{id}")
    @ApiOperation(value = "수정", notes = "수정")
    public ResponseEntity<ResponseRiskCheckDetailDTO> modify(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        @RequestBody RequestRiskcheckDetailDTO dto,
        HttpServletRequest request) throws Exception {
        RiskCheckDetail params = riskCheckDetailService.toEntity(dto);
        params.setId(id);
        RiskCheckDetail result = riskCheckDetailService.edit(params, request);
        return new ResponseEntity<>(ResponseRiskCheckDetailDTO
                .builder()
                .detail(result)
                .build(), OK);
    }

    @DeleteMapping(value = "")
    @ApiOperation(value = "삭제", notes = "삭제")
    public void remove(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        riskCheckDetailService.remove(id, request);
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "조회 (단건)", notes = "조회 (단건)")
    public RiskCheckDetail find(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        return riskCheckDetailService.find(id, request);
    }

    @GetMapping(value = "")
    @ApiOperation(value = "목록 조회 (다건)", notes = "목록 조회 (다건)")
    public ListResponse findAll(
        Pages bfPage,
        HttpServletRequest request) throws Exception {
        return riskCheckDetailService.findAll(
            RiskCheckDetail.builder()
                .build(),
            bfPage,
            request);
    }
}
