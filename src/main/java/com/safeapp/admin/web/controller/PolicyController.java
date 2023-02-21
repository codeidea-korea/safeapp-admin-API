package com.safeapp.admin.web.controller;

import com.safeapp.admin.utils.ResponseUtil;
import com.safeapp.admin.web.data.NoticeType;
import com.safeapp.admin.web.dto.request.RequestNoticeDTO;
import com.safeapp.admin.web.dto.request.RequestNoticeEditDTO;
import com.safeapp.admin.web.dto.request.RequestPolicyEditDTO;
import com.safeapp.admin.web.dto.response.ResponseNoticeDTO;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.Notice;
import com.safeapp.admin.web.model.entity.Policy;
import com.safeapp.admin.web.service.NoticeService;
import com.safeapp.admin.web.service.PolicyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/policy")
@AllArgsConstructor
@Api(tags = {"Policy"}, description = "컨텐츠 관리 > 정책 관리")
public class PolicyController {

    private final PolicyService policyService;

    @GetMapping(value = "/find/{id}")
    @ApiOperation(value = "정책 단독 조회", notes = "정책 단독 조회")
    public ResponseEntity find(@PathVariable("id") @ApiParam(value = "정책 PK", required = true) long id,
            HttpServletRequest request) throws Exception {

        return ResponseUtil.sendResponse(policyService.find(id, request));
    }

    @PutMapping(value = "/edit/{id}")
    @ApiOperation(value = "정책 수정", notes = "정책 수정")
    public ResponseEntity<ResponseNoticeDTO> edit(@PathVariable("id") @ApiParam(value = "공지사항 PK", required = true) long id,
            @RequestBody RequestPolicyEditDTO editDto, HttpServletRequest request) throws Exception {

        Policy newPolicy = policyService.toEditEntity(editDto);
        newPolicy.setId(id);

        return ResponseUtil.sendResponse(policyService.edit(newPolicy, request));
    }

    @GetMapping(value = "/list")
    @ApiOperation(value = "정책 목록 조회", notes = "정책 목록 조회")
    public ResponseEntity<List<Policy>> findAll(HttpServletRequest request) throws Exception {
        Pages pages = new Pages(1, 100);
        return
            ResponseUtil.sendResponse(policyService.findAll(
                Policy.builder()
                .build(), pages, request)
            );
    }

}