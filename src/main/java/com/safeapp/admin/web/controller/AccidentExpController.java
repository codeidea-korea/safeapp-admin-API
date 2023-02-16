package com.safeapp.admin.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.dto.request.RequestAccidentCaseDTO;
import com.safeapp.admin.web.dto.response.ResponseAccidentCaseDTO;
import com.safeapp.admin.utils.ResponseUtil;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.response.ResponseCheckListProjectDTO;
import com.safeapp.admin.web.dto.response.ResponseCheckListProjectSelectDTO;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.AccidentExp;
import com.safeapp.admin.web.model.entity.CheckListProject;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
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

import com.safeapp.admin.web.service.AccidentExpService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/board/accExp")
@RequiredArgsConstructor
@Api(tags = {"AccExp"}, description = "리스트 관리 > 사고사례")
public class AccidentExpController {

    private final AccidentExpService accidentExpService;

    @PostMapping(value = "/add")
    @ApiOperation(value = "사고사례 등록", notes = "사고사례 등록")
    public ResponseEntity<ResponseAccidentCaseDTO> add(@RequestBody RequestAccidentCaseDTO addDto,
            HttpServletRequest request) throws Exception {

        AccidentExp addedAccExp = accidentExpService.add(accidentExpService.toAddEntity(addDto), request);
        return new ResponseEntity<>(ResponseAccidentCaseDTO.builder().accExp(addedAccExp).build(), OK);
    }

    @GetMapping(value = "/find/{id}")
    @ApiOperation(value = "사고사례 단독 조회", notes = "사고사례 단독 조회")
    public ResponseEntity<ResponseAccidentCaseDTO> find(@PathVariable("id") @ApiParam(value = "사고사례 PK", required = true) long id,
            HttpServletRequest request) throws Exception {

        AccidentExp foundAccExp = accidentExpService.find(id, request);
        return new ResponseEntity<>(ResponseAccidentCaseDTO.builder().accExp(foundAccExp).build(), OK);
    }

    @PutMapping(value = "/edit/{id}")
    @ApiOperation(value = "사고사례 수정", notes = "사고사례 수정")
    public ResponseEntity<ResponseAccidentCaseDTO> edit(@PathVariable("id") @ApiParam(value = "사고사례 PK", required = true) long id,
            @RequestBody RequestAccidentCaseDTO editDto, HttpServletRequest request) throws Exception {

        AccidentExp newAccExp = accidentExpService.toEditEntity(editDto);
        newAccExp.setId(id);

        AccidentExp editedAccExp = accidentExpService.edit(newAccExp, request);
        return new ResponseEntity<>(ResponseAccidentCaseDTO.builder().accExp(editedAccExp).build(), OK);
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
            @RequestParam(value = "createdAtStart", required = false) LocalDateTime createdAtStart,
            @RequestParam(value = "createdAtEnd", required = false) LocalDateTime createdAtEnd,
            @RequestParam(value = "createdAtDesc", required = false) @Parameter(description = "최신순") YN createdAtDesc,
            @RequestParam(value = "viewsDesc", required = false) @Parameter(description = "조회순") YN viewsDesc,
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            HttpServletRequest request) throws Exception {

        Long count =
            accidentExpService.countAllByCondition(keyword, adminName, phoneNo, createdAtStart, createdAtEnd);
        List<ResponseAccidentCaseDTO> list =
            accidentExpService.findAllByConditionAndOrderBy(keyword, adminName, phoneNo,
            createdAtStart, createdAtEnd, createdAtDesc, viewsDesc,
            pageNo, pageSize, request);
        Pages pages = new Pages(pageNo, pageSize);

        ListResponse accExpList = new ListResponse(count, list, pages);
        return ResponseUtil.sendResponse(accExpList);
    }

}