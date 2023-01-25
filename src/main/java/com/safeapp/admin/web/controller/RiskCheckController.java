package com.safeapp.admin.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.data.StatusType;
import com.safeapp.admin.web.dto.request.RequestRiskCheckDTO;
import com.safeapp.admin.web.dto.response.ResponseRiskcheckDTO;
import com.safeapp.admin.web.dto.response.ResponseRiskcheckSelectDTO;
import com.safeapp.admin.utils.ResponseUtil;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.entity.RiskCheck;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
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

import com.safeapp.admin.web.service.RiskCheckService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

//@RestController
@RequestMapping("/check")
@AllArgsConstructor
@Api(tags = {"RiskCheck"}, description = "위험체크", basePath = "/check")
public class RiskCheckController {

    private final RiskCheckService riskCheckService;

    @PostMapping(value = "/risk-checklist")
    @ApiOperation(value = "등록", notes = "등록")
    public ResponseEntity<ResponseRiskcheckDTO> add(
            @RequestBody RequestRiskCheckDTO dto,
            HttpServletRequest request) throws Exception {
        RiskCheck result = riskCheckService.add(riskCheckService.toEntity(dto), request);
        return new ResponseEntity<>(
                ResponseRiskcheckDTO
                        .builder()
                        .riskCheck(result)
                        .build(), OK);
    }

    @PutMapping(value = "/risk-checklists/{id}")
    @ApiOperation(value = "수정", notes = "수정")
    public ResponseEntity<ResponseRiskcheckDTO> modify(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        @RequestBody RequestRiskCheckDTO dto,
        HttpServletRequest request) throws Exception {
        RiskCheck params = riskCheckService.toEntity(dto);
        params.setId(id);
        RiskCheck result = riskCheckService.edit(params, request);
        return new ResponseEntity<>(
                ResponseRiskcheckDTO
                        .builder()
                        .riskCheck(result)
                        .build(), OK);
    }

    @DeleteMapping(value = "/risk-checklists/{id}")
    @ApiOperation(value = "삭제", notes = "삭제")
    public ResponseEntity remove(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        riskCheckService.remove(id, request);
        return ResponseUtil.sendResponse(null);
    }

    @GetMapping(value = "/risk-checklists/{id}")
    @ApiOperation(value = "조회 (단건)", notes = "조회 (단건)")
    public ResponseEntity<ResponseRiskcheckSelectDTO> find(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        RiskCheck result = riskCheckService.find(id, request);
        return new ResponseEntity<>(
                ResponseRiskcheckSelectDTO
                        .builder()
                        .riskCheck(result)
                        .build(), OK);
    }

    @GetMapping(value = "/risk-checklists")
    @ApiOperation(value = "목록 조회 (다건)", notes = "목록 조회 (다건)")
    public ResponseEntity<List<ResponseRiskcheckDTO>> findAll(
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "projectId", required = false) Long projectId,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "visibled", required = false, defaultValue = "Y") YN visibled,
            @RequestParam(value = "status", required = false, defaultValue = "READY") String status,
            @RequestParam(value = "created_at_descended", required = false) YN created_at_descended,
            @RequestParam(value = "views_descended", required = false) YN views_descended,
            @RequestParam(value = "likes_descended", required = false) YN likes_descended,
            @RequestParam(value = "detail_contents", required = false) String detail_contents,
            Pageable page,
            HttpServletRequest request) throws Exception {

        return new ResponseEntity<>(riskCheckService.findAllByCondition(
                userId, projectId, name, tag, visibled, status, created_at_descended, views_descended, likes_descended, detail_contents, page, request),OK);
    }


    @PatchMapping(value = "/risk-checklists/{id}/status-update")
    @ApiOperation(value = "점검/검토/승인 업데이트", notes = "점검/검토/승인 업데이트")
    public ResponseEntity statusUpdate(
            @PathVariable("id") @ApiParam(value = "위험성평가ID", required = true) long id,
            @RequestParam(value = "type") @Parameter(description = "타입") StatusType type,
            HttpServletRequest request) throws Exception {
        riskCheckService.updateStatus(id,type);
        return ResponseUtil.sendResponse(true);
    }

    @PatchMapping(value = "/risk-checklists/{id}/like")
    @ApiOperation(value = "좋아요", notes = "좋아요")
    public ResponseEntity like(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        
        riskCheckService.addLike(id, request);
        return ResponseUtil.sendResponse(true);
    }

    @PatchMapping(value = "/risk-checklists/{id}/dislike")
    @ApiOperation(value = "좋아요 해제", notes = "좋아요 해제")
    public ResponseEntity dislike(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        
        riskCheckService.removeLike(id, request);
        return ResponseUtil.sendResponse(true);
    }

    @GetMapping(value = "/risk-checklists/{id}/liked")
    @ApiOperation(value = "나의 좋아요 여부", notes = "나의 좋아요 여부")
    public ResponseEntity liked(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        
        return ResponseUtil.sendResponse(riskCheckService.isLiked(id, request));
    }
}
