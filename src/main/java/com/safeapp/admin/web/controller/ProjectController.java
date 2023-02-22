package com.safeapp.admin.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.utils.ResponseUtil;
import com.safeapp.admin.web.data.ProjectType;
import com.safeapp.admin.web.dto.request.RequestProjectGroupEditDTO;
import com.safeapp.admin.web.dto.response.ResponseProjectGroupDTO;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.docs.InviteHistory;
import com.safeapp.admin.web.model.entity.Project;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.safeapp.admin.web.service.ProjectService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.Base64;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/project")
@AllArgsConstructor
@Api(tags = {"Project"}, description = "프로젝트 관리")
@Slf4j
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping(value = "/add")
    @ApiOperation(value = "프로젝트 등록", notes = "프로젝트 등록")
    public ResponseEntity add(@RequestBody Project newProject, HttpServletRequest request) throws Exception {

        return ResponseUtil.sendResponse(projectService.add(newProject, request));
    }

    @GetMapping(value = "/find/{id}")
    @ApiOperation(value = "프로젝트 단독 조회", notes = "프로젝트 단독 조회")
    public ResponseEntity find(@PathVariable("id") @ApiParam(value = "프로젝트 PK", required = true) long id,
            HttpServletRequest request) throws Exception {

        return ResponseUtil.sendResponse(projectService.find(id, request));
    }

    @PutMapping(value = "/edit/{id}")
    @ApiOperation(value = "프로젝트 수정", notes = "프로젝트 수정")
    public ResponseEntity edit(@PathVariable("id") @ApiParam(value = "프로젝트 PK", required = true) long id,
            @RequestBody Project newProject, HttpServletRequest request) throws Exception {
        newProject.setId(id);
        return ResponseUtil.sendResponse(projectService.edit(newProject, request));
    }

    @PostMapping(value = "/group/addList")
    @ApiOperation(value = "프로젝트 그룹원 초대", notes = "프로젝트 그룹원 초대")
    public ResponseEntity addAllGroup(
            @RequestParam(value = "id") long id,
            @RequestParam(value = "emails") List<String> emails,
            @RequestParam(value = "content") String content,
            HttpServletRequest request) throws Exception {

        for(String email : emails) {
            InviteHistory newIvtHst =
                InviteHistory.builder()
                .groupId(id)
                .groupName(id + "")
                .userMail(email)
                .contents(content)
                .urlData(Base64.getEncoder().encodeToString((id + "-" + email + "-type:mail").getBytes()))
                .build();

            projectService.addAllGroup(newIvtHst, request);
        }

        return ResponseUtil.sendResponse(true);
    }

    @PutMapping(value = "/group/editList")
    @ApiOperation(value = "프로젝트 그룹원 수정", notes = "프로젝트 그룹원 수정")
    public ResponseEntity editAllGroup(@RequestBody List<RequestProjectGroupEditDTO> prjGrEditList, HttpServletRequest request) throws Exception {
        projectService.editAllGroup(prjGrEditList, request);
        return ResponseUtil.sendResponse(null);
    }

    /*
    @DeleteMapping(value = "/remove/group/{id}")
    @ApiOperation(value = "프로젝트 그룹원 삭제", notes = "프로젝트 그룹원 삭제")
    public ResponseEntity removeGroup(@PathVariable("id") @ApiParam(value = "프로젝트 그룹원 PK") long id,
            HttpServletRequest request) throws Exception {

        projectService.removeGroup(id, request);
        return ResponseUtil.sendResponse(null);
    }
    */

    @GetMapping(value = "/find/{id}/doc/list")
    @ApiOperation(value = "프로젝트 문서 목록 조회", notes = "프로젝트 문서 목록 조회")
    public ResponseEntity<List<ResponseProjectGroupDTO>> findAllDoc(
            @PathVariable("id") @ApiParam(value = "프로젝트 PK", required = true) long id,
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            HttpServletRequest request) throws Exception {

        long count = projectService.countDocList(id, request);
        List<Map<String, Object>> list = projectService.findDocList(id, pageNo, pageSize, request);
        Pages pages = new Pages(pageNo, pageSize);

        ListResponse docListResponse = new ListResponse(count, list, pages);
        return ResponseUtil.sendResponse(docListResponse);
    }

    @GetMapping(value = "/find/{id}/group/list")
    @ApiOperation(value = "프로젝트 그룹원 목록 조회", notes = "프로젝트 그룹원 목록 조회")
    public ResponseEntity<List<ResponseProjectGroupDTO>> findAllGroupByCondition(
            @PathVariable("id") @ApiParam(value = "프로젝트 PK", required = true) long id,
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            HttpServletRequest request) throws Exception {

        return new ResponseEntity<>(projectService.findAllGroupByCondition(id, pageNo, pageSize, request), OK);
    }

    @DeleteMapping(value = "/remove/{id}")
    @ApiOperation(value = "프로젝트 삭제", notes = "프로젝트 삭제")
    public ResponseEntity remove(@PathVariable("id") @ApiParam(value = "프로젝트 PK") long id,
            HttpServletRequest request) throws Exception {

        projectService.remove(id, request);
        return ResponseUtil.sendResponse(null);
    }

    @GetMapping(value = "/list")
    @ApiOperation(value = "프로젝트 목록 조회", notes = "프로젝트 목록 조회")
    public ResponseEntity<List<Map<String, Object>>> findAll(
            @RequestParam(value = "name", required = false, defaultValue = "") String name,
            @RequestParam(value = "userName", required = false, defaultValue = "") String userName,
            // 멤버쉽 검색 관련은 주석 처리함
            /*
            @RequestParam(value = "orderType", required = false, defaultValue = "") String orderType,
            @RequestParam(value = "status", required = false, defaultValue = "") String status,
            */
            @RequestParam(value = "createdAtStart", required = false, defaultValue = "") String createdAtStart,
            @RequestParam(value = "createdAtEnd", required = false, defaultValue = "") String createdAtEnd,
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            HttpServletRequest request) throws Exception {

        // 멤버쉽 검색 관련은 주석 처리함
        /*
        long count =
            projectService.countProjectList(name, userName, orderType, status, createdAtStart,
            createdAtEnd, request);
        List<Map<String, Object>> list =
            projectService.findProjectList(name, userName, orderType, status, createdAtStart,
            createdAtEnd, pageNo, pageSize, request);
        */
        long count =
            projectService.countProjectList(name, userName, createdAtStart, createdAtEnd, request);
        List<Map<String, Object>> list =
            projectService.findProjectList(name, userName, createdAtStart, createdAtEnd, pageNo, pageSize, request);
        Pages pages = new Pages(pageNo, pageSize);

        ListResponse projectListResponse = new ListResponse(count, list, pages);
        return ResponseUtil.sendResponse(projectListResponse);
    }

}