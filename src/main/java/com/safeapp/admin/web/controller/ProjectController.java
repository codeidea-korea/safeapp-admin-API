package com.safeapp.admin.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.utils.ResponseUtil;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.Project;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.safeapp.admin.web.service.ProjectGroupService;
import com.safeapp.admin.web.service.ProjectService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/project")
@AllArgsConstructor
@Api(tags = {"Project"}, description = "프로젝트")
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectGroupService projectGroupService;

    @GetMapping(value = "/find/{id}")
    @ApiOperation(value = "프로젝트 확인", notes = "프로젝트 확인")
    public ResponseEntity find(@PathVariable("id") @ApiParam(value = "프로젝트 PK", required = true) long id,
                               HttpServletRequest request) throws Exception {

        return ResponseUtil.sendResponse(projectService.find(id, request));
    }

    @PutMapping(value = "/edit/{id}")
    @ApiOperation(value = "프로젝트 수정", notes = "프로젝트 수정")
    public ResponseEntity edit(@PathVariable("id") @ApiParam(value = "프로젝트 PK", required = true) long id,
                               @RequestBody Project project, HttpServletRequest request) throws Exception {

        project.setId(id);
        return ResponseUtil.sendResponse(projectService.edit(project, request));
    }

    @DeleteMapping(value = "/remove/{id}")
    @ApiOperation(value = "프로젝트 삭제", notes = "프로젝트 삭제")
    public ResponseEntity remove(@PathVariable("id") @ApiParam(value = "프로젝트 PK") long id,
                                 HttpServletRequest request) throws Exception {

        projectService.remove(id, request);
        return ResponseUtil.sendResponse(null);
    }

    @GetMapping(value = "/list")
    @ApiOperation(value = "프로젝트 목록", notes = "프로젝트 목록")
    public ResponseEntity<ListResponse> findAll(Pages pages, HttpServletRequest request) throws Exception {
        return ResponseUtil.sendResponse(
                projectService.findAll(
                    Project.builder()
                        .build(),
                pages,
                request));
    }

    @GetMapping(value = "/group/list/{id}")
    @ApiOperation(value = "특정 프로젝트에 속한 그룹원 목록 조회", notes = "특정 프로젝트에 속한 그룹원 목록 조회")
    public ResponseEntity<ListResponse> findAllGroup(@PathVariable("id") @ApiParam(value = "프로젝트 PK", required = true) long id,
                                                     Pages bfPage, HttpServletRequest request) throws Exception {

        /*
        Users user = jwtService.getUserInfoByToken(request);
        ListResponse<ProjectGroup> groups = projectGroupService.findAll(ProjectGroup.builder()
                .user(user)
                .build(), new Pages(1, 100), request);

        List<Project> projects = new ArrayList<>();

        for(ProjectGroup group : groups.getList()) {
            Project project = projectService.find(group.getProject().getId(), request);
            projects.add(project);
        }

        return ResponseUtil.sendResponse(new ListResponse(projects.size(), projects, bfPage));
        */

        return null;
    }

    @GetMapping(value = "/template/list/{id}")
    @ApiOperation(value = "특정 프로젝트에 속한 템플릿 목록 조회", notes = "특정 프로젝트에 속한 템플릿 목록 조회")
    public ResponseEntity<ListResponse> findAllTemplate(@PathVariable("id") @ApiParam(value = "프로젝트 PK", required = true) long id,
                                                        Pages pages, HttpServletRequest request) throws Exception {

        /*
        Users user = jwtService.getUserInfoByToken(request);

        ListResponse<ProjectGroup> groups = projectGroupService.findAll(ProjectGroup.builder()
                .user(user)
                .build(), new Pages(1, 100), request);

        List<Project> projects = new ArrayList<>();

        for(ProjectGroup group : groups.getList()) {
            Project project = projectService.find(group.getProject().getId(), request);
            projects.add(project);
        }
        */

        /*
        List<Project> projects = new ArrayList<>();
        return ResponseUtil.sendResponse(
                userService.findAll(
                        Users.builder()
                                .userId("%" + userId + "%")
                                .userName("%" + userName + "%")
                                .email("%" + email + "%")
                                .build(),
                        pages,
                        request));
        */

        return null;
    }

}