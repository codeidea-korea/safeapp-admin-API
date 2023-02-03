package com.safeapp.admin.web.service.cmmn.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.utils.JwtUtil;
import com.safeapp.admin.utils.DateUtil;
import com.safeapp.admin.web.data.AdminType;
import com.safeapp.admin.web.data.UserType;
import com.safeapp.admin.web.model.cmmn.Token;
import com.safeapp.admin.web.model.entity.Admins;
import com.safeapp.admin.web.model.entity.Users;
import com.safeapp.admin.web.repos.jpa.AdminRepos;
import com.safeapp.admin.web.repos.jpa.UserRepos;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import com.safeapp.admin.web.service.cmmn.JwtService;
import com.querydsl.core.util.StringUtils;

import io.jsonwebtoken.ExpiredJwtException;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {

    private final JwtUtil jwtUtil;
    private final AdminRepos adminRepos;
    private final UserRepos userRepos;
    private final DateUtil dateUtil;

    @Autowired
    public JwtServiceImpl(JwtUtil jwtUtil, AdminRepos adminRepos, UserRepos userRepos, DateUtil dateUtil) {
        this.jwtUtil = jwtUtil;
        this.adminRepos = adminRepos;
        this.userRepos = userRepos;
        this.dateUtil = dateUtil;
    }

    private void checkToken(String token) {
        if(StringUtils.isNullOrEmpty(token)) {
            throw new HttpServerErrorException(HttpStatus.UNAUTHORIZED, "[토큰] 또는 [관리자 코드]는 반드시 필요합니다.");
        }
        if(jwtUtil.isExpired(token)) {
            throw new HttpServerErrorException(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다.");
        }

        // 이메일
        String email = jwtUtil.getAdminIDOrUserIdByAccessToken(token);
        if(StringUtils.isNullOrEmpty(email)) {
            throw new HttpServerErrorException(HttpStatus.UNAUTHORIZED, "인증되지 않은 관리자입니다.");
        }

        Admins admin = adminRepos.findByEmail(email);
        if(Objects.isNull(admin)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 관리자입니다.");
        }
    }
    
    private String removeBearer(String token) {
        if(StringUtils.isNullOrEmpty(token) || (!token.toLowerCase().contains("bearer "))) {
            throw new HttpServerErrorException(HttpStatus.UNAUTHORIZED, "반드시 토큰을 넣어주셔야 합니다. 123");
        }
        
        return token.replace("Bearer ", "").replace("Bearer ", "");
    }

    @Override
    public Admins getAdminInfoByToken(HttpServletRequest request) {
        String token = removeBearer(request.getHeader("Authorization"));
        checkToken(token);

        String email = jwtUtil.getAdminIDOrUserIdByAccessToken(token);
        Admins admin = adminRepos.findByEmail(email);

        return admin;
    }

    @Override
    public Admins getAdminInfoByTokenAnyway(HttpServletRequest httpServletRequest) {
        try {
            return getAdminInfoByToken(httpServletRequest);
        } catch (HttpServerErrorException e) {
            e.getStackTrace();
        }

        Admins admin = new Admins();
        admin.setAdminType(AdminType.ADMIN);

        return admin;
    }

    @Override
    public boolean checkAdminTokenPriority(HttpServletRequest httpServletRequest, AdminType lessType) {
        Admins admin;

        try {
            admin = getAdminInfoByToken(httpServletRequest);
            if(admin.getAdminType().getCode() >= lessType.getCode()) {
                return true;
            }
        } catch (Exception e) {
            e.getStackTrace();
        }

        return false;
    }

    @Override
    public Users getUserInfoByToken(HttpServletRequest httpServletRequest) {
        String token = removeBearer(httpServletRequest.getHeader("Authorization"));
        checkToken(token);

        String userId = jwtUtil.getAdminIDOrUserIdByAccessToken(token);
        Users user = userRepos.findByUserId(userId);

        return user;
    }

    @Override
    public Users getUserInfoByTokenAnyway(HttpServletRequest httpServletRequest) {
        try {
            return getUserInfoByToken(httpServletRequest);
        } catch (HttpServerErrorException e) {
            e.getStackTrace();
        }

        Users user = new Users();
        user.setUserType(UserType.NONE);

        return user;
    }

    @Override
    public boolean checkUserTokenPriority(HttpServletRequest httpServletRequest, UserType lessType) {
        Users user;

        try {
            user = getUserInfoByToken(httpServletRequest);
            if(user.getUserType().getCode() >= lessType.getCode()) {
                return true;
            }
        } catch (Exception e) {
            e.getStackTrace();
        }

        return false;
    }

    @Override
    public Token generateAccessToken(Admins admin, String adminCode) {
        Token token = new Token(admin);

        token.setAccessToken(jwtUtil.generateAccessToken(admin));
        token.setRefreshToken(jwtUtil.generateRefreshToken(admin));
        
        Date date = new Date(System.currentTimeMillis() + JwtUtil.ACCESS_TOKEN_ALIVE_TIME);
        LocalDateTime localDateTime =
                date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        token.setExpiredAt(localDateTime);
        
        return token;
    }

    @Override
    public Token generateAccessTokenByRefreshToken(String refreshToken) {
        if(jwtUtil.isExpired(refreshToken)) {
            throw new HttpServerErrorException(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다.");
        }
        
        String adminId = jwtUtil.getAdminIDOrUserIdByRefreshToken(refreshToken);
        Admins admin = adminRepos.findByAdminId(adminId);
        
        if(admin == null) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 관리자입니다.");
        }
        
        Token token = new Token(admin);

        token.setAccessToken(jwtUtil.generateAccessToken(admin));
        token.setRefreshToken(jwtUtil.generateRefreshToken(admin));
        
        return token;
    }

    @Override
    public boolean checkTokenExpired(HttpServletRequest httpServletRequest) {
        String token = removeBearer(httpServletRequest.getHeader("Authorization"));

        try {
            return jwtUtil.isExpired(token);
        } catch (ExpiredJwtException e1) {
            e1.getStackTrace();

            return true;
        }
    }

}