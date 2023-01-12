package com.binoofactory.cornsqure.web.service.impl;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.binoofactory.cornsqure.utils.DateUtil;
import com.binoofactory.cornsqure.utils.PasswordUtil;
import com.binoofactory.cornsqure.web.model.cmmn.BfListResponse;
import com.binoofactory.cornsqure.web.model.cmmn.BfPage;
import com.binoofactory.cornsqure.web.model.entity.Notice;
import com.binoofactory.cornsqure.web.repos.jpa.NoticeRepos;
import com.binoofactory.cornsqure.web.repos.jpa.dsl.NoticeDslRepos;
import com.binoofactory.cornsqure.web.service.NoticeService;
import com.binoofactory.cornsqure.web.service.cmmn.UserJwtService;

@Service
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepos repos;

    private final NoticeDslRepos dslRepos;

    private final UserJwtService userJwtService;

    private final PasswordUtil passwordUtil;

    private final DateUtil dateUtil;

    @Autowired
    public NoticeServiceImpl(NoticeRepos repos, NoticeDslRepos dslRepos,
        UserJwtService userJwtService, PasswordUtil passwordUtil, DateUtil dateUtil) {

        this.repos = repos;
        this.dslRepos = dslRepos;
        this.userJwtService = userJwtService;
        this.passwordUtil = passwordUtil;
        this.dateUtil = dateUtil;
    }

    @Transactional
    @Override
    public Notice add(Notice instance, HttpServletRequest httpServletRequest) throws Exception {
        if (Objects.isNull(instance)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }
        Notice savedInstance = repos.save(generate(instance));
        if (Objects.isNull(savedInstance)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "저장중 오류가 발생했습니다.");
        }
        return savedInstance;
    }

    @Override
    public Notice edit(Notice instance, HttpServletRequest httpServletRequest)
        throws Exception {
        Notice savedInstance = repos.findById(instance.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "수정을 위해서는 키가 필요합니다."));
        if (Objects.isNull(savedInstance)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }
        savedInstance = repos.save(generate(instance));
        return savedInstance;
    }

    @Override
    public void remove(long id, HttpServletRequest httpServletRequest) {
        Notice savedInstance = repos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "이미 삭제된 내용이거나 존재하지 않는 키입니다."));
        repos.delete(savedInstance);
    }

    @Override
    public Notice find(long id, HttpServletRequest httpServletRequest) throws Exception {
        Notice savedInstance = repos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "이미 삭제된 내용이거나 존재하지 않는 키입니다."));
        return savedInstance;
    }

    @Override
    public BfListResponse<Notice> findAll(Notice instance, BfPage bfPage,
        HttpServletRequest httpServletRequest) throws Exception {

        List<Notice> list = dslRepos.findAll(instance, bfPage);
        long count = dslRepos.countAll(instance);

        return new BfListResponse<Notice>(list, count, bfPage);
    }

    @Override
    public Notice generate(Notice instance) {
        return Notice.builder()
            .id(instance.getId())
            .contents(instance.getContents())
            .createdAt(instance.getCreatedAt() == null ? dateUtil.getThisTime() : instance.getCreatedAt())
            .title(instance.getTitle())
            .userId(instance.getUserId())
            .type(instance.getType())
            .build();
    }
}
