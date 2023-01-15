package com.safeapp.admin.web.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.data.AdminType;
import com.safeapp.admin.utils.DateUtil;
import com.safeapp.admin.utils.PasswordUtil;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.BfPage;
import com.safeapp.admin.web.model.entity.Admins;
import com.safeapp.admin.web.model.entity.SmsAuthHistory;
import com.safeapp.admin.web.repos.jpa.AdminRepos;
import com.safeapp.admin.web.repos.jpa.SmsAuthHistoryRepos;
import com.safeapp.admin.web.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.safeapp.admin.web.service.cmmn.DirectSendAPIService;
import com.querydsl.core.util.StringUtils;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepos adminRepos;
    private final SmsAuthHistoryRepos smsAuthHistoryRepos;
    private final DirectSendAPIService directSendAPIService;
    private final PasswordUtil passwordUtil;
    private final DateUtil dateUtil;

    @Autowired
    public AdminServiceImpl(AdminRepos adminRepos, SmsAuthHistoryRepos smsAuthHistoryRepos, DirectSendAPIService directSendAPIService,
                           PasswordUtil passwordUtil, DateUtil dateUtil) {

        this.adminRepos = adminRepos;
        this.smsAuthHistoryRepos = smsAuthHistoryRepos;
        this.directSendAPIService = directSendAPIService;
        this.passwordUtil = passwordUtil;
        this.dateUtil = dateUtil;
    }

    @Override
    public boolean chkAdminID(String adminID) {
        Admins adminInfo = adminRepos.findByAdminID(adminID);

        if(!Objects.isNull(adminInfo)) {
            return false;
        }

        return true;
    }

    @Transactional
    @Override
    public Admins add(Admins admin, HttpServletRequest httpServletRequest) throws Exception {
        if(StringUtils.isNullOrEmpty(admin.getAdminID())) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "아이디를 입력해주세요.");
        }
        if(!chkAdminID(admin.getAdminID())) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "중복된 아이디입니다.");
        }

        admin.setType(AdminType.ADMIN);
        admin.setDeleted(YN.N);

        admin.setPassword(passwordUtil.encode(admin.getPassword()));
        Admins adminInfo = adminRepos.save(admin);

        if(Objects.isNull(adminInfo)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "저장 중 오류가 발생하였습니다.");
        }

        return adminInfo;
    }

    @Override
    public Admins find(long seq, HttpServletRequest httpServletRequest) throws Exception {
        Admins oldAdmin = adminRepos.findById(seq).orElse(null);
        //if(Objects.isNull(oldAdmin) || oldAdmin.getType() != AdminType.ADMIN) {
        if(Objects.isNull(oldAdmin)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 관리자입니다.");
        }

        return oldAdmin;
    }

    @Transactional
    @Override
    public Admins editPassword(String adminID, String password, String newPassword,
                               HttpServletRequest httpServletRequest) throws Exception {

        Admins myInfo = adminRepos.findByAdminID(adminID);
        if(myInfo == null) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 관리자입니다.");
        }

        String passwordChk = passwordUtil.encode(password);
        String passwordConfirm = passwordUtil.encode(newPassword);
        if(!passwordChk.equals(passwordConfirm)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "두 비밀번호가 일치하지 않습니다.");
        }

        myInfo.setPassword(passwordChk);
        return adminRepos.save(myInfo);
    }

    @Transactional
    @Override
    public Admins edit(Admins admin, HttpServletRequest httpServletRequest) throws Exception {
        Admins adminInfo = adminRepos.findByAdminID(admin.getAdminID());
        if(Objects.isNull(adminInfo)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 관리자입니다.");
        }

        admin.setPassword(passwordUtil.encode(admin.getPassword()));
        admin = adminRepos.save(admin);

        return admin;
    }

    @Override
    public void remove(long seq, HttpServletRequest httpServletRequest) {
        Admins adminInfo = adminRepos.findById(seq).orElse(null);
        if(Objects.isNull(adminInfo)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다.");
        }

        if(adminInfo.getType() == AdminType.ADMIN) {
            adminInfo.setDeleted(YN.Y);
            adminRepos.save(adminInfo);
        }
    }

    @Override
    public ListResponse<Admins> findAll(Admins admin, BfPage bfPage, HttpServletRequest httpServletRequest)
            throws Exception {

        return null;
    }

    @Override
    public boolean sendAuthSMSCode(String phoneNo) throws Exception {
        LocalDateTime thisTime = dateUtil.getThisTime();
        String authCode = Math.round(Math.random() * 1000000) + "";

        SmsAuthHistory smsAuthHistory =
                SmsAuthHistory.builder()
                .createdAt(thisTime)
                .efectedEndedAt(thisTime.plusMinutes(3))
                .phoneNo(phoneNo)
                .authCode(authCode)
                .build();

        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("title", "인증번호입니다.");
        bodyMap.put("message", "인증번호는 [" + authCode + "]입니다.");
        bodyMap.put("name", "인증자");
        bodyMap.put("phoneNo", phoneNo);

        smsAuthHistoryRepos.save(smsAuthHistory);

        return directSendAPIService.sendSMS(phoneNo, bodyMap);
    }

    @Override
    public boolean isCorrectSMSCode(String phoneNo, String authNo) {
        LocalDateTime thisTime = dateUtil.getThisTime();
        SmsAuthHistory smsAuthHistory =
                smsAuthHistoryRepos.findFirstByPhoneNoAndEfectedEndedAtAfterOrderByIdDesc(phoneNo, thisTime);

        if(smsAuthHistory == null) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "먼저 인증을 요청해주세요.");
        }
        if(smsAuthHistory.getAuthCode().equals(authNo)) {
            return true;
        }

        return false;
    }

    /*
    public Admins toEntity(RequestUserDTO dto) {
        Admins admin = new Admins();

        admin.setType(dto.getType());
        admin.setAdminID(dto.getUserId());
        admin.setPhoneNo(dto.getPhoneNo());
        admin.setUserName(dto.getUserName());
        admin.setEmail(dto.getEmail());
        admin.setSnsAllowed(dto.getSnsAllowed());
        admin.setMarketingAllowed(dto.getMarketingAllowed());
        admin.setMarketingAllowedAt(dto.getMarketingAllowedAt());
        admin.setMessageAllowed(dto.getMessageAllowed());
        admin.setMessageAllowedAt(dto.getMessageAllowedAt());
        admin.setPassword(dto.getPassword());
        admin.setSnsType(dto.getSnsType());

        return user;
    }
    */

    @Override
    public Admins generate(Admins obj) {
        return null;
    }

}