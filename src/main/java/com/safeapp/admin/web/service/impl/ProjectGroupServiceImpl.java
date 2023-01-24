package com.safeapp.admin.web.service.impl;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.utils.DateUtil;
import com.safeapp.admin.utils.PasswordUtil;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.ProjectGroup;
import com.safeapp.admin.web.model.entity.Users;
import com.safeapp.admin.web.repos.jpa.ProjectGroupRepos;
import com.safeapp.admin.web.repos.jpa.dsl.ProjectGroupDslRepos;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.safeapp.admin.web.service.ProjectGroupService;
import com.safeapp.admin.web.service.cmmn.JwtService;

@Service
@AllArgsConstructor
@Slf4j
public class ProjectGroupServiceImpl implements ProjectGroupService {

    private final ProjectGroupRepos prjGrRepos;
    private final ProjectGroupDslRepos prjGrDslRepos;

    @Transactional
    @Override
    public ProjectGroup add(ProjectGroup prjGr, HttpServletRequest request) throws Exception {
        if(Objects.isNull(prjGr)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 그룹원입니다.");
        }

        ProjectGroup prjGrInfo = prjGrRepos.save(prjGr);
        if(Objects.isNull(prjGrInfo)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "DB 저장 중 오류가 발생하였습니다.");
        }

        return prjGrInfo;
    }

    @Override
    public ProjectGroup find(long id, HttpServletRequest httpServletRequest) throws Exception {
        ProjectGroup oldPrjGr =
            prjGrRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 그룹원입니다."));
        
        return oldPrjGr;
    }

    @Override
    public ProjectGroup edit(ProjectGroup prjGr, HttpServletRequest request) throws Exception {
        ProjectGroup prjGrInfo =
            prjGrRepos.findById(prjGr.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 그룹원입니다."));

        prjGr = prjGrRepos.save(generate(prjGr));

        return prjGr;
    }

    @Override
    public void remove(long id, HttpServletRequest request) {
        ProjectGroup prjGrInfo =
            prjGrRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 그룹원입니다."));

        prjGrRepos.delete(prjGrInfo);
    }

    @Override
    public ListResponse<ProjectGroup> findAll(ProjectGroup prjGr, Pages pages, HttpServletRequest request) throws Exception {
        long count = prjGrDslRepos.countAll(prjGr);
        List<ProjectGroup> list = prjGrDslRepos.findAll(prjGr, pages);

        return new ListResponse(count, list, pages);
    }

    @Override
    public ProjectGroup generate(ProjectGroup obj) { return null; }

}