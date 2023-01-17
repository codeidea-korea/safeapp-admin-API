package com.safeapp.admin.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.utils.ResponseUtil;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.ConcernAccidentExp;
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

import com.safeapp.admin.web.service.ConcernAccidentExpService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/board/concern-accidents")
@Api(tags = {"ConcernAccidentExp"}, description = "아차 사고", basePath = "/board/concern-accidents")
public class ConcernAccidentExpController {

    private final ConcernAccidentExpService concernAccidentExpService;

    @Autowired
    public ConcernAccidentExpController(ConcernAccidentExpService concernAccidentExpService) {
        this.concernAccidentExpService = concernAccidentExpService;
    }

    @PostMapping(value = "")
    @ApiOperation(value = "등록", notes = "등록")
    public ResponseEntity add(
        @RequestBody ConcernAccidentExp params,
        HttpServletRequest request) throws Exception {
        return ResponseUtil.sendResponse(concernAccidentExpService.add(params, request));
    }

    @PutMapping(value = "/{id}")
    @ApiOperation(value = "수정", notes = "수정")
    public ResponseEntity modify(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        @RequestBody ConcernAccidentExp params,
        HttpServletRequest request) throws Exception {

        params.setId(id);
        return ResponseUtil.sendResponse(concernAccidentExpService.edit(params, request));
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "삭제", notes = "삭제")
    public ResponseEntity remove(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        concernAccidentExpService.remove(id, request);
        return ResponseUtil.sendResponse(null);
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "조회 (단건)", notes = "조회 (단건)")
    public ResponseEntity find(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        return ResponseUtil.sendResponse(concernAccidentExpService.find(id, request));
    }

    @GetMapping(value = "")
    @ApiOperation(value = "목록 조회 (다건)", notes = "목록 조회 (다건)")
    public ResponseEntity<ListResponse> findAll(
        Pages bfPage,
        @RequestParam(value = "name", required = false) String name,
        @RequestParam(value = "title", required = false) String title,
        @RequestParam(value = "tags", required = false) String tags,
        @RequestParam(value = "created_at_descended", required = false) YN created_at_descended,
        @RequestParam(value = "views_descended", required = false) YN views_descended,
        @RequestParam(value = "detail_contents", required = false) String detail_contents,
        HttpServletRequest request) throws Exception {
        return ResponseUtil.sendResponse(concernAccidentExpService.findAll(
            ConcernAccidentExp.builder()
                .name(name)
                .tags(tags)
                .title(title)
                .createdAtDescended(created_at_descended)
                .viewsDescended(views_descended)
                .detailContents(detail_contents)
                .build(),
            bfPage,
            request));
    }
}
