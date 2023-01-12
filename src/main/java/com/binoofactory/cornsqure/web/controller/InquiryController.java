package com.binoofactory.cornsqure.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.binoofactory.cornsqure.utils.ResponseUtil;
import com.binoofactory.cornsqure.web.model.cmmn.BfListResponse;
import com.binoofactory.cornsqure.web.model.cmmn.BfPage;
import com.binoofactory.cornsqure.web.model.entity.Inquiry;
import com.binoofactory.cornsqure.web.service.InquiryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/board")
@Api(tags = {"Inquiry"}, description = "문의", basePath = "/board")
public class InquiryController {

    private final InquiryService inquiryService;

    @Autowired
    public InquiryController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    @PostMapping(value = "/inquiriy")
    @ApiOperation(value = "등록", notes = "등록")
    public ResponseEntity add(
        @RequestBody Inquiry params,
        HttpServletRequest request) throws Exception {
        return ResponseUtil.sendResponse(inquiryService.add(params, request));
    }

    @PutMapping(value = "/inquiries/{id}")
    @ApiOperation(value = "수정", notes = "수정")
    public ResponseEntity modify(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        @RequestBody Inquiry params,
        HttpServletRequest request) throws Exception {

        params.setId(id);
        return ResponseUtil.sendResponse(inquiryService.edit(params, request));
    }

    @DeleteMapping(value = "/inquiries/{id}")
    @ApiOperation(value = "삭제", notes = "삭제")
    public ResponseEntity remove(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        inquiryService.remove(id, request);
        return ResponseUtil.sendResponse(null);
    }

    @GetMapping(value = "/inquiries/{id}")
    @ApiOperation(value = "조회 (단건)", notes = "조회 (단건)")
    public ResponseEntity find(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        return ResponseUtil.sendResponse(inquiryService.find(id, request));
    }

    @GetMapping(value = "/inquiries")
    @ApiOperation(value = "목록 조회 (다건)", notes = "목록 조회 (다건)")
    public ResponseEntity<BfListResponse> findAll(
        BfPage bfPage,
        HttpServletRequest request) throws Exception {
        return ResponseUtil.sendResponse(inquiryService.findAll(
            Inquiry.builder()
                .build(),
            bfPage,
            request));
    }
}
