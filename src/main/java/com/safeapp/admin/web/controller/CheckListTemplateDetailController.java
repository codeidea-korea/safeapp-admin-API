package com.safeapp.admin.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.utils.ResponseUtil;
import com.safeapp.admin.web.dto.request.RequestCheckListTemplateDetailDTO;
import com.safeapp.admin.web.dto.response.ResponseCheckListProjectDetailDTO;
import com.safeapp.admin.web.dto.response.ResponseCheckListTemplateDTO;
import com.safeapp.admin.web.dto.response.ResponseCheckListTemplateDetailDTO;
import com.safeapp.admin.web.model.entity.CheckListProjectDetail;
import com.safeapp.admin.web.model.entity.CheckListTemplate;
import com.safeapp.admin.web.service.CheckListTemplateService;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.CheckListTemplateDetail;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.safeapp.admin.web.service.CheckListTemplateDetailService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/checkList/template/detail")
@AllArgsConstructor
@Api(tags = {"CheckListTemplateDetail"}, description = "체크리스트 템플릿 상세")
public class CheckListTemplateDetailController {

    private final CheckListTemplateDetailService checkListTemplateDetailService;
    private final CheckListTemplateService checkListTemplateService;

    @PostMapping(value = "/add/{id}")
    @ApiOperation(value = "체크리스트 템플릿 상세 등록", notes = "체크리스트 템플릿 상세 등록")
    public ResponseEntity<ResponseCheckListTemplateDetailDTO> add(@PathVariable("id") @ApiParam(value = "체크리스트 템플릿 상세 PK", readOnly = true) long id,
                @RequestBody RequestCheckListTemplateDetailDTO addDto, HttpServletRequest request) throws Exception {

        CheckListTemplateDetail chkTmpDet = checkListTemplateDetailService.toEntity(addDto);
        chkTmpDet.setCheckListTemplate(checkListTemplateService.find(id, request));

        CheckListTemplateDetail addedChkTmpDet = checkListTemplateDetailService.add(chkTmpDet, request);
        return new ResponseEntity<>(ResponseCheckListTemplateDetailDTO.builder().detail(addedChkTmpDet).build(),OK);
    }

    @GetMapping(value = "/find/{id}")
    @ApiOperation(value = "체크리스트 템플릿 상세 단독 조회", notes = "체크리스트 템플릿 단독 조회")
    public ResponseEntity<CheckListTemplateDetail> find(@PathVariable("id") @ApiParam(value = "체크리스트 템플릿 상세 PK", required = true) long id,
            HttpServletRequest request) throws Exception {

        CheckListTemplateDetail oldChkTmpDet = checkListTemplateDetailService.find(id, request);
        return ResponseUtil.sendResponse(oldChkTmpDet);
    }

    @PutMapping(value = "/edit/{id}")
    @ApiOperation(value = "체크리스트 템플릿 상세 수정", notes = "체크리스트 템플릿 상세 수정")
    public ResponseEntity<CheckListTemplateDetail> edit(@PathVariable("id") @ApiParam(value = "체크리스트 템플릿 상세 PK", required = true) long id,
        @RequestBody CheckListTemplateDetail chkTmpDet, HttpServletRequest request) throws Exception {

        chkTmpDet.setId(id);

        CheckListTemplateDetail editedChkTmpDet = checkListTemplateDetailService.edit(chkTmpDet, request);
        return ResponseUtil.sendResponse(editedChkTmpDet);
    }

    @DeleteMapping(value = "/remove/{id}")
    @ApiOperation(value = "체크리스트 템플릿 상세 삭제", notes = "체크리스트 템플릿 상세 삭제")
    public void remove(@PathVariable("id") @ApiParam(value = "일련번호", required = true) long id, HttpServletRequest request) throws Exception {

        checkListTemplateDetailService.remove(id, request);
    }

    @GetMapping(value = "")
    @ApiOperation(value = "체크리스트 템플릿 상세 목록 조회", notes = "체크리스트 템플릿 상세 목록 조회")
    public ListResponse findAll(Pages pages, HttpServletRequest request) throws Exception {

        return checkListTemplateDetailService.findAll(CheckListTemplateDetail.builder().build(), pages, request);
    }

}