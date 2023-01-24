package com.safeapp.admin.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.utils.ResponseUtil;
import com.safeapp.admin.web.dto.request.RequestCheckListProjectDetailDTO;
import com.safeapp.admin.web.dto.response.ResponseCheckListProjectDetailDTO;
import com.safeapp.admin.web.model.entity.CheckListProjectDetail;
import com.safeapp.admin.web.service.CheckListProjectService;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.safeapp.admin.web.service.CheckListProjectDetailService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/checkList/detail")
@AllArgsConstructor
@Api(tags = {"CheckListProjectDetail"}, description = "체크리스트 상세")
public class CheckListProjectDetailController {

    private final CheckListProjectDetailService checkListProjectDetailService;
    private final CheckListProjectService checkListProjectService;

    @PostMapping(value = "/add/{id}")
    @ApiOperation(value = "체크리스트 상세 등록", notes = "체크리스트 상세 등록")
    public ResponseEntity<ResponseCheckListProjectDetailDTO> add(@PathVariable("id") @ApiParam(value = "체크리스트 PK", readOnly = true) long id,
            @RequestBody RequestCheckListProjectDetailDTO addDto, HttpServletRequest request) throws Exception {

        CheckListProjectDetail chkPrjDetInfo = checkListProjectDetailService.toEntity(addDto);
        chkPrjDetInfo.setCheckListProject(checkListProjectService.find(id, request));

        CheckListProjectDetail chkPrjDet = checkListProjectDetailService.add(chkPrjDetInfo, request);
        return new ResponseEntity<>(ResponseCheckListProjectDetailDTO.builder().detail(chkPrjDet).build(), OK);
    }

    @GetMapping(value = "/find/{id}")
    @ApiOperation(value = "체크리스트 상세 확인", notes = "체크리스트 상세 확인")
    public ResponseEntity<ResponseCheckListProjectDetailDTO> find(@PathVariable("id") @ApiParam(value = "체크리스트 상세 PK", required = true) long id,
            HttpServletRequest request) throws Exception {

        CheckListProjectDetail oldChkPrjDet = checkListProjectDetailService.find(id, request);
        return new ResponseEntity<>(ResponseCheckListProjectDetailDTO.builder().detail(oldChkPrjDet).build(), OK);
    }

    @PutMapping(value = "/edit/{id}")
    @ApiOperation(value = "체크리스트 상세 수정", notes = "체크리스트 상세 수정")
    public ResponseEntity<ResponseCheckListProjectDetailDTO> edit(@PathVariable("id") @ApiParam(value = "체크리스트 상세 PK", required = true) long id,
            @RequestBody RequestCheckListProjectDetailDTO modifyDto, HttpServletRequest request) throws Exception {

        CheckListProjectDetail chkPrjDetInfo = checkListProjectDetailService.toEntity(modifyDto);
        chkPrjDetInfo.setId(id);

        CheckListProjectDetail chkPrjDet =  checkListProjectDetailService.edit(chkPrjDetInfo, request);
        return new ResponseEntity<>(ResponseCheckListProjectDetailDTO.builder().detail(chkPrjDet).build(), OK);
    }

    @DeleteMapping(value = "/remove/{id}")
    @ApiOperation(value = "체크리스트 상세 삭제", notes = "체크리스트 상세 삭제")
    public ResponseEntity remove(@PathVariable("id") @ApiParam(value = "체크리스트 상세 PK", required = true) long id,
                HttpServletRequest request) throws Exception {

        checkListProjectDetailService.remove(id, request);
        return ResponseUtil.sendResponse(null);
    }

    @GetMapping(value = "/list")
    @ApiOperation(value = "체크리스트 상세 목록", notes = "체크리스트 상세 목록")
    public ListResponse findAll(Pages pages, HttpServletRequest request) throws Exception {

        return checkListProjectDetailService.findAll(CheckListProjectDetail.builder().build(), pages, request);
    }

}