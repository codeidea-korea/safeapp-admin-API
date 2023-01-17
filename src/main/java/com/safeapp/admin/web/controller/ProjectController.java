package com.safeapp.admin.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.data.DocumentType;
import com.safeapp.admin.web.data.StatusType;
import com.safeapp.admin.web.dto.request.RequestDetailModifyDTO;
import com.safeapp.admin.web.dto.request.RequestStatusChangeDTO;
import com.safeapp.admin.utils.ResponseUtil;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.Admins;
import com.safeapp.admin.web.model.entity.Project;
import com.safeapp.admin.web.model.entity.ProjectGroup;
import com.safeapp.admin.web.model.entity.Users;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.safeapp.admin.web.service.ProjectService;
import com.safeapp.admin.web.service.cmmn.JwtService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/project")
@Api(tags = {"Project"}, description = "프로젝트", basePath = "/")
public class ProjectController {

    private final ProjectService projectService;

    private final ProjectGroupService projectGroupService;

    private final JwtService jwtService;

    @Autowired
    public ProjectController(ProjectService projectService, ProjectGroupService projectGroupService,
        JwtService jwtService) {
        this.projectService = projectService;
        this.projectGroupService = projectGroupService;
        this.jwtService = jwtService;
    }

    @PostMapping(value = "/project")
    @ApiOperation(value = "등록", notes = "등록")
    public ResponseEntity add(
        @RequestBody Project params,
        HttpServletRequest request) throws Exception {
        return ResponseUtil.sendResponse(projectService.add(params, request));
    }

    @PutMapping(value = "/projects/{id}")
    @ApiOperation(value = "수정", notes = "수정")
    public ResponseEntity modify(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        @RequestBody Project params,
        HttpServletRequest request) throws Exception {

        params.setId(id);
        return ResponseUtil.sendResponse(projectService.edit(params, request));
    }

    @DeleteMapping(value = "/projects/{id}")
    @ApiOperation(value = "삭제", notes = "삭제")
    public ResponseEntity remove(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        projectService.remove(id, request);
        return ResponseUtil.sendResponse(null);
    }

    @GetMapping(value = "/projects/{id}")
    @ApiOperation(value = "조회 (단건)", notes = "조회 (단건)")
    public ResponseEntity find(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        return ResponseUtil.sendResponse(projectService.find(id, request));
    }

    @GetMapping(value = "/projects")
    @ApiOperation(value = "목록 조회 (다건)", notes = "목록 조회 (다건)")
    public ResponseEntity<ListResponse> findAll(
        Pages bfPage,
        HttpServletRequest request) throws Exception {
        return ResponseUtil.sendResponse(projectService.findAll(
            Project.builder()
                .build(),
            bfPage,
            request));
    }

    @GetMapping(value = "/projects/me")
    @ApiOperation(value = "나의 목록 조회 (다건)", notes = "나의 목록 조회 (다건)")
    public ResponseEntity<ListResponse> findAllByMe(
        Pages bfPage,
        HttpServletRequest request) throws Exception {
        Admins admin = jwtService.getAdminInfoByToken(request);
        
        ListResponse<ProjectGroup> groups = projectGroupService.findAll(ProjectGroup.builder()
            .admin(admin)
            .build(), new Pages(1, 100), request);
        
        List<Project> projects = new ArrayList<Project>();
        
        for(ProjectGroup group : groups.getList()) {
            Project project = projectService.find(group.getProject().getId(), request);
            projects.add(project);
        }
        
        return ResponseUtil.sendResponse(new ListResponse(projects.size(), projects, bfPage));
    }



    @PutMapping(value = "/projects/details")
    @ApiOperation(value = "문서 디테일 수정")
    public ResponseEntity modifyDetail(
            @RequestBody List<RequestDetailModifyDTO> dto,
            @RequestParam(value = "type") @Parameter(description = "문서타입") DocumentType documentType,
            HttpServletRequest request
            ) throws Exception{

        return new ResponseEntity(projectService.modifyDetails(dto, documentType), OK);
    }


    @PostMapping(value = "/projects/status-change/{id}")
    @ApiOperation(value = "문서 상태값 변경", notes = "문서 상태값 변경")
    public ResponseEntity changeStatus(
            @PathVariable("id") @ApiParam(value = "고유 아이디") Long id,
            @RequestParam(value = "type") @Parameter(description = "문서타입") DocumentType documentType,
            @RequestParam(value = "status") @Parameter(description = "상태값") StatusType status,
            @RequestParam(value = "recheck_reason", required = false) @Parameter(description = "재점검사유")String recheckReason,
            @RequestBody(required = false) RequestStatusChangeDTO dto,
            HttpServletRequest request
            ) throws Exception {
        Boolean result = projectService.changeStatus(id, documentType, status, recheckReason, dto, request);
        if(result){
            return new ResponseEntity(OK);
        }
        else{
            return new ResponseEntity(BAD_REQUEST);
        }
    }

    @PostMapping(value = "/projects/add-project/{id}")
    @ApiOperation(value = "템플릿으로 새 문서 생성", notes = "템플릿으로 새 문서 생성")
    public ResponseEntity addProject(
            @PathVariable("id") @ApiParam(value = "고유 아이디") Long id,
            @RequestParam(value = "document-type") @Parameter(description = "문서 타입") DocumentType documentType,
            @RequestParam(value = "project-id", required = false) @Parameter(description = "프로젝트 아이디") Long projectId,
            HttpServletRequest request) throws Exception{
        Long resultId = projectService.addProject(id, projectId, documentType);
        return new ResponseEntity(resultId, OK);
    }

    @PostMapping(value = "/projects/add-project/{project-id}/documents/{document-id}")
    @ApiOperation(value = "프로젝트 문서 새로 생성", notes = "프로젝트 문서 새로 생성")
    public ResponseEntity addNewProject(
            @PathVariable("project-id") @ApiParam(value = "프로젝트 아이디") Long projectId,
            @PathVariable("document-id") @ApiParam(value = "문서 아이디") Long documentId,
            @RequestParam(value = "document-type") @Parameter(description = "문서 타입") DocumentType documentType,
            HttpServletRequest request) throws Exception{
        Long resultId = projectService.addNewProject(projectId, documentId, documentType);
        return new ResponseEntity(resultId, OK);
    }

    @GetMapping(value = "/projects/subject/details")
    @ApiOperation(value = "체크리스트 or 위험성 리스트의 detail", notes = "")
    public ResponseEntity abc(
            @RequestParam(value = "document-type") @Parameter(description = "문서 타입") DocumentType documentType,
            @RequestParam(value = "체크리스트 id or 위험리스트 id") long id,
            HttpServletRequest request) throws Exception {

        Object body = null;

        if(documentType == DocumentType.CHECKLIST){
            body = projectService.findAllChecklistDetails(id);
        }if(documentType == DocumentType.RISKCHECK){
            body = projectService.findAllRiskCheckDetails(id);
        }

        return ResponseEntity.ok().body(body);
    }
}
