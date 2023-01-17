package com.safeapp.admin.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.utils.ResponseUtil;
import com.safeapp.admin.web.model.cmmn.Pages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safeapp.admin.web.service.ProjectManagersService;
import com.safeapp.admin.web.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api(tags = {"ProjectManagers"}, description = "프로젝트 관리자", basePath = "/")
public class ProjectManagersController {

    private final ProjectManagersService projectManagersService;

    private final UserService userService;

    @Autowired
    public ProjectManagersController(ProjectManagersService projectManagersService, UserService userService) {
        this.projectManagersService = projectManagersService;
        this.userService = userService;
    }

    /*
    @PostMapping(value = "/project/manager")
    @ApiOperation(value = "등록", notes = "등록")
    public ResponseEntity add(
        @RequestBody ProjectManagers params,
        HttpServletRequest request) throws Exception {
        return ResponseUtil.sendResponse(projectManagersService.add(params, request));
    }
    */
    // 이미 초대된 회원 기준 매니저 초대
    @PostMapping(value = "/project/manager")
    @ApiOperation(value = "등록", notes = "등록")
    public ResponseEntity add(
        @RequestParam(value = "project_id", required = true) long project_id,
        @RequestParam(value = "user_ids", required = true) List<String> user_ids,
        HttpServletRequest request) throws Exception {

//        for (String user_id : user_ids) {
//            Users user = userService.findByUserID(user_id);
//
//            projectManagersService.add(ProjectManager.builder()
//                .projectId(project_id)
//                .userId(user.getId())
//                .build(), request);
//        }

        return ResponseUtil.sendResponse(true);
    }

    @PutMapping(value = "/project/manager/{id}")
    @ApiOperation(value = "수정", notes = "수정")
    public ResponseEntity modify(
        @RequestParam(value = "project_id", required = true) long project_id,
        @RequestParam(value = "user_ids", required = true) List<String> user_ids,
        HttpServletRequest request) throws Exception {

        projectManagersService.removeByProjectId(project_id, request);
        
//        for (String user_id : user_ids) {
//            Users user = userService.findByUserID(user_id);
//
//            projectManagersService.add(ProjectManager.builder()
//                .projectId(project_id)
//                .userId(user.getId())
//                .build(), request);
//        }
        return ResponseUtil.sendResponse(true);
    }

    @DeleteMapping(value = "/project/manager/{id}")
    @ApiOperation(value = "삭제", notes = "삭제")
    public ResponseEntity remove(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        projectManagersService.remove(id, request);
        return ResponseUtil.sendResponse(null);
    }

    @GetMapping(value = "/project/managers/{id}")
    @ApiOperation(value = "조회 (단건)", notes = "조회 (단건)")
    public ResponseEntity find(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        return ResponseUtil.sendResponse(projectManagersService.find(id, request));
    }

    @GetMapping(value = "/projects/{projectId}/managers")
    @ApiOperation(value = "목록 조회 (다건)", notes = "목록 조회 (다건)")
    public ResponseEntity findAllByProjectId(
        Pages bfPage,
        @RequestParam(value = "projectId", required = false, defaultValue = "1") long projectId,
        HttpServletRequest request) throws Exception {
//        return ResponseUtil.sendResponse(projectManagersService.findAll(
//                ProjectManager.builder()
//                .projectId(projectId)
//                .build(),
//            bfPage,
//            request));
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/project/managers")
    @ApiOperation(value = "목록 조회 (다건)", notes = "목록 조회 (다건)")
    public ResponseEntity findAll(
        Pages bfPage,
        HttpServletRequest request) throws Exception {
//        return ResponseUtil.sendResponse(projectManagersService.findAll(
//                ProjectManager.builder()
//                .build(),
//            bfPage,
//            request));
        return new ResponseEntity(HttpStatus.OK);
    }
}
