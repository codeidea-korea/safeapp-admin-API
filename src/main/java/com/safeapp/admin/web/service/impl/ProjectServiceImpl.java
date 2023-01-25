package com.safeapp.admin.web.service.impl;

import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.model.entity.*;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    public Project find(long id, HttpServletRequest request) throws Exception {
        Project oldProject =
            prjRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 프로젝트입니다."));

        return oldProject;
    }

    @Override
    public Project edit(Project project, HttpServletRequest request) throws Exception {
        Project prjInfo =
            prjRepos.findById(project.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 프로젝트입니다."));

        project = prjRepos.save(generate(project));

        return project;
    }

    @Override
    public void remove(long id, HttpServletRequest request) {
        Project prjInfos =
            prjRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 프로젝트입니다."));

        prjRepos.delete(prjInfos);
    }

    @Override
    public ListResponse<Project> findAll(Project project, Pages pages, HttpServletRequest request) throws Exception {
        long count = prjDslRepos.countAll(project);
        List<Project> list = prjDslRepos.findAll(project, pages);

        return new ListResponse<>(count, list, pages);
    }

    @Override
    public Project add(Project obj, HttpServletRequest request) throws Exception { return null; }

    @Override
    public Project generate(Project obj) { return null; }

}