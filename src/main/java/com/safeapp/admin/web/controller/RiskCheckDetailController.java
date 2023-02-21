package com.safeapp.admin.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.dto.request.RequestRiskCheckDetailDTO;
import com.safeapp.admin.web.dto.response.ResponseRiskCheckDetailDTO;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.RiskCheckDetail;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.safeapp.admin.web.service.RiskCheckDetailService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/riskCheck/detail")
@AllArgsConstructor
@Api(tags = {"RiskCheckDetail"}, description = "리스트 관리 > 위험성 평가표 > 위험성 평가표 상세")
public class RiskCheckDetailController {

    private final RiskCheckDetailService riskCheckDetailService;

    @PostMapping(value = "/add")
    @ApiOperation(value = "위험성 평가표 상세 등록", notes = "위험성 평가표 상세 등록")
    public ResponseEntity<ResponseRiskCheckDetailDTO> add(@RequestBody RequestRiskCheckDetailDTO addDto,
            HttpServletRequest request) throws Exception {

        RiskCheckDetail riskChkDet = riskCheckDetailService.toEntity(addDto);

        RiskCheckDetail addedRiskChkDet = riskCheckDetailService.add(riskChkDet, request);
        return new ResponseEntity<>(ResponseRiskCheckDetailDTO.builder().detail(addedRiskChkDet).build(), OK);
    }

    @GetMapping(value = "/find/{id}")
    @ApiOperation(value = "위험성 평가표 상세 단독 조회", notes = "위험성 평가표 상세 단독 조회")
    public RiskCheckDetail find(@PathVariable("id") @ApiParam(value = "위험성 평가표 상세 PK", required = true) long id,
            HttpServletRequest request) throws Exception {

        return riskCheckDetailService.find(id, request);
    }

    @PutMapping(value = "/edit/{id}")
    @ApiOperation(value = "위험성 평가표 상세 수정", notes = "위험성 평가표 상세 수정")
    public ResponseEntity<ResponseRiskCheckDetailDTO> edit(@PathVariable("id") @ApiParam(value = "위험성 평가표 상세 PK", required = true) long id,
            @RequestBody RequestRiskCheckDetailDTO editDto, HttpServletRequest request) throws Exception {

        RiskCheckDetail riskChkDet = riskCheckDetailService.toEntity(editDto);
        riskChkDet.setId(id);

        RiskCheckDetail editedRiskChkDet = riskCheckDetailService.edit(riskChkDet, request);
        return new ResponseEntity<>(ResponseRiskCheckDetailDTO.builder().detail(editedRiskChkDet).build(), OK);
    }

    @DeleteMapping(value = "/remove/{id}")
    @ApiOperation(value = "위험성 평가표 상세 삭제", notes = "위험성 평가표 상세 삭제")
    public void remove(@PathVariable("id") @ApiParam(value = "위험성 평가표 상세 PK", required = true) long id,
            HttpServletRequest request) throws Exception {

        riskCheckDetailService.remove(id, request);
    }

    /*
    @GetMapping(value = "/list")
    @ApiOperation(value = "위험성 평가표 목록 조회", notes = "위험성 평가표 목록 조회")
    public ListResponse findAll(Pages pages, HttpServletRequest request) throws Exception {

        return riskCheckDetailService.findAll(RiskCheckDetail.builder().build(), pages,request);
    }
    */

}