package com.safeapp.admin.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.request.RequestCheckListTemplateDTO;
import com.safeapp.admin.web.dto.response.ResponseCheckListProjectDTO;
import com.safeapp.admin.web.dto.response.ResponseCheckListTemplateDTO;
import com.safeapp.admin.utils.ResponseUtil;
import com.safeapp.admin.web.dto.response.ResponseCheckListTemplateSelectDTO;
import com.safeapp.admin.web.model.entity.CheckListProject;
import com.safeapp.admin.web.model.entity.CheckListTemplate;
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
@Api(tags = {"CheckListTemplate"}, description = "리스트 관리 > 체크리스트 > 체크리스트 템플릿")
public class CheckListTemplateController {

    private final CheckListTemplateService checkListTemplateService;

    @PostMapping(value = "/add")
    @ApiOperation(value = "체크리스트 템플릿 등록", notes = "체크리스트 템플릿 등록")
    public ResponseEntity<ResponseCheckListTemplateDTO> add(@RequestBody RequestCheckListTemplateDTO addDto,
            HttpServletRequest request) throws Exception {

        CheckListTemplate addedChkTmp = checkListTemplateService.add(checkListTemplateService.toEntity(addDto), request);
        return new ResponseEntity<>(ResponseCheckListTemplateDTO.builder().template(addedChkTmp).build(), OK);
    }

    @GetMapping(value = "/find/{id}")
    @ApiOperation(value = "체크리스트 템플릿 단독 조회", notes = "체크리스트 템플릿 단독 조회")
    public ResponseEntity<ResponseCheckListTemplateSelectDTO> find(@PathVariable("id") @ApiParam(value = "체크리스트 템플릿 PK", required = true) long id,
            HttpServletRequest request) throws Exception {

        CheckListTemplate oldChkTmp = checkListTemplateService.find(id, request);
        return new ResponseEntity<>(ResponseCheckListTemplateSelectDTO.builder().template(oldChkTmp).build(), OK);
    }

    @PutMapping(value = "/edit/{id}")
    @ApiOperation(value = "체크리스트 템플릿 수정", notes = "체크리스트 템플릿 수정")
    public ResponseEntity edit(@PathVariable("id") @ApiParam(value = "체크리스트 템플릿 PK", required = true) long id,
            @RequestBody CheckListTemplate chkTmp, HttpServletRequest request) throws Exception {

        chkTmp.setId(id);

        CheckListTemplate editedChkTmp = checkListTemplateService.edit(chkTmp, request);
        return new ResponseEntity<>(ResponseCheckListTemplateDTO.builder().template(editedChkTmp).build(), OK);
    }

    @DeleteMapping(value = "/remove/{id}")
    @ApiOperation(value = "체크리스트 템플릿 삭제", notes = "체크리스트 템플릿 삭제")
    public ResponseEntity remove(@PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
            HttpServletRequest request) throws Exception {

        checkListTemplateService.remove(id, request);
        return ResponseUtil.sendResponse(null);
    }

    @GetMapping(value = "")
    @ApiOperation(value = "체크리스트 템플릿 목록", notes = "체크리스트 템플릿 목록")
    public ResponseEntity<List<ResponseCheckListTemplateDTO>> findAllByCondition(
            @RequestParam(value = "projectId", required = false) @Parameter(description = "프로젝트 PK") Long projectId,
            @RequestParam(value = "userId", required = false) @Parameter(description = "회원 PK") Long userId,
            @RequestParam(value = "name", required = false) @Parameter(description = "제목") String name,
            @RequestParam(value = "tag", required = false) @Parameter(description = "태그") String tag,
            @RequestParam(value = "visibled", required = false) @Parameter(description = "공개 여부") YN visibled,
            @RequestParam(value = "created_at_descended", required = false) @Parameter(description = "작성순") YN created_at_descended,
            @RequestParam(value = "views_descended", required = false) @Parameter(description = "조회순") YN views_descended,
            @RequestParam(value = "likes_descended", required = false) @Parameter(description = "좋아요순") YN likes_descended,
            @RequestParam(value = "detail_contents", required = false) @Parameter(description = "본문내용") String detail_contents,
            Pageable pageable, HttpServletRequest request) throws Exception {

        return
            new ResponseEntity<>(checkListTemplateService.findAllByCondition(projectId, userId, name, tag,
                created_at_descended, views_descended, likes_descended, detail_contents, pageable, request), OK);
    }

}