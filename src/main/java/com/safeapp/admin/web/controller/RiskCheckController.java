package com.safeapp.admin.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.dto.request.RequestRiskCheckDTO;
import com.safeapp.admin.web.dto.response.ResponseCheckListProjectDTO;
import com.safeapp.admin.web.dto.response.ResponseRiskCheckDTO;
import com.safeapp.admin.utils.ResponseUtil;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.response.ResponseRiskCheckSelectDTO;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.RiskCheck;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
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

import com.safeapp.admin.web.service.RiskCheckService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/riskCheck")
@AllArgsConstructor
@Api(tags = {"RiskCheck"}, description = "리스트 관리 > 위험성 평가표")
public class RiskCheckController {

    private final RiskCheckService riskCheckService;

    @PostMapping(value = "/add")
    @ApiOperation(value = "위험성 평가표 등록", notes = "위험성 평가표 등록")
    public ResponseEntity<ResponseRiskCheckDTO> add(@RequestBody RequestRiskCheckDTO addDto,
                                                    HttpServletRequest request) throws Exception {

        RiskCheck addedRiskChk = riskCheckService.add(riskCheckService.toEntity(addDto), request);
        return new ResponseEntity<>(ResponseRiskCheckDTO.builder().riskCheck(addedRiskChk).build(), OK);
    }

    @GetMapping(value = "/find/{id}")
    @ApiOperation(value = "위험성 평가표 단독 조회", notes = "위험성 평가표 단독 조회")
    public ResponseEntity<ResponseRiskCheckSelectDTO> find(@PathVariable("id") @ApiParam(value = "위험성 평가표 PK", required = true) long id,
            HttpServletRequest request) throws Exception {

        RiskCheck oldRiskChk = riskCheckService.find(id, request);
        return new ResponseEntity<>(ResponseRiskCheckSelectDTO.builder().riskCheck(oldRiskChk).build(), OK);
    }

    @PutMapping(value = "/edit/{id}")
    @ApiOperation(value = "수정", notes = "수정")
    public ResponseEntity<ResponseRiskCheckDTO> edit(@PathVariable("id") @ApiParam(value = "위험성 평가표 PK", required = true) long id,
                                                     @RequestBody RequestRiskCheckDTO modifyDto, HttpServletRequest request) throws Exception {

        RiskCheck riskChk = riskCheckService.toEntity(modifyDto);
        riskChk.setId(id);

        RiskCheck editedRiskChk = riskCheckService.edit(riskChk, request);
        return new ResponseEntity<>(ResponseRiskCheckDTO.builder().riskCheck(editedRiskChk).build(), OK);
    }

    @DeleteMapping(value = "/remove/{id}")
    @ApiOperation(value = "삭제", notes = "삭제")
    public ResponseEntity remove(@PathVariable("id") @ApiParam(value = "위험성 평가표 PK", required = true) long id,
            HttpServletRequest request) throws Exception {

        riskCheckService.remove(id, request);
        return ResponseUtil.sendResponse(null);
    }

    @GetMapping(value = "/list")
    @ApiOperation(value = "위험성 평가표 목록 조회", notes = "위험성 평가표 목록 조회")
    public ResponseEntity<List<ResponseRiskCheckDTO>> findAll(
            @RequestParam(value = "keyword", required = false) @Parameter(description = "키워드") String keyword,
            @RequestParam(value = "userName", required = false) @Parameter(description = "이름") String userName,
            @RequestParam(value = "phoneNo", required = false) @Parameter(description = "휴대폰번호") String phoneNo,
            @RequestParam(value = "visibled", required = false) @Parameter(description = "공개 여부") YN visibled,
            @RequestParam(value = "createdAtStart", required = false) LocalDateTime createdAtStart,
            @RequestParam(value = "createdAtEnd", required = false) LocalDateTime createdAtEnd,
            @RequestParam(value = "createdAtDesc", required = false) @Parameter(description = "최신순") YN createdAtDesc,
            @RequestParam(value = "likesDesc", required = false) @Parameter(description = "좋아요순") YN likesDesc,
            @RequestParam(value = "viewsDesc", required = false) @Parameter(description = "조회순") YN viewsDesc,
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            HttpServletRequest request) throws Exception {

        /*
        Long count =
            riskCheckService.countAllByCondition(keyword, userName, phoneNo, visibled, createdAtStart, createdAtEnd);
        List<ResponseCheckListProjectDTO> list =
            riskCheckService.findAllByConditionAndOrderBy(keyword, userName, phoneNo,
            visibled, createdAtStart, createdAtEnd, createdAtDesc, likesDesc, viewsDesc,
            pageNo, pageSize, request);
        Pages pages = new Pages(pageNo, pageSize);

        ListResponse riskChkList = new ListResponse(count, list, pages);
        */

        return ResponseUtil.sendResponse(null);
    }

}