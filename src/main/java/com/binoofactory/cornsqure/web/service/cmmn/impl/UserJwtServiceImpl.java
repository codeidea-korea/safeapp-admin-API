package com.binoofactory.cornsqure.web.service.cmmn.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import com.binoofactory.cornsqure.utils.BfJwtUtil;
import com.binoofactory.cornsqure.utils.DateUtil;
import com.binoofactory.cornsqure.web.data.UserType;
import com.binoofactory.cornsqure.web.model.cmmn.BfToken;
import com.binoofactory.cornsqure.web.model.entity.Users;
import com.binoofactory.cornsqure.web.repos.jpa.UserRepos;
import com.binoofactory.cornsqure.web.service.cmmn.UserJwtService;
import com.querydsl.core.util.StringUtils;

import io.jsonwebtoken.ExpiredJwtException;

@Service
public class UserJwtServiceImpl implements UserJwtService {

    private final BfJwtUtil jwtUtil;

    private final UserRepos userRepos;

    private final DateUtil dateUtil;

    @Autowired
    public UserJwtServiceImpl(BfJwtUtil jwtUtil, UserRepos userRepos, DateUtil dateUtil) {
        this.jwtUtil = jwtUtil;
        this.userRepos = userRepos;
        this.dateUtil = dateUtil;
    }

    private void checkToken(String token) {
        if (StringUtils.isNullOrEmpty(token)) {
            throw new HttpServerErrorException(HttpStatus.UNAUTHORIZED, "토큰/사용자 코드는 반드시 필요한 값입니다.");
        }
        if (jwtUtil.isExpired(token)) {
            throw new HttpServerErrorException(HttpStatus.UNAUTHORIZED, "사용이 만료된 토큰입니다. 재발급 받아 주시기 바랍니다.");
        }
        String userId = jwtUtil.getUserId(token);
        if (StringUtils.isNullOrEmpty(userId)) {
            throw new HttpServerErrorException(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다.");
        }
        Users user = userRepos.findByUserId(userId);
        if (Objects.isNull(user)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "없는 사용자입니다.");
        }
    }
    
    private String removeBearer(String token) {
        if(StringUtils.isNullOrEmpty(token) || (!token.toLowerCase().contains("bearer "))) {
            throw new HttpServerErrorException(HttpStatus.UNAUTHORIZED, "토큰을 넣어주세요.");
        }
        return token.replace("bearer ", "").replace("Bearer ", "");
    }

    @Override
    public Users getUserInfoByToken(HttpServletRequest httpServletRequest) {
        String token = removeBearer(httpServletRequest.getHeader("Authorization"));

        checkToken(token);

        String userId = jwtUtil.getUserId(token);
        Users user = userRepos.findByUserId(userId);
        return user;
    }

    @Override
    public boolean checkUserTokenPriority(HttpServletRequest httpServletRequest, UserType lessType) {
        Users user;
        try {
            user = getUserInfoByToken(httpServletRequest);
            if (user.getType().getCode() > lessType.getCode()) {
                return true;
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        return false;
    }

    @Override
    public Users getUserInfoByTokenAnyway(HttpServletRequest httpServletRequest) {
        try {
            return getUserInfoByToken(httpServletRequest);
        } catch (HttpServerErrorException e) {
            e.getStackTrace();
        }

        Users user = new Users();
        user.setType(UserType.NONE);
        return user;
    }

    @Override
    public BfToken generateToken(Users user, String userCode) {
        BfToken token = new BfToken(user);
        token.setAccessToken(jwtUtil.generateAccessToken(user));
        token.setRefreshToken(jwtUtil.generateRefreshToken(user));
        
        Date date = new Date(System.currentTimeMillis() + BfJwtUtil.ACCESS_TOKEN_ALIVE_TIME);
        LocalDateTime localDateTime = date.toInstant() 
            .atZone(ZoneId.systemDefault()) 
            .toLocalDateTime(); 
        token.setExpiredAt(localDateTime);
        
        return token;
    }

    @Override
    public BfToken generateTokenByRefreshToken(String refreshToken) {
        
        if(jwtUtil.isExpired(refreshToken)) {
            throw new HttpServerErrorException(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다. 다시 로그인하여주세요.");
        }
        
        String userId = jwtUtil.getUserIdByRefresh(refreshToken);
        Users user = userRepos.findByUserId(userId);
        
        if(user == null) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "없는 사용자입니다.");
        }
        
        BfToken token = new BfToken(user);
        token.setAccessToken(jwtUtil.generateAccessToken(user));
        token.setRefreshToken(jwtUtil.generateRefreshToken(user));
        
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
