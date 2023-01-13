package com.safeapp.admin.web.service.impl;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.utils.DateUtil;
import com.safeapp.admin.utils.PasswordUtil;
import com.safeapp.admin.web.model.cmmn.BfListResponse;
import com.safeapp.admin.web.model.cmmn.BfPage;
import com.safeapp.admin.web.model.entity.ProjectManager;
import com.safeapp.admin.web.repos.jpa.ProjectManagersRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.safeapp.admin.web.repos.jpa.dsl.ProjectManagersDslRepos;
import com.safeapp.admin.web.service.ProjectManagersService;
import com.safeapp.admin.web.service.cmmn.JwtService;

@Service
public class ProjectManagersServiceImpl implements ProjectManagersService {

    private final ProjectManagersRepos repos;

    private final ProjectManagersDslRepos dslRepos;

    private final JwtService jwtService;

    private final PasswordUtil passwordUtil;

    private final DateUtil dateUtil;

    @Autowired
    public ProjectManagersServiceImpl(ProjectManagersRepos repos, ProjectManagersDslRepos dslRepos,
                                      JwtService jwtService, PasswordUtil passwordUtil, DateUtil dateUtil) {

        this.repos = repos;
        this.dslRepos = dslRepos;
        this.jwtService = jwtService;
        this.passwordUtil = passwordUtil;
        this.dateUtil = dateUtil;
    }

    @Transactional
    @Override
    public ProjectManager add(ProjectManager instance, HttpServletRequest httpServletRequest) throws Exception {
        if (Objects.isNull(instance)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }
        ProjectManager savedInstance = repos.save(generate(instance));
        if (Objects.isNull(savedInstance)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "저장중 오류가 발생했습니다.");
        }
        return savedInstance;
    }

    @Override
    public ProjectManager edit(ProjectManager instance, HttpServletRequest httpServletRequest)
        throws Exception {
        ProjectManager savedInstance = repos.findById(instance.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "수정을 위해서는 키가 필요합니다."));
        if (Objects.isNull(savedInstance)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }
        savedInstance = repos.save(generate(instance));
        return savedInstance;
    }

    @Override
    public void remove(long id, HttpServletRequest httpServletRequest) {
        ProjectManager savedInstance = repos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "이미 삭제된 내용이거나 존재하지 않는 키입니다."));
        repos.delete(savedInstance);
    }

    @Override
    public void removeByProjectId(long projectId, HttpServletRequest httpServletRequest) {
        List<ProjectManager> savedInstance = repos.findAllByProjectId(projectId);
        repos.deleteAll(savedInstance);
    }

    @Override
    public ProjectManager find(long id, HttpServletRequest httpServletRequest) throws Exception {
        ProjectManager savedInstance = repos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "이미 삭제된 내용이거나 존재하지 않는 키입니다."));
        return savedInstance;
    }

    @Override
    public BfListResponse<ProjectManager> findAll(ProjectManager instance, BfPage bfPage,
                                                  HttpServletRequest httpServletRequest) throws Exception {

        List<ProjectManager> list = dslRepos.findAll(instance, bfPage);
        long count = dslRepos.countAll(instance);

        return new BfListResponse<ProjectManager>(list, count, bfPage);
    }

    @Override
    public ProjectManager generate(ProjectManager obj) {
        return null;
    }
}
