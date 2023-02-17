package com.safeapp.admin.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.utils.ResponseUtil;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.request.RequestConcernAccidentDTO;
import com.safeapp.admin.web.dto.request.RequestConcernAccidentEditDTO;
import com.safeapp.admin.web.dto.response.ResponseConcernAccidentDTO;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.AccidentExp;
import com.safeapp.admin.web.model.entity.ConcernAccidentExp;
import com.safeapp.admin.web.model.entity.Reports;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.safeapp.admin.web.service.ConcernAccidentExpService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/board/conExp")
@RequiredArgsConstructor
@Api(tags = {"ConExp"}, description = "리스트 관리 > 아차사고")
public class ConcernAccidentExpController {

    private final ConcernAccidentExpService concernAccidentExpService;

    @PostMapping(value = "/add")
    @ApiOperation(value = "아차사고 등록", notes = "아차사고 등록")
    public ResponseEntity<ResponseConcernAccidentDTO> add(@RequestBody RequestConcernAccidentDTO addDto, HttpServletRequest request) throws Exception {
        ConcernAccidentExp addedConExp = concernAccidentExpService.add(concernAccidentExpService.toAddEntity(addDto), request);
        return new ResponseEntity<>(ResponseConcernAccidentDTO.builder().conExp(addedConExp).build(), OK);
    }

    @GetMapping(value = "/find/{id}")
    @ApiOperation(value = "아차사고 단독 조회", notes = "아차사고 단독 조회")
    public ResponseEntity<ResponseConcernAccidentDTO> find(@PathVariable("id") @ApiParam(value = "아차사고 PK", required = true) long id,
            HttpServletRequest request) throws Exception {

        ConcernAccidentExp conExp = concernAccidentExpService.find(id, request);
        return new ResponseEntity<>(ResponseConcernAccidentDTO.builder().conExp(conExp).build(), OK);
    }

    @PutMapping(value = "/edit/{id}")
    @ApiOperation(value = "아차사고 수정", notes = "아차사고 수정")
    public ResponseEntity<ResponseConcernAccidentDTO> edit(@PathVariable("id") @ApiParam(value = "아차사고 PK", required = true) long id,
            @RequestBody RequestConcernAccidentEditDTO editDto, HttpServletRequest request) throws Exception {

        ConcernAccidentExp newConExp = concernAccidentExpService.toEditEntity(editDto);
        newConExp.setId(id);

        ConcernAccidentExp editedConExp = concernAccidentExpService.edit(newConExp, request);
        return new ResponseEntity<>(ResponseConcernAccidentDTO.builder().conExp(editedConExp).build(), OK);
    }

    @DeleteMapping(value = "/remove/{id}")
    @ApiOperation(value = "아차사고 삭제", notes = "아차사고 삭제")
    public ResponseEntity remove(@PathVariable("id") @ApiParam(value = "아차사고 PK", required = true) long id,
            HttpServletRequest request) throws Exception {

        concernAccidentExpService.remove(id, request);
        return ResponseUtil.sendResponse(null);
    }

    @GetMapping(value = "/list")
    @ApiOperation(value = "아차사고 목록 조회", notes = "아차사고 목록 조회")
    public ResponseEntity<List<ResponseConcernAccidentDTO>> findAll(
            @RequestParam(value = "keyword", required = false) @Parameter(description = "키워드") String keyword,
            @RequestParam(value = "adminName", required = false) @Parameter(description = "이름") String adminName,
            @RequestParam(value = "phoneNo", required = false) @Parameter(description = "휴대폰번호") String phoneNo,
            @RequestParam(value = "createdAtStart", required = false) @Parameter(description = "등록일시 시작") String createdAtStart,
            @RequestParam(value = "createdAtEnd", required = false) @Parameter(description = "등록일시 종료") String createdAtEnd,
            @RequestParam(value = "createdAtDesc", required = false) @Parameter(description = "최신순") YN createdAtDesc,
            @RequestParam(value = "viewsDesc", required = false) @Parameter(description = "조회순") YN viewsDesc,
            @RequestParam(value = "pageNo", defaultValue = "1")  @Parameter(description = "현재 페이지 번호") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") @Parameter(description = "1 페이지 당 목록 수") int pageSize,
            HttpServletRequest request) throws Exception {

        Pages pages = new Pages(pageNo, pageSize);
        return
            ResponseUtil.sendResponse(concernAccidentExpService.findAll(
                ConcernAccidentExp.builder()
                .keyword(keyword)
                .adminName(adminName)
                .phoneNo(phoneNo)
                .createdAtStart(createdAtStart)
                .createdAtEnd(createdAtEnd)
                .createdAtDesc(createdAtDesc)
                .viewsDesc(viewsDesc)
                .build(), pages, request)
            );
    }

    @PostMapping(value = "/report/add/{id}")
    @ApiOperation(value = "아차사고 신고")
    public ResponseEntity addReport(@PathVariable("id") @ApiParam(value = "아차사고 PK", required = true) long id,
            @RequestParam(value = "report_reason") @ApiParam(value = "신고사유") String reportReason, HttpServletRequest request) {

        concernAccidentExpService.addReport(id, reportReason, request);
        return new ResponseEntity(CREATED);
    }

    @GetMapping(value = "/report/find/{id}")
    @ApiOperation(value = "아차사고 신고 단독 조회", notes = "아차사고 신고 단독 조회")
    public ResponseEntity<List<Reports>> findReports(@PathVariable("id") @ApiParam(value = "아차사고 PK", required = true) long id,
            HttpServletRequest request) throws Exception {

        List<Reports> reports = concernAccidentExpService.findReports(id, request);
        return new ResponseEntity<>(reports, OK);
    }

    @GetMapping(value = "/report/list")
    @ApiOperation(value = "아차사고 신고 목록 조회", notes = "아차사고 신고 목록 조회")
    public ResponseEntity<List<ResponseConcernAccidentDTO>> findAllReport(
            @RequestParam(value = "keyword", required = false) @Parameter(description = "키워드") String keyword,
            @RequestParam(value = "createdAtDesc", required = false) @Parameter(description = "최신순") YN createdAtDesc,
            @RequestParam(value = "viewsDesc", required = false) @Parameter(description = "조회순") YN viewsDesc,
            @RequestParam(value = "pageNo", defaultValue = "1")  @Parameter(description = "현재 페이지 번호") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") @Parameter(description = "1 페이지 당 목록 수") int pageSize,
            HttpServletRequest request) throws Exception {

        Pages pages = new Pages(pageNo, pageSize);
        return
            ResponseUtil.sendResponse(concernAccidentExpService.findAllReport(
                ConcernAccidentExp.builder()
                .keyword(keyword)
                .createdAtDesc(createdAtDesc)
                .viewsDesc(viewsDesc)
                .build(), pages, request)
            );
    }

}