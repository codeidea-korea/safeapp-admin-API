package com.safeapp.admin.web.service.impl;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.utils.DateUtil;
import com.safeapp.admin.web.dto.request.RequestProjectGroupEditDTO;
import com.safeapp.admin.web.dto.response.ResponseProjectGroupDTO;
import com.safeapp.admin.web.model.docs.InviteHistory;
import com.safeapp.admin.web.model.entity.*;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.repos.direct.DirectQuery;
import com.safeapp.admin.web.repos.jpa.*;
import com.safeapp.admin.web.repos.mongo.InviteHistoryRepos;
import com.safeapp.admin.web.service.CheckListProjectService;
import com.safeapp.admin.web.service.RiskCheckService;
import com.safeapp.admin.web.service.cmmn.DirectSendAPIService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.safeapp.admin.web.service.ProjectService;

@Service
@AllArgsConstructor
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepos prjRepos;
    private final ProjectGroupRepos prjGrRepos;
    private final CheckListProjectRepository chkPrjRepos;
    private final RiskCheckRepository riskChkRepos;
    private final UserRepos userRepos;
    private final InviteHistoryRepos ivtHstRepos;
    private final DirectQuery dirRepos;

    private final DateUtil dateUtil;

    private final DirectSendAPIService directSendAPIService;

    @Transactional
    @Override
    public Project add(Project newProject, HttpServletRequest request) throws Exception {
        if(Objects.isNull(newProject)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 프로젝트입니다.");
        }

        newProject.setDeleteYn(false);

        Project addedProject = prjRepos.save(newProject);
        if(Objects.isNull(addedProject)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "DB 저장 중 오류가 발생하였습니다.");
        }

        Users user =
            userRepos.findById(newProject.getUserId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다."));

        ProjectGroup newPrjGr =
                ProjectGroup
                .builder()
                .userAuthType("TEAM_MASTER")
                .project(addedProject)
                .user(user)
                .name(addedProject.getName())
                .build();
        prjGrRepos.save(newPrjGr);

        return addedProject;
    }

    @Override
    public Project find(long id, HttpServletRequest request) throws Exception {
        Project project =
            prjRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 프로젝트입니다."));
        if(project.getDeleteYn() == true) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 프로젝트입니다.");
        }

        return project;
    }

    @Override
    public Project edit(Project newProject, HttpServletRequest request) throws Exception {
        Project project =
            prjRepos.findById(newProject.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 프로젝트입니다."));

        project.edit(newProject);

        Project editedProject = prjRepos.save(project);
        return editedProject;
    }

    public InviteHistory generate(InviteHistory newIvtHst) {
        return
            InviteHistory.builder()
            .id(newIvtHst.getId())
            .createdAt(newIvtHst.getCreatedAt() == null ? dateUtil.getThisTime() : newIvtHst.getCreatedAt())
            .groupId(newIvtHst.getGroupId())
            .groupName(newIvtHst.getGroupName())
            .userMail(newIvtHst.getUserMail())
            .contents(newIvtHst.getContents())
            .urlData(newIvtHst.getUrlData())
            .efectiveEndAt(newIvtHst.getEfectiveEndAt())
            .build();
    }

    @Transactional
    @Override
    public InviteHistory addAllGroup(InviteHistory newIvtHst, HttpServletRequest request) throws Exception {
        if(Objects.isNull(newIvtHst)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 초대입니다.");
        }

        InviteHistory addedIvtHst = ivtHstRepos.save(generate(newIvtHst));
        if(Objects.isNull(addedIvtHst)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "DB 저장 중 오류가 발생하였습니다.");
        }

        Map<String, String> bodyMap = new HashMap<>();

        bodyMap.put("name", "인증자");
        bodyMap.put("mailAddress", addedIvtHst.getUserMail());
        bodyMap.put("subject", "SAFFY에 초대합니다.");
        bodyMap.put("body", URLEncoder.encode("안녕하세요, 컨스퀘어에 초대합니다. <a href=\"https://gosaffy.com/login?code=" + addedIvtHst.getUrlData() + "\">가입하기</a>", "UTF-8"));

        directSendAPIService.sendMail(newIvtHst.getUserMail(), bodyMap);

        return addedIvtHst;
    }

    @Override
    public void editAllGroup(List<RequestProjectGroupEditDTO> prjGrEditList, HttpServletRequest request) {
        for(int i = 0; i < prjGrEditList.size(); i++) {
            ProjectGroup prjGr =
                prjGrRepos.findById(prjGrEditList.get(i).getId())
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 프로젝트 그룹원입니다."));
            prjGr.setId(prjGrEditList.get(i).getId());
            prjGr.setUserAuthType(prjGrEditList.get(i).getUserAuthType());

            if(prjGrEditList.get(i).getDeleteYn() == false) {
                prjGrRepos.save(prjGr);
            } else {
                prjGr.setDeleteYn(true);
                prjGrRepos.save(prjGr);
            }
        }
    }

    @Override
    public void removeGroup(long id, HttpServletRequest request) {
        ProjectGroup prjGr =
            prjGrRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 프로젝트 그룹원입니다."));

        prjGr.setDeleteYn(true);
        prjGrRepos.save(prjGr);
    }

    @Override
    public long countDocList(long id, String userName, String name, HttpServletRequest request) { return dirRepos.countDocList(id, userName, name); }

    @Override
    public List<Map<String, Object>> findDocList(long id, String userName, String name, int pageNo, int pageSize, HttpServletRequest request) {

        return dirRepos.findDocList(id, userName, name, pageNo, pageSize);
    }

    @Override
    public void removeDoc(String docType, long id, HttpServletRequest request) {
        switch(docType) {
            case "checkList":
                CheckListProject chkPrj =
                    chkPrjRepos.findById(id).orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 프로젝트 문서입니다."));

                chkPrj.setDeleteYn(true);
                chkPrjRepos.save(chkPrj);

                break;
            case "riskCheck":
                RiskCheck riskChk =
                    riskChkRepos.findById(id).orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 프로젝트 문서입니다."));

                riskChk.setDeleteYn(true);
                riskChkRepos.save(riskChk);
                
                break;
            default:
                throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 프로젝트 문서 타입입니다.");
        }
    }

    @Override
    public List<ResponseProjectGroupDTO> findAllGroupByCondition(long id, int pageNo, int pageSize, HttpServletRequest request) {

        return prjGrRepos.findAllById(id, pageNo, pageSize, request);
    }

    @Override
    public void remove(long id, HttpServletRequest request) {
        Project project =
            prjRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 프로젝트입니다."));

        project.setDeleteYn(true);
        prjRepos.save(project);
    }

    @Override
    public long countProjectList(String name, String userName, String createdAtStart, String createdAtEnd, HttpServletRequest request) {

        return dirRepos.countProjectList(name, userName, createdAtStart, createdAtEnd);
    }

    @Override
    public List<Map<String, Object>> findProjectList(String name, String userName, String createdAtStart, String createdAtEnd,
            int pageNo, int pageSize, HttpServletRequest request) {

        return dirRepos.findProjectList(name, userName, createdAtStart, createdAtEnd, pageNo, pageSize);
    }

    @Override
    public Project generate(Project oldProject) { return null; }

    @Override
    public ListResponse<Project> findAll(Project project, Pages pages, HttpServletRequest request) throws Exception {

        return null;
    }

}