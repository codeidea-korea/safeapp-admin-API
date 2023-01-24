package com.safeapp.admin.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.request.RequestCheckListTemplateDTO;
import com.safeapp.admin.web.dto.response.ResponseCheckListTemplateDTO;
import com.safeapp.admin.utils.ResponseUtil;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
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

import com.safeapp.admin.web.service.CheckListTemplateService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/checkList/template")
@AllArgsConstructor
@Api(tags = {"CheckListTemplate"}, description = "체크리스트 템플릿")
public class CheckListTemplateController {

    private final CheckListTemplateService checklistTemplateService;

    @PostMapping(value = "/add")
    @ApiOperation(value = "체크리스트 템플릿 등록", notes = "체크리스트 템플릿 등록")
    public ResponseEntity<ResponseCheckListTemplateDTO> add(
        @RequestBody RequestCheckListTemplateDTO dto,
        HttpServletRequest request) throws Exception {
        CheckListTemplate params = checklistTemplateService.toEntity(dto);
        CheckListTemplate result = checklistTemplateService.add(params, request);
        return new ResponseEntity<>(
                ResponseCheckListTemplateDTO
                        .builder().
                        template(result)
                        .build(),OK
        );
    }

    @PutMapping(value = "/{id}")
    @ApiOperation(value = "수정", notes = "수정")
    public ResponseEntity modify(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        @RequestBody CheckListTemplate params,
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
    public ResponseEntity<ResponseCheckListTemplateSelectDTO> find(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        CheckListTemplate result = checklistTemplateService.find(id, request);
        return new ResponseEntity<>(
                ResponseCheckListTemplateSelectDTO
                        .builder()
                        .template(result)
                        .build(), OK);
    }

    @GetMapping(value = "")
    @ApiOperation(value = "목록 조회 (다건)", notes = "목록 조회 (다건)")
    public ResponseEntity<List<ResponseCheckListTemplateDTO>> findAll(
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

}