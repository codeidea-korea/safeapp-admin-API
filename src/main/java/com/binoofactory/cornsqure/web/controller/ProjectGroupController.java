package com.binoofactory.cornsqure.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.binoofactory.cornsqure.web.model.entity.Project;
import com.binoofactory.cornsqure.web.model.entity.Users;
import com.binoofactory.cornsqure.web.service.ProjectService;
import com.binoofactory.cornsqure.web.service.cmmn.UserJwtService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.binoofactory.cornsqure.web.model.entity.ProjectGroup;
import com.binoofactory.cornsqure.web.service.ProjectGroupService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@AllArgsConstructor
@RestController
@RequestMapping("/api/projectGroup")
@Api(tags = {"ProjectGroup"}, description = "프로젝트 그룹", basePath = "/api/projectGroup")
public class ProjectGroupController {

    private final ProjectGroupService projectGroupService;
    private final ProjectService projectService;
    private final UserJwtService userJwtService;

    @PostMapping(value = "")
    @ApiOperation(value = "등록", notes = "등록")
    public ProjectGroup add(
        @RequestBody ProjectGroup params,
        HttpServletRequest request) throws Exception {
        return projectGroupService.add(params, request);
    }

    @PutMapping(value = "/{id}")
    @ApiOperation(value = "수정", notes = "수정")
    public ProjectGroup modify(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        @RequestBody ProjectGroup params,
        HttpServletRequest request) throws Exception {

        params.setId(id);
        return projectGroupService.edit(params, request);
    }

    @DeleteMapping(value = "")
    @ApiOperation(value = "삭제", notes = "삭제")
    public void remove(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        projectGroupService.remove(id, request);
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "조회 (단건)", notes = "조회 (단건)")
    public ProjectGroup find(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        return projectGroupService.find(id, request);
    }

    @GetMapping(value = "")
    @ApiOperation(value = "목록 조회 (다건)", notes = "목록 조회 (다건)")
    public BfListResponse findAll(
        BfPage bfPage,
        @RequestParam(value = "userId", required = false, defaultValue = "1") Long userId,
        @RequestParam(value = "projectId", required = false, defaultValue = "1") Long projectId,
        HttpServletRequest request) throws Exception {

        Users user = userJwtService.getUserInfoByToken(request);
        Project project = projectService.find(projectId, request);
        return projectGroupService.findAll(
            ProjectGroup.builder()
            .user(user)
            .project(project)
            .build(),
            bfPage,
            request);
    }
}
