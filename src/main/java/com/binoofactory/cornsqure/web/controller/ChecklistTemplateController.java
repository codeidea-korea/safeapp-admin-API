package com.binoofactory.cornsqure.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.binoofactory.cornsqure.web.data.YN;
import com.binoofactory.cornsqure.web.dto.request.RequestChecklistTemplateDTO;
import com.binoofactory.cornsqure.web.dto.response.ResponseChecklistTemplateDTO;
import com.binoofactory.cornsqure.web.dto.response.ResponseChecklistTemplateSelectDTO;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
import com.binoofactory.cornsqure.web.model.entity.ChecklistTemplate;
import com.binoofactory.cornsqure.web.service.ChecklistTemplateService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/template/checklists")
@Api(tags = {"ChecklistTemplate"}, description = "체크리스트 템플릿", basePath = "/template/checklists")
public class ChecklistTemplateController {

    private final ChecklistTemplateService checklistTemplateService;

    @Autowired
    public ChecklistTemplateController(ChecklistTemplateService checklistTemplateService) {
        this.checklistTemplateService = checklistTemplateService;
    }

    @PostMapping(value = "")
    @ApiOperation(value = "등록", notes = "등록")
    public ResponseEntity<ResponseChecklistTemplateDTO> add(
        @RequestBody RequestChecklistTemplateDTO dto,
        HttpServletRequest request) throws Exception {
        ChecklistTemplate params = checklistTemplateService.toEntity(dto);
        ChecklistTemplate result = checklistTemplateService.add(params, request);
        return new ResponseEntity<>(
                ResponseChecklistTemplateDTO
                        .builder().
                        template(result)
                        .build(),OK
        );
    }

    @PutMapping(value = "/{id}")
    @ApiOperation(value = "수정", notes = "수정")
    public ResponseEntity modify(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        @RequestBody ChecklistTemplate params,
        HttpServletRequest request) throws Exception {
        params.setId(id);
        return ResponseUtil.sendResponse(checklistTemplateService.edit(params, request));
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "삭제", notes = "삭제")
    public ResponseEntity remove(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        checklistTemplateService.remove(id, request);
        return ResponseUtil.sendResponse(null);
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "조회 (단건)", notes = "조회 (단건)")
    public ResponseEntity<ResponseChecklistTemplateSelectDTO> find(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        ChecklistTemplate result = checklistTemplateService.find(id, request);
        return new ResponseEntity<>(
                ResponseChecklistTemplateSelectDTO
                        .builder()
                        .template(result)
                        .build(), OK);
    }

    @GetMapping(value = "")
    @ApiOperation(value = "목록 조회 (다건)", notes = "목록 조회 (다건)")
    public ResponseEntity<List<ResponseChecklistTemplateDTO>> findAll(
            @RequestParam(value = "userId", required = false) @Parameter(description = "유저ID") Long userId,
            @RequestParam(value = "projectId", required = false) @Parameter(description = "프로젝트ID") Long projectId,
            @RequestParam(value = "name", required = false) @Parameter(description = "제목") String name,
            @RequestParam(value = "tag", required = false) @Parameter(description = "태그") String tag,
            @RequestParam(value = "visibled", required = false) @Parameter(description = "전체공개여부") YN visibled,
            @RequestParam(value = "created_at_descended", required = false) @Parameter(description = "작성순") YN created_at_descended,
            @RequestParam(value = "views_descended", required = false) @Parameter(description = "조회순") YN views_descended,
            @RequestParam(value = "likes_descended", required = false) @Parameter(description = "좋아요순") YN likes_descended,
            @RequestParam(value = "detail_contents", required = false) @Parameter(description = "본문내용") String detail_contents,
            Pageable page, HttpServletRequest request) throws Exception {
        return new ResponseEntity<>(checklistTemplateService.findAllByCondition(
                userId, projectId, name, tag, created_at_descended, views_descended, likes_descended, detail_contents, page),OK);
    }

    @PatchMapping(value = "/{id}/like")
    @ApiOperation(value = "좋아요", notes = "좋아요")
    public ResponseEntity like(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        
        checklistTemplateService.addLike(id, request);
        return ResponseUtil.sendResponse(true);
    }

    @PatchMapping(value = "/{id}/dislike")
    @ApiOperation(value = "좋아요 해제", notes = "좋아요 해제")
    public ResponseEntity dislike(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        
        checklistTemplateService.removeLike(id, request);
        return ResponseUtil.sendResponse(true);
    }

    @GetMapping(value = "/{id}/liked")
    @ApiOperation(value = "나의 좋아요 여부", notes = "나의 좋아요 여부")
    public ResponseEntity liked(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        
        return ResponseUtil.sendResponse(checklistTemplateService.isLiked(id, request));
    }
}
