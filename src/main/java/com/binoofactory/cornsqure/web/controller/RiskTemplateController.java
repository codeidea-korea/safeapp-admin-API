package com.binoofactory.cornsqure.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.binoofactory.cornsqure.web.dto.request.RequestRiskTemplateDTO;
import com.binoofactory.cornsqure.web.dto.response.ResponseRiskTemplateDTO;
import com.binoofactory.cornsqure.web.dto.response.ResponseRiskTemplateSelectDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.binoofactory.cornsqure.utils.ResponseUtil;
import com.binoofactory.cornsqure.web.data.YN;
import com.binoofactory.cornsqure.web.model.cmmn.BfListResponse;
import com.binoofactory.cornsqure.web.model.cmmn.BfPage;
import com.binoofactory.cornsqure.web.model.entity.RiskTemplate;
import com.binoofactory.cornsqure.web.service.RiskTemplateService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.data.domain.Pageable;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/template/risk-checklists")
@Api(tags = {"RiskTemplate"}, description = "위험체크 템플릿", basePath = "/template/risk-checklists")
public class RiskTemplateController {

    private final RiskTemplateService riskTemplateService;

    @Autowired
    public RiskTemplateController(RiskTemplateService riskTemplateService) {
        this.riskTemplateService = riskTemplateService;
    }

    @PostMapping(value = "")
    @ApiOperation(value = "등록", notes = "등록")
    public ResponseEntity<ResponseRiskTemplateDTO> add(
            @RequestBody RequestRiskTemplateDTO dto,
            HttpServletRequest request) throws Exception {
        RiskTemplate result = riskTemplateService.add(riskTemplateService.toEntity(dto), request);
        return new ResponseEntity<>(
                ResponseRiskTemplateDTO
                        .builder()
                        .template(result)
                        .build(), OK);
    }

    @PutMapping(value = "/{id}")
    @ApiOperation(value = "수정", notes = "수정")
    public ResponseEntity modify(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        @RequestBody RiskTemplate params,
        HttpServletRequest request) throws Exception {

        params.setId(id);
        return ResponseUtil.sendResponse(riskTemplateService.edit(params, request));
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "삭제", notes = "삭제")
    public ResponseEntity remove(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        riskTemplateService.remove(id, request);
        return ResponseUtil.sendResponse(null);
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "조회 (단건)", notes = "조회 (단건)")
    public ResponseEntity<ResponseRiskTemplateSelectDTO> find(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        RiskTemplate result = riskTemplateService.find(id, request);
        return new ResponseEntity<>(
                ResponseRiskTemplateSelectDTO
                        .builder()
                        .template(result)
                        .build(), OK);
    }

    @GetMapping(value = "")
    @ApiOperation(value = "목록 조회 (다건)", notes = "목록 조회 (다건)")
    public ResponseEntity<List<ResponseRiskTemplateDTO>> findAll(
        @RequestParam(value = "checkerId", required = false) Long checkerId,
        @RequestParam(value = "projectId", required = false) Long projectId,
        @RequestParam(value = "userId", required = false) Long userId,
        @RequestParam(value = "name", required = false) String name,
        @RequestParam(value = "visibled", required = false) YN visibled,
        @RequestParam(value = "tags", required = false) String tags,
        Pageable page,
        HttpServletRequest request) throws Exception {
        return new ResponseEntity<>(
                riskTemplateService.findAllByCondition(
                        checkerId, projectId, userId, name, visibled, tags, page
                ), OK);
    }

    @PatchMapping(value = "/{id}/like")
    @ApiOperation(value = "좋아요", notes = "좋아요")
    public ResponseEntity like(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        
        riskTemplateService.addLike(id, request);
        return ResponseUtil.sendResponse(true);
    }

    @PatchMapping(value = "/{id}/dislike")
    @ApiOperation(value = "좋아요 해제", notes = "좋아요 해제")
    public ResponseEntity dislike(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        
        riskTemplateService.removeLike(id, request);
        return ResponseUtil.sendResponse(true);
    }

    @GetMapping(value = "/{id}/liked")
    @ApiOperation(value = "나의 좋아요 여부", notes = "나의 좋아요 여부")
    public ResponseEntity liked(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        
        return ResponseUtil.sendResponse(riskTemplateService.isLiked(id, request));
    }
}
