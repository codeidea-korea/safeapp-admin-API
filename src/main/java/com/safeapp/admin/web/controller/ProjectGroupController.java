package com.safeapp.admin.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.utils.ResponseUtil;
import com.safeapp.admin.web.model.entity.Admins;
import com.safeapp.admin.web.model.entity.Project;
import com.safeapp.admin.web.model.entity.Users;
import com.safeapp.admin.web.service.ProjectService;
import com.safeapp.admin.web.service.cmmn.JwtService;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.ProjectGroup;
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

import com.safeapp.admin.web.service.ProjectGroupService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/project/group")
@AllArgsConstructor
@Api(tags = {"ProjectGroup"}, description = "프로젝트 그룹원")
public class ProjectGroupController {

    private final ProjectGroupService projectGroupService;

    @PostMapping(value = "/add")
    @ApiOperation(value = "그룹원 등록", notes = "그룹원 등록")
    public ProjectGroup add(@RequestBody ProjectGroup prjGr, HttpServletRequest request) throws Exception {

        return projectGroupService.add(prjGr, request);
    }

    @GetMapping(value = "/find/{id}")
    @ApiOperation(value = "그룹원 확인", notes = "그룹원 확인")
    public ProjectGroup find(@PathVariable("id") @ApiParam(value = "그룹원 PK", required = true) long id,
                             HttpServletRequest request) throws Exception {

        return projectGroupService.find(id, request);
    }

    @PutMapping(value = "/edit/{id}")
    @ApiOperation(value = "그룹원 권한 수정", notes = "그룹원 권한 수정")
    public ProjectGroup edit(@PathVariable("id") @ApiParam(value = "그룹원 PK", required = true) long id,
                             @RequestBody ProjectGroup prjGr, HttpServletRequest request) throws Exception {

        prjGr.setId(id);
        return projectGroupService.edit(prjGr, request);
    }

    @DeleteMapping(value = "/remove/{id}")
    @ApiOperation(value = "그룹원 삭제", notes = "그룹원 삭제")
    public ResponseEntity remove(@PathVariable("id") @ApiParam(value = "그룹원 PK", required = true) long id,
                                 HttpServletRequest request) throws Exception {

        projectGroupService.remove(id, request);
        return ResponseUtil.sendResponse(null);
    }

    @GetMapping(value = "/list")
    @ApiOperation(value = "그룹원 목록", notes = "그룹원 목록")
    public ResponseEntity<ListResponse> findAll(@RequestParam(value = "project", defaultValue = "0") Long project,
            Pages pages, HttpServletRequest request) throws Exception {

        return ResponseUtil.sendResponse(
                projectGroupService.findAll(
                        ProjectGroup.builder()
                            .project(project)
                            .build(),
                        pages,
                        request));
    }

}