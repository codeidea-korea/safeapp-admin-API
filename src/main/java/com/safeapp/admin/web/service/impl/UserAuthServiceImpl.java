package com.safeapp.admin.web.service.impl;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.utils.DateUtil;
import com.safeapp.admin.utils.PasswordUtil;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.UserAuth;
import com.safeapp.admin.web.model.entity.Users;
import com.safeapp.admin.web.repos.jpa.UserAuthRepos;
import com.safeapp.admin.web.repos.jpa.UserRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.safeapp.admin.web.service.UserAuthService;
import com.safeapp.admin.web.service.cmmn.JwtService;

@Service
public class UserAuthServiceImpl implements UserAuthService {

    private final UserAuthRepos repos;

    private final UserRepos userRepos;

    private final JwtService jwtService;

    private final PasswordUtil passwordUtil;

    private final DateUtil dateUtil;

    @Autowired
    public UserAuthServiceImpl(UserAuthRepos repos, UserRepos userRepos,
                               JwtService jwtService, PasswordUtil passwordUtil, DateUtil dateUtil) {

        this.repos = repos;
        this.userRepos = userRepos;
        this.jwtService = jwtService;
        this.passwordUtil = passwordUtil;
        this.dateUtil = dateUtil;
    }

    @Transactional
    @Override
    public UserAuth add(UserAuth instance, HttpServletRequest httpServletRequest) throws Exception {
        if (Objects.isNull(instance)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }
        UserAuth savedInstance = repos.save(instance);
        if (Objects.isNull(savedInstance)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "저장중 오류가 발생했습니다.");
        }
        return savedInstance;
    }

    @Override
    public UserAuth edit(UserAuth instance, HttpServletRequest httpServletRequest)
        throws Exception {
        UserAuth savedInstance = repos.findById(instance.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "수정을 위해서는 키가 필요합니다."));
        if (Objects.isNull(savedInstance)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }

        savedInstance = repos.save(instance);
        return savedInstance;
    }

    @Override
    public void remove(long id, HttpServletRequest httpServletRequest) {
        UserAuth savedInstance = repos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "이미 삭제된 내용이거나 존재하지 않는 키입니다."));
        repos.delete(savedInstance);
    }

    @Override
    public UserAuth find(long id, HttpServletRequest httpServletRequest) throws Exception {
        UserAuth savedInstance = repos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "이미 삭제된 내용이거나 존재하지 않는 키입니다."));
        return savedInstance;
    }

    @Override
    public ListResponse<UserAuth> findAll(UserAuth instance, Pages bfPage,
                                          HttpServletRequest httpServletRequest) throws Exception {
        return null;
    }

    @Override
    public UserAuth generate(UserAuth instance) {
        return UserAuth.builder()
            .id(instance.getId())
            //.userId(instance.getUser().getId())
            //.createdAt(instance.getCreatedAt() == null ? dateUtil.getThisTime() : instance.getCreatedAt())
            .efectiveEndAt(instance.getEfectiveEndAt())
            .efectiveStartAt(instance.getEfectiveStartAt())
            .build();
    }

    @Override
    public UserAuth getEfectiveAuthByUserId(String userId) {
        Users user = userRepos.findByUserId(userId);
       // List<UserAuth> auths = repos.findAllByUserIdAndEfectiveEndAtAfter(user.getId(), dateUtil.getThisTime());
        List<UserAuth> auths = null;
        return auths == null || auths.size() < 1 ? null : auths.get(0);
    }
}
