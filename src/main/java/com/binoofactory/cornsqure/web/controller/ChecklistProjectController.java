package com.binoofactory.cornsqure.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Response;

import com.binoofactory.cornsqure.web.data.StatusType;
import com.binoofactory.cornsqure.web.dto.request.RequestChecklistProjectDTO;
import com.binoofactory.cornsqure.web.dto.request.RequestChecklistProjectModifyDTO;
import com.binoofactory.cornsqure.web.dto.response.ResponseChecklistProjectDTO;
import com.binoofactory.cornsqure.web.dto.response.ResponseChecklistProjectSelectDTO;
import com.binoofactory.cornsqure.web.model.entity.Project;
import com.binoofactory.cornsqure.web.model.entity.Users;
import com.binoofactory.cornsqure.web.repos.jpa.ProjectRepos;
import com.binoofactory.cornsqure.web.repos.jpa.UserRepos;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
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
import com.binoofactory.cornsqure.web.data.YN;
import com.binoofactory.cornsqure.web.model.cmmn.BfListResponse;
import com.binoofactory.cornsqure.web.model.cmmn.BfPage;
import com.binoofactory.cornsqure.web.model.entity.ChecklistProject;
import com.binoofactory.cornsqure.web.service.ChecklistProjectService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/check")
@AllArgsConstructor
@Api(tags = {"ChecklistProject"}, description = "체크리스트", basePath = "/check")
public class ChecklistProjectController {

    private final ChecklistProjectService checklistProjectService;

    @PostMapping(value = "/checklist")
    @ApiOperation(value = "등록", notes = "등록")
    public ResponseEntity<ResponseChecklistProjectDTO> add(
        @RequestBody RequestChecklistProjectDTO dto,
        HttpServletRequest request) throws Exception {
        ChecklistProject result = checklistProjectService.add(checklistProjectService.toEntity(dto), request);
        return new ResponseEntity<>(
                ResponseChecklistProjectDTO
                .builder()
                .checklistProject(result)
                .build(), OK);
    }

    @PutMapping(value = "/checklists/{id}")
    @ApiOperation(value = "수정", notes = "수정")
    public ResponseEntity<ResponseChecklistProjectDTO> modify(
            @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
            @RequestBody RequestChecklistProjectModifyDTO dto,
            HttpServletRequest request) throws Exception {
        ChecklistProject params = checklistProjectService.toEntityModify(dto);
        params.setId(id);
        ChecklistProject result = checklistProjectService.edit(params, request);
        return new ResponseEntity<>(
                ResponseChecklistProjectDTO
                        .builder()
                        .checklistProject(result)
                        .build(), OK);
    }

    @DeleteMapping(value = "/checklists/{id}")
    @ApiOperation(value = "삭제", notes = "삭제")
    public ResponseEntity remove(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        checklistProjectService.remove(id, request);
        return ResponseUtil.sendResponse(null);
    }

    @GetMapping(value = "/checklists/{id}")
    @ApiOperation(value = "조회 (단건)", notes = "조회 (단건)")
    public ResponseEntity<ResponseChecklistProjectSelectDTO> find(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        ChecklistProject result = checklistProjectService.find(id, request);
        return new ResponseEntity<>(
                ResponseChecklistProjectSelectDTO
                        .builder()
                        .checklistProject(result)
                        .build(),OK);
    }

    @GetMapping(value = "/checklists")
    @ApiOperation(value = "목록 조회 (다건)", notes = "목록 조회 (다건)")
    public ResponseEntity<List<ResponseChecklistProjectDTO>> findAll(
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

        return new ResponseEntity<>(checklistProjectService.findAllByCondition(
                userId, projectId, name, tag, visibled, created_at_descended, views_descended, likes_descended, detail_contents, page, request),OK);
    }

    @PatchMapping(value = "/checklists/{id}/like")
    @ApiOperation(value = "좋아요", notes = "좋아요")
    public ResponseEntity like(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        
        checklistProjectService.addLike(id, request);
        return ResponseUtil.sendResponse(true);
    }

    @PatchMapping(value = "/checklists/{id}/dislike")
    @ApiOperation(value = "좋아요 해제", notes = "좋아요 해제")
    public ResponseEntity dislike(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        
        checklistProjectService.removeLike(id, request);
        return ResponseUtil.sendResponse(true);
    }


    @PatchMapping(value = "/checklists/{id}/status-update")
    @ApiOperation(value = "점검/검토/승인 업데이트", notes = "점검/검토/승인 업데이트")
    public ResponseEntity statusUpdate(
            @PathVariable("id") @ApiParam(value = "체크리스트ID", required = true) long id,
            @RequestParam(value = "type") @Parameter(description = "타입") StatusType type,
            HttpServletRequest request) throws Exception {
        checklistProjectService.updateStatus(id,type);
        return ResponseUtil.sendResponse(true);
    }

    @GetMapping(value = "/checklists/{id}/liked")
    @ApiOperation(value = "나의 좋아요 여부", notes = "나의 좋아요 여부")
    public ResponseEntity liked(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        
        return ResponseUtil.sendResponse(checklistProjectService.isLiked(id, request));
    }
}
