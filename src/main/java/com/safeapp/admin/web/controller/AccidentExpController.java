package com.safeapp.admin.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.dto.request.RequestAccidentCaseDTO;
import com.safeapp.admin.web.dto.request.RequestAccidentCaseEditDTO;
import com.safeapp.admin.web.dto.response.ResponseAccidentCaseDTO;
import com.safeapp.admin.utils.ResponseUtil;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.response.ResponseCheckListProjectDTO;
import com.safeapp.admin.web.dto.response.ResponseCheckListProjectSelectDTO;
import com.safeapp.admin.web.dto.response.ResponseConcernAccidentDTO;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.AccidentExp;
import com.safeapp.admin.web.model.entity.CheckListProject;
import com.safeapp.admin.web.model.entity.ConcernAccidentExp;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.safeapp.admin.web.service.AccidentExpService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/board/accExp")
@RequiredArgsConstructor
@Api(tags = {"AccExp"}, description = "리스트 관리 > 사고사례")
public class AccidentExpController {

    private final AccidentExpService accidentExpService;

    @PostMapping(value = "/add")
    @ApiOperation(value = "사고사례 등록", notes = "사고사례 등록")
    public ResponseEntity<ResponseAccidentCaseDTO> add(@RequestBody RequestAccidentCaseDTO addDto, HttpServletRequest request) throws Exception {
        AccidentExp addedAccExp = accidentExpService.add(accidentExpService.toAddEntity(addDto), request);
        return new ResponseEntity<>(ResponseAccidentCaseDTO.builder().accExp(addedAccExp).build(), OK);
    }

    @PostMapping(value = "/add/{id}/files")
    @ApiOperation(value = "사고사례 첨부파일 등록")
    public ResponseEntity addFiles(
            @PathVariable("id") @ApiParam(value = "사고사례 PK", required = true) long id,
            @RequestPart(required = false) List<MultipartFile> files,
            HttpServletRequest request) throws Exception {

        accidentExpService.addFiles(id, files, request);
        return new ResponseEntity(null, CREATED);
    }

    @GetMapping(value = "/find/{id}")
    @ApiOperation(value = "사고사례 단독 조회", notes = "사고사례 단독 조회")
    public ResponseEntity<ResponseAccidentCaseDTO> find(@PathVariable("id") @ApiParam(value = "사고사례 PK", required = true) long id,
            HttpServletRequest request) throws Exception {

        ResponseAccidentCaseDTO accExp = accidentExpService.findAccExp(id, request);
        return new ResponseEntity<>(accExp, OK);
    }

    @PutMapping(value = "/edit/{id}")
    @ApiOperation(value = "사고사례 수정", notes = "사고사례 수정")
    public ResponseEntity<ResponseAccidentCaseDTO> edit(@PathVariable("id") @ApiParam(value = "사고사례 PK", required = true) long id,
            @RequestBody RequestAccidentCaseEditDTO editDto, HttpServletRequest request) throws Exception {

        AccidentExp newAccExp = accidentExpService.toEditEntity(editDto);
        newAccExp.setId(id);

        AccidentExp editedAccExp = accidentExpService.edit(newAccExp, request);
        return new ResponseEntity<>(ResponseAccidentCaseDTO.builder().accExp(editedAccExp).build(), OK);
    }

    @DeleteMapping(value = "/file/remove/{id}")
    @ApiOperation(value = "사고사례 첨부파일 삭제", notes = "사고사례 첨부파일 삭제")
    public ResponseEntity removeFile(@PathVariable("id") @ApiParam(value = "사고사례 첨부파일 PK", required = true) long id,
            HttpServletRequest request) throws Exception {

        accidentExpService.removeFile(id, request);
        return ResponseUtil.sendResponse(null);
    }

    @DeleteMapping(value = "/remove/{id}")
    @ApiOperation(value = "사고사례 삭제", notes = "사고사례 삭제")
    public ResponseEntity remove(@PathVariable("id") @ApiParam(value = "사고사례 PK", required = true) long id,
            HttpServletRequest request) throws Exception {

        accidentExpService.remove(id, request);
        return ResponseUtil.sendResponse(null);
    }

    @GetMapping(value = "/list")
    @ApiOperation(value = "사고사례 목록 조회", notes = "사고사례 목록 조회")
    public ResponseEntity<List<ResponseAccidentCaseDTO>> findAll(
            @RequestParam(value = "keyword", required = false) @Parameter(description = "키워드") String keyword,
            @RequestParam(value = "adminName", required = false) @Parameter(description = "이름") String adminName,
            @RequestParam(value = "phoneNo", required = false) @Parameter(description = "휴대폰번호") String phoneNo,
            @RequestParam(value = "createdAtStart", required = false) @Parameter(description = "등록일시 시작") String createdAtStart,
            @RequestParam(value = "createdAtEnd", required = false) @Parameter(description = "등록일시 종료") String createdAtEnd,
            @RequestParam(value = "createdAtDesc", required = false) @Parameter(description = "최신순") YN createdAtDesc,
            @RequestParam(value = "viewsDesc", required = false) @Parameter(description = "조회순") YN viewsDesc,
            @RequestParam(value = "pageNo", defaultValue = "1") @Parameter(description = "현재 페이지 번호") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") @Parameter(description = "1 페이지 당 목록 수") int pageSize,
            HttpServletRequest request) throws Exception {

        Pages pages = new Pages(pageNo, pageSize);
        return
            ResponseUtil.sendResponse(accidentExpService.findAllByCondition(
                AccidentExp.builder()
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

}