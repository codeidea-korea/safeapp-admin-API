package com.safeapp.admin.web.service.impl;

import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.utils.DateUtil;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.response.ResponseCheckListProjectDTO;
import com.safeapp.admin.web.dto.response.ResponseProjectGroupDTO;
import com.safeapp.admin.web.model.entity.*;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.repos.jpa.ProjectGroupRepos;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
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
    private final ProjectGroupRepos prjGrRepos;
    private final DateUtil dateUtil;

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

    @Override
    public List<ResponseProjectGroupDTO> findAllGroupByCondition(long id, int pageNo, int pageSize, HttpServletRequest request) {

        return prjGrRepos.findAllById(id, pageNo, pageSize, request);
    }

    @Override
    public void removeGroup(long id, HttpServletRequest request) {
        ProjectGroup projectGroup =
            prjGrRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 프로젝트 그룹원입니다."));

        prjGrRepos.delete(projectGroup);
    }

    @Override
    public void remove(long id, HttpServletRequest request) {
        Project project =
            prjRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 프로젝트입니다."));

        prjRepos.delete(project);
    }

    @Override
    public ListResponse<Project> findAll(Project project, Pages pages, HttpServletRequest request) throws Exception {
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