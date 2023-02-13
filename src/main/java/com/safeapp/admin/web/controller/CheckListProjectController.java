package com.safeapp.admin.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.dto.request.RequestCheckListProjectDTO;
import com.safeapp.admin.web.dto.request.RequestCheckListProjectModifyDTO;
import com.safeapp.admin.web.dto.response.ResponseCheckListProjectDTO;
import com.safeapp.admin.web.dto.response.ResponseCheckListProjectSelectDTO;
import com.safeapp.admin.utils.ResponseUtil;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.CheckListProject;
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

import com.safeapp.admin.web.service.CheckListProjectService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/checkList")
@AllArgsConstructor
@Api(tags = {"CheckListProject"}, description = "리스트 관리 > 체크리스트")
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
    @ApiOperation(value = "체크리스트 목록 조회", notes = "체크리스트 목록 조회")
    public ResponseEntity<List<ResponseCheckListProjectDTO>> findAll(
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

        Long count =
            checkListProjectService.countAllByCondition(keyword, userName, phoneNo, visibled, createdAtStart, createdAtEnd);
        List<ResponseCheckListProjectDTO> list =
            checkListProjectService.findAllByConditionAndOrderBy(keyword, userName, phoneNo,
            visibled, createdAtStart, createdAtEnd, createdAtDesc, likesDesc, viewsDesc,
            pageNo, pageSize, request);
        Pages pages = new Pages(pageNo, pageSize);

        ListResponse chkPrjList = new ListResponse(count, list, pages);
        return ResponseUtil.sendResponse(chkPrjList);
    }

}