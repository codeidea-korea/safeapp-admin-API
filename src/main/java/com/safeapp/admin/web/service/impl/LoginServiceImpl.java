package com.safeapp.admin.web.service.impl;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.dto.request.RequestUserDTO;
import com.safeapp.admin.utils.DateUtil;
import com.safeapp.admin.utils.PasswordUtil;
import com.safeapp.admin.web.data.UserType;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.cmmn.BfListResponse;
import com.safeapp.admin.web.model.cmmn.BfPage;
import com.safeapp.admin.web.model.cmmn.Token;
import com.safeapp.admin.web.model.docs.LoginHistory;
import com.safeapp.admin.web.model.entity.Admins;
import com.safeapp.admin.web.model.entity.Users;
import com.safeapp.admin.web.repos.jpa.AdminRepos;
import com.safeapp.admin.web.repos.jpa.SmsAuthHistoryRepos;
import com.safeapp.admin.web.repos.jpa.UserRepos;
import com.safeapp.admin.web.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.safeapp.admin.web.repos.mongo.LoginHistoryRepos;
import com.safeapp.admin.web.service.cmmn.DirectSendAPIService;
import com.safeapp.admin.web.service.cmmn.JwtService;
import com.querydsl.core.util.StringUtils;

@Service
public class LoginServiceImpl implements LoginService {

    private final AdminRepos adminRepos;
    private final PasswordUtil passwordUtil;

    private final JwtService jwtService;

    @Autowired
    public LoginServiceImpl(AdminRepos userRepos, LoginHistoryRepos loginHistoryRepos,
                           SmsAuthHistoryRepos smsAuthHistoryRepos, DirectSendAPIService directSendAPIService,
                           JwtService jwtService, PasswordUtil passwordUtil, DateUtil dateUtil) {

        this.userRepos = userRepos;
        this.loginHistoryRepos = loginHistoryRepos;
        this.jwtService = jwtService;
        this.smsAuthHistoryRepos = smsAuthHistoryRepos;
        this.directSendAPIService = directSendAPIService;
        this.passwordUtil = passwordUtil;
        this.dateUtil = dateUtil;
    }

    /*
    private final SmsAuthHistoryRepos smsAuthHistoryRepos;

    private final DirectSendAPIService directSendAPIService;

    private final DateUtil dateUtil;
    */

    public ResponseEntity login(String email, String password, HttpServletRequest request) throws Exception {
        Admins admin = new Admins();

        admin.setEmail(email);
        admin.setPassword(password);

        String template =
                "Addr:" + request.getRemoteAddr()
                + "/Host:" + request.getRemoteHost()
                + "/User:" + request.getRemoteUser();

        Admins adminInfo = adminRepos.findByEmail(admin.getEmail());

        // 입력한 이메일이 존재하지 않거나, 삭제 처리된 이메일일 때
        if(Objects.isNull(adminInfo) || adminInfo.getDeleted() == YN.Y) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "입력하신 로그인 정보가 일치하지 않습니다.");
        }
        // 입력한 이메일은 존재하나, 비밀번호가 일치하지 않을 때
        if(!passwordUtil.checkEqual(adminInfo.getPassword(), admin.getPassword())) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "입력하신 로그인 정보가 일치하지 않습니다.");
        }

        Token token = jwtService.generateToken(adminInfo, template);

        loginHistoryRepos.save(LoginHistory.builder()
                .createDt(LocalDateTime.now())
                .isSuccess(true)
                .userId(user.getUserId())
                .password(user.getPassword())
                .platformInfo(template)
                .build());

        return token;
    }

}