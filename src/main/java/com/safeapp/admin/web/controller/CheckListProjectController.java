package com.safeapp.admin.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.data.StatusType;
import com.safeapp.admin.web.dto.request.RequestCheckListProjectDTO;
import com.safeapp.admin.web.dto.request.RequestCheckListProjectModifyDTO;
import com.safeapp.admin.web.dto.response.ResponseCheckListProjectDTO;
import com.safeapp.admin.web.dto.response.ResponseCheckListProjectSelectDTO;
import com.safeapp.admin.utils.ResponseUtil;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.response.ResponseCheckListTemplateDTO;
import com.safeapp.admin.web.model.entity.CheckListProject;
import com.safeapp.admin.web.model.entity.Users;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
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

import com.safeapp.admin.web.service.CheckListProjectService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/checkList")
@AllArgsConstructor
@Api(tags = {"CheckListProject"}, description = "체크리스트")
public class CheckListProjectController {

    private final CheckListProjectService checkListProjectService;

    @PostMapping(value = "/add")
    @ApiOperation(value = "체크리스트 등록", notes = "체크리스트 등록")
    public ResponseEntity<ResponseCheckListProjectDTO> add(@RequestBody RequestCheckListProjectDTO addDto,
            HttpServletRequest request) throws Exception {

        CheckListProject addedChkPrj = checkListProjectService.add(checkListProjectService.toEntity(addDto), request);
        return new ResponseEntity<>(ResponseCheckListProjectDTO.builder().checkListProject(addedChkPrj).build(), OK);
    }

    @GetMapping(value = "/find/{id}")
    @ApiOperation(value = "체크리스트 단독 조회", notes = "체크리스트 단독 조회")
    public ResponseEntity<ResponseCheckListProjectSelectDTO> find(@PathVariable("id") @ApiParam(value = "체크리스트 PK", required = true) long id,
            HttpServletRequest request) throws Exception {

        CheckListProject oldChkPrj = checkListProjectService.find(id, request);
        return new ResponseEntity<>(ResponseCheckListProjectSelectDTO.builder().checkListProject(oldChkPrj).build(), OK);
    }

    @PutMapping(value = "/edit/{id}")
    @ApiOperation(value = "체크리스트 수정", notes = "체크리스트 수정")
    public ResponseEntity<ResponseCheckListProjectDTO> edit(@PathVariable("id") @ApiParam(value = "체크리스트 PK", required = true) long id,
            @RequestBody RequestCheckListProjectModifyDTO modifyDto, HttpServletRequest request) throws Exception {

        CheckListProject chkPrj = checkListProjectService.toEntityModify(modifyDto);
        chkPrj.setId(id);

        CheckListProject editedChkPrj = checkListProjectService.edit(chkPrj, request);
        return new ResponseEntity<>(ResponseCheckListProjectDTO.builder().checkListProject(editedChkPrj).build(), OK);
    }

    @DeleteMapping(value = "/remove/{id}")
    @ApiOperation(value = "체크리스트 삭제", notes = "체크리스트 삭제")
    public ResponseEntity remove(@PathVariable("id") @ApiParam(value = "체크리스트 PK", required = true) long id,
            HttpServletRequest request) throws Exception {

        checkListProjectService.remove(id, request);
        return ResponseUtil.sendResponse(null);
    }

    @GetMapping(value = "/list")
    @ApiOperation(value = "체크리스트 목록", notes = "체크리스트 목록")
    public ResponseEntity<List<ResponseCheckListProjectDTO>> findAllByCondition(
        @RequestParam(value = "tag", required = false) @Parameter(description = "키워드") String tag,
        @RequestParam(value = "visibled", required = false) @Parameter(description = "공개상태") YN visibled,
        @RequestParam(value = "created_at_descended", required = false) @Parameter(description = "최신순") YN created_at_descended,
        @RequestParam(value = "likes_descended", required = false) @Parameter(description = "좋아요순") YN likes_descended,
        @RequestParam(value = "views_descended", required = false) @Parameter(description = "조회순") YN views_descended,
        Pageable pageable, HttpServletRequest request) throws Exception {

        return
            new ResponseEntity<>(checkListProjectService.findAllByCondition(tag, visibled, created_at_descended,
                views_descended, likes_descended, pageable, request), OK);
    }

}