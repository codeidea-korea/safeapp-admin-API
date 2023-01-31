package com.safeapp.admin.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.utils.ResponseUtil;
import com.safeapp.admin.web.dto.response.ResponseProjectGroupDTO;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.Project;
import com.safeapp.admin.web.model.entity.Users;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.safeapp.admin.web.service.ProjectService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/project")
@AllArgsConstructor
@Api(tags = {"Project"}, description = "프로젝트")
@Slf4j
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping(value = "/find/{id}")
    @ApiOperation(value = "프로젝트 단독 조회", notes = "프로젝트 단독 조회")
    public ResponseEntity find(@PathVariable("id") @ApiParam(value = "프로젝트 PK", required = true) long id,
            HttpServletRequest request) throws Exception {

        return ResponseUtil.sendResponse(projectService.find(id, request));
    }

    @PutMapping(value = "/edit/{id}")
    @ApiOperation(value = "프로젝트 수정", notes = "프로젝트 수정")
    public ResponseEntity edit(@PathVariable("id") @ApiParam(value = "프로젝트 PK", required = true) long id,
            @RequestBody Project project, HttpServletRequest request) throws Exception {
        log.error("project: {}", project);
        project.setId(id);
        return ResponseUtil.sendResponse(projectService.edit(project, request));
    }

    @GetMapping(value = "/find/{id}/groupList")
    @ApiOperation(value = "프로젝트에 그룹원 목록 조회", notes = "프로젝트에 그룹원 목록 조회")
    public ResponseEntity<List<ResponseProjectGroupDTO>> findAllGroupByCondition(
            @PathVariable("id") @ApiParam(value = "프로젝트 PK", required = true) long id,
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            HttpServletRequest request) throws Exception {

        return new ResponseEntity<>(projectService.findAllGroupByCondition(id, pageNo, pageSize, request), OK);
    }

    @DeleteMapping(value = "/remove/group/{id}")
    @ApiOperation(value = "프로젝트 그룹원 삭제", notes = "프로젝트 그룹원 삭제")
    public ResponseEntity removeGroup(@PathVariable("id") @ApiParam(value = "프로젝트 그룹원 PK") long id,
            HttpServletRequest request) throws Exception {

        projectService.removeGroup(id, request);
        return ResponseUtil.sendResponse(null);
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
    public ResponseEntity<ListResponse> findAll(Pages pages, HttpServletRequest request) throws Exception {

        return ResponseUtil.sendResponse(projectService.findAll(Project.builder().build(), pages, request));
    }

}