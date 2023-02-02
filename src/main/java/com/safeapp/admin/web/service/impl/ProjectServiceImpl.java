package com.safeapp.admin.web.service.impl;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.utils.DateUtil;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.request.RequestProjectGroupEditDTO;
import com.safeapp.admin.web.dto.response.ResponseCheckListProjectDTO;
import com.safeapp.admin.web.dto.response.ResponseProjectGroupDTO;
import com.safeapp.admin.web.model.docs.InviteHistory;
import com.safeapp.admin.web.model.entity.*;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.repos.jpa.ProjectGroupRepos;
import com.safeapp.admin.web.repos.mongo.InviteHistoryRepos;
import com.safeapp.admin.web.service.cmmn.DirectSendAPIService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.safeapp.admin.web.repos.jpa.ProjectRepos;
import com.safeapp.admin.web.repos.jpa.dsl.ProjectDslRepos;
import com.safeapp.admin.web.service.ProjectService;

@Service
@AllArgsConstructor
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepos prjRepos;
    private final ProjectDslRepos prjDslRepos;
    private final InviteHistoryRepos ivtHstRepos;
    private final ProjectGroupRepos prjGrRepos;
    private final DateUtil dateUtil;
    private final DirectSendAPIService directSendAPIService;

    @Override
    public Project find(long id, HttpServletRequest request) throws Exception {
        Project oldProject =
            prjRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 프로젝트입니다."));
        if(oldProject.getDeleteYn() == true) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 프로젝트입니다.");
        }

        return oldProject;
    }

    @Override
    public Project edit(Project project, HttpServletRequest request) throws Exception {
        Project oldProject =
            prjRepos.findById(project.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 프로젝트입니다."));

        oldProject.edit(project);

        Project editedProject = prjRepos.save(oldProject);
        return editedProject;
    }

    public InviteHistory generate(InviteHistory ivtHst) {
        return
            InviteHistory.builder()
            .id(ivtHst.getId())
            .contents(ivtHst.getContents())
            .groupId(ivtHst.getGroupId())
            .groupName(ivtHst.getGroupName())
            .urlData(ivtHst.getUrlData())
            .userMail(ivtHst.getUserMail())
            .efectiveEndAt(ivtHst.getEfectiveEndAt())
            .createdAt(ivtHst.getCreatedAt() == null ? dateUtil.getThisTime() : ivtHst.getCreatedAt())
            .build();
    }

    @Transactional
    @Override
    public InviteHistory addAllGroup(InviteHistory ivtHst, HttpServletRequest request) throws Exception {
        if(Objects.isNull(ivtHst)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }

        InviteHistory addedIvtHst = ivtHstRepos.save(generate(ivtHst));
        if(Objects.isNull(addedIvtHst)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "DB 저장 중 오류가 발생하였습니다.");
        }

        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("name", "인증자");
        bodyMap.put("mailAddress", addedIvtHst.getUserMail());
        bodyMap.put("subject", "인증번호입니다.");
        bodyMap.put("body", URLEncoder.encode("안녕하세요, 컨스퀘어에 초대합니다. <a href=\"https://safeapp.codeidea.io/login?code=" + addedIvtHst.getUrlData() + "\">가입하기</a>", "UTF-8"));

        directSendAPIService.sendMail(ivtHst.getUserMail(), bodyMap);

        return addedIvtHst;
    }

    @Override
    public void editAllGroup(List<RequestProjectGroupEditDTO> prjGrEditList, HttpServletRequest request) {
        for(int i = 0; i < prjGrEditList.size(); i++) {
            ProjectGroup oldPrjGr =
                prjGrRepos.findById(prjGrEditList.get(i).getId())
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 프로젝트 그룹원입니다."));
            oldPrjGr.setId(prjGrEditList.get(i).getId());
            oldPrjGr.setUserAuthType(prjGrEditList.get(i).getUserAuthType());

            if(prjGrEditList.get(i).getDeleteYn() == false) {
                prjGrRepos.save(oldPrjGr);
            } else {
                oldPrjGr.setDeleteYn(true);
                prjGrRepos.save(oldPrjGr);
            }
        }
    }

    @Override
    public void removeGroup(long id, HttpServletRequest request) {
        ProjectGroup projectGroup =
            prjGrRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 프로젝트 그룹원입니다."));

        projectGroup.setDeleteYn(true);
        prjGrRepos.save(projectGroup);
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
    public ListResponse<Project> findAll(Project project, Pages pages, HttpServletRequest request) throws Exception {
        log.error("pages: {}", pages);
        long count = prjDslRepos.countAll(project);
        List<Project> list = prjDslRepos.findAll(project, pages);

        return new ListResponse<>(count, list, pages);
    }

    @Override
    public Project add(Project project, HttpServletRequest request) throws Exception { return null; }

    @Override
    public Project generate(Project oldProject) {
        return
            Project
            .builder()
            .id(oldProject.getId())
            .address(oldProject.getAddress())
            .addressDetail(oldProject.getAddressDetail())
            .contents(oldProject.getContents())
            .createdAt(oldProject.getCreatedAt() == null ? dateUtil.getThisTime() : oldProject.getCreatedAt())
            .endAt(oldProject.getEndAt())
            .image(oldProject.getImage())
            .maxUserCount(oldProject.getMaxUserCount())
            .name(oldProject.getName())
            .startAt(oldProject.getStartAt())
            .status(oldProject.getStatus())
            .build();
    }

}