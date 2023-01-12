package com.binoofactory.cornsqure.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Response;

import com.binoofactory.cornsqure.web.dto.request.RequestChecklistProjectDetailDTO;
import com.binoofactory.cornsqure.web.dto.response.ResponseChecklistProjectDetailDTO;
import com.binoofactory.cornsqure.web.model.entity.ChecklistProject;
import com.binoofactory.cornsqure.web.service.ChecklistProjectService;
import lombok.AllArgsConstructor;
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

import com.binoofactory.cornsqure.web.model.cmmn.BfListResponse;
import com.binoofactory.cornsqure.web.model.cmmn.BfPage;
import com.binoofactory.cornsqure.web.model.cmmn.service.BfCRUDService;
import com.binoofactory.cornsqure.web.model.entity.ChecklistProjectDetail;
import com.binoofactory.cornsqure.web.model.entity.ChecklistProjectDetail;
import com.binoofactory.cornsqure.web.service.ChecklistProjectDetailService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/checklistProjectDetail")
@AllArgsConstructor
@Api(tags = {"ChecklistProjectDetail"}, description = "체크리스트 상세", basePath = "/api/checklistProjectDetail")
public class ChecklistProjectDetailController {

    private final ChecklistProjectDetailService checklistProjectDetailService;
    private final ChecklistProjectService checklistProjectService;

    @PostMapping(value = "/{id}")
    @ApiOperation(value = "체크리스트 상세 등록", notes = "체크리스트 상세 등록")
    public ResponseEntity<ResponseChecklistProjectDetailDTO> add(
            @PathVariable("id") @ApiParam(value = "체크리스트 본문 ID", readOnly = true)Long id,
        @RequestBody RequestChecklistProjectDetailDTO dto,
        HttpServletRequest request) throws Exception {
        ChecklistProjectDetail params = checklistProjectDetailService.toEntity(dto);
        params.setChecklistProject(checklistProjectService.find(id, request));
        ChecklistProjectDetail result = checklistProjectDetailService.add(params, request);
        return new ResponseEntity<>(
            ResponseChecklistProjectDetailDTO
                    .builder()
                    .detail(result)
                    .build(), OK);
    }

    @PutMapping(value = "/{id}")
    @ApiOperation(value = "수정", notes = "수정")
    public ResponseEntity<ResponseChecklistProjectDetailDTO> modify(
        @PathVariable("id") @ApiParam(value = "디테일ID", required = true) long id,
        @RequestBody RequestChecklistProjectDetailDTO dto,
        HttpServletRequest request) throws Exception {
        ChecklistProjectDetail params = checklistProjectDetailService.toEntity(dto);
        params.setId(id);
        ChecklistProjectDetail result =  checklistProjectDetailService.edit(params, request);
        return new ResponseEntity<>(
                ResponseChecklistProjectDetailDTO
                        .builder()
                        .detail(result)
                        .build(),OK
        );
    }

    @DeleteMapping(value = "")
    @ApiOperation(value = "삭제", notes = "삭제")
    public void remove(
        @PathVariable("id") @ApiParam(value = "디테일ID", required = true) long id,
        HttpServletRequest request) throws Exception {
        checklistProjectDetailService.remove(id, request);
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "조회 (단건)", notes = "조회 (단건)")
    public ResponseEntity<ResponseChecklistProjectDetailDTO> find(
        @PathVariable("id") @ApiParam(value = "디테일ID", required = true) long id,
        HttpServletRequest request) throws Exception {
        ChecklistProjectDetail detail = checklistProjectDetailService.find(id, request);
        return new ResponseEntity<>(
                ResponseChecklistProjectDetailDTO
                        .builder()
                        .detail(detail)
                        .build(), OK
        );
    }

    @GetMapping(value = "")
    @ApiOperation(value = "목록 조회 (다건)", notes = "목록 조회 (다건)")
    public BfListResponse findAll(
        BfPage bfPage,
        HttpServletRequest request) throws Exception {
        return checklistProjectDetailService.findAll(
            ChecklistProjectDetail.builder()
                .build(),
            bfPage,
            request);
    }
}
