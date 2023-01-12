package com.binoofactory.cornsqure.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.binoofactory.cornsqure.web.dto.request.RequestAccidentCaseDTO;
import com.binoofactory.cornsqure.web.dto.response.ResponseAccidentCaseDTO;
import lombok.RequiredArgsConstructor;
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
import com.binoofactory.cornsqure.web.data.YN;
import com.binoofactory.cornsqure.web.model.cmmn.BfListResponse;
import com.binoofactory.cornsqure.web.model.cmmn.BfPage;
import com.binoofactory.cornsqure.web.model.entity.AccidentExp;
import com.binoofactory.cornsqure.web.service.AccidentExpService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/board/accidents")
@RequiredArgsConstructor
@Api(tags = {"AccidentExp"}, description = "사고 사례", basePath = "/board/accidents")
public class AccidentExpController {

    private final AccidentExpService accidentExpService;

    @PostMapping(value = "")
    @ApiOperation(value = "사고사례 등록", notes = "사고사례 등록")
    public ResponseEntity<ResponseAccidentCaseDTO> add(
        @RequestBody RequestAccidentCaseDTO dto,
        HttpServletRequest request) throws Exception {
        AccidentExp params = accidentExpService.toEntity(dto);
        return ResponseUtil.sendResponse(accidentExpService.add(params, request));
    }

    @PutMapping(value = "/{id}")
    @ApiOperation(value = "수정", notes = "수정")
    public ResponseEntity modify(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        @RequestBody AccidentExp params,
        HttpServletRequest request) throws Exception {

        params.setId(id);
        return ResponseUtil.sendResponse(accidentExpService.edit(params, request));
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "삭제", notes = "삭제")
    public ResponseEntity remove(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        accidentExpService.remove(id, request);
        return ResponseUtil.sendResponse(null);
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "조회 (단건)", notes = "조회 (단건)")
    public ResponseEntity<ResponseAccidentCaseDTO> find(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        return ResponseUtil.sendResponse(accidentExpService.find(id, request));
    }

    @GetMapping(value = "")
    @ApiOperation(value = "목록 조회 (다건)", notes = "목록 조회 (다건)")
    public ResponseEntity<BfListResponse> findAll(
        BfPage bfPage,
        @RequestParam(value = "name", required = false) @ApiParam("이름") String name,
        @RequestParam(value = "title", required = false) @ApiParam("제목") String title,
        @RequestParam(value = "tags", required = false) @ApiParam("태그") String tags,
        @RequestParam(value = "created_at_descended", required = false) YN created_at_descended,
        @RequestParam(value = "views_descended", required = false) YN views_descended,
        @RequestParam(value = "detail_contents", required = false) String detail_contents,
        HttpServletRequest request) throws Exception {
        return ResponseUtil.sendResponse(accidentExpService.findAll(
            AccidentExp.builder()
                .name(name)
                .title(title)
                .tags(tags)
                .createdAtDescended(created_at_descended)
                .viewsDescended(views_descended)
                .detailContents(detail_contents)
                .build(),
            bfPage,
            request));
    }
}
