package com.safeapp.admin.web.service.impl;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.utils.ResponseUtil;
import com.safeapp.admin.utils.DateUtil;
import com.safeapp.admin.utils.PasswordUtil;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.cmmn.Token;
import com.safeapp.admin.web.model.entity.Admins;
import com.safeapp.admin.web.repos.jpa.AdminRepos;
import com.safeapp.admin.web.repos.jpa.SmsAuthHistoryRepos;
import com.safeapp.admin.web.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import com.safeapp.admin.web.service.cmmn.DirectSendAPIService;
import com.safeapp.admin.web.service.cmmn.JwtService;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    private final AdminRepos adminRepos;
    private final PasswordUtil passwordUtil;
    private final DateUtil dateUtil;
    private final JwtService jwtService;
    private final SmsAuthHistoryRepos smsAuthHistoryRepos;
    private final DirectSendAPIService directSendAPIService;

    @Autowired
    public LoginServiceImpl(AdminRepos adminRepos, PasswordUtil passwordUtil, DateUtil dateUtil, JwtService jwtService,
            SmsAuthHistoryRepos smsAuthHistoryRepos, DirectSendAPIService directSendAPIService) {

        this.adminRepos = adminRepos;
        this.passwordUtil = passwordUtil;
        this.dateUtil = dateUtil;
        this.jwtService = jwtService;
        this.smsAuthHistoryRepos = smsAuthHistoryRepos;
        this.directSendAPIService = directSendAPIService;
    }

    public ResponseEntity login(String email, String password, HttpServletRequest request) throws Exception {
        log.error("1");
        Admins adminInfo = adminRepos.findByEmail(email);
        log.error("2");
        // 입력한 이메일이 존재하지 않거나, 이미 삭제 처리된 이메일일 때
        if(Objects.isNull(adminInfo) || adminInfo.getDeleted() == YN.Y) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "입력하신 로그인 정보가 일치하지 않습니다.");
        }
        // 입력한 이메일은 존재하나, 비밀번호가 일치하지 않을 때
        if(!passwordUtil.checkEqual(adminInfo.getPassword(), password)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "입력하신 로그인 정보가 일치하지 않습니다.");
        }
        log.error("adminInfo: {}", adminInfo);
        String template =
                "Addr:" + request.getRemoteAddr()
                + "/Host:" + request.getRemoteHost()
                + "/User:" + request.getRemoteUser();

        Token token = jwtService.generateAccessToken(adminInfo, template);

        return ResponseUtil.sendResponse(token);
    }

}