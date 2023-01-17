package com.safeapp.admin.web.service.impl;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.utils.DateUtil;
import com.safeapp.admin.utils.PasswordUtil;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.ChecklistProjectResultImg;
import com.safeapp.admin.web.repos.jpa.ChecklistProjectResultImgRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.safeapp.admin.web.service.ChecklistProjectResultImgService;
import com.safeapp.admin.web.service.cmmn.JwtService;

@Service
public class ChecklistProjectResultImgServiceImpl implements ChecklistProjectResultImgService {

    private final ChecklistProjectResultImgRepos repos;

    private final JwtService jwtService;

    private final PasswordUtil passwordUtil;

    private final DateUtil dateUtil;

    @Autowired
    public ChecklistProjectResultImgServiceImpl(ChecklistProjectResultImgRepos repos,
                                                JwtService jwtService, PasswordUtil passwordUtil, DateUtil dateUtil) {

        this.repos = repos;
        this.jwtService = jwtService;
        this.passwordUtil = passwordUtil;
        this.dateUtil = dateUtil;
    }

    @Transactional
    @Override
    public ChecklistProjectResultImg add(ChecklistProjectResultImg instance, HttpServletRequest httpServletRequest)
        throws Exception {
        if (Objects.isNull(instance)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }
        ChecklistProjectResultImg savedInstance = repos.save(generate(instance));
        if (Objects.isNull(savedInstance)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "저장중 오류가 발생했습니다.");
        }
        return savedInstance;
    }

    @Override
    public ChecklistProjectResultImg edit(ChecklistProjectResultImg instance, HttpServletRequest httpServletRequest)
        throws Exception {
        ChecklistProjectResultImg savedInstance = repos.findById(instance.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "수정을 위해서는 키가 필요합니다."));
        if (Objects.isNull(savedInstance)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }
        savedInstance = repos.save(generate(instance));
        return savedInstance;
    }

    @Override
    public void remove(long id, HttpServletRequest httpServletRequest) {
        ChecklistProjectResultImg savedInstance = repos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "이미 삭제된 내용이거나 존재하지 않는 키입니다."));
        repos.delete(savedInstance);
    }

    @Override
    public ChecklistProjectResultImg find(long id, HttpServletRequest httpServletRequest) throws Exception {
        ChecklistProjectResultImg savedInstance = repos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "이미 삭제된 내용이거나 존재하지 않는 키입니다."));
        return savedInstance;
    }

    @Override
    public ListResponse<ChecklistProjectResultImg> findAll(ChecklistProjectResultImg instance, Pages bfPage,
                                                           HttpServletRequest httpServletRequest) throws Exception {
        return null;
    }

    @Override
    public ChecklistProjectResultImg generate(ChecklistProjectResultImg instance) {
        return ChecklistProjectResultImg.builder()
            .id(instance.getId())
            .resultId(instance.getResult().getId())
            .url(instance.getUrl())
            .build();
    }
}
