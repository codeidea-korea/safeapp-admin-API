package com.safeapp.admin.web.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.data.AdminType;
import com.safeapp.admin.utils.DateUtil;
import com.safeapp.admin.utils.PasswordUtil;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.request.RequestAdminsDTO;
import com.safeapp.admin.web.dto.request.RequestAdminsModifyDTO;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.docs.LoginHistory;
import com.safeapp.admin.web.model.entity.Admins;
import com.safeapp.admin.web.model.entity.SmsAuthHistory;
import com.safeapp.admin.web.repos.jpa.AdminRepos;
import com.safeapp.admin.web.repos.jpa.SmsAuthHistoryRepos;
import com.safeapp.admin.web.repos.jpa.dsl.AdminsDslRepos;
import com.safeapp.admin.web.repos.mongo.LoginHistoryRepos;
import com.safeapp.admin.web.service.AdminsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.safeapp.admin.web.service.cmmn.DirectSendAPIService;
import com.querydsl.core.util.StringUtils;

@Service
@AllArgsConstructor
@Slf4j
public class AdminsServiceImpl implements AdminsService {

    private final AdminRepos adminRepos;
    private final AdminsDslRepos adminsDslRepos;
    private final SmsAuthHistoryRepos smsAuthHistoryRepos;
    private final LoginHistoryRepos loginHistoryRepos;
    private final DirectSendAPIService directSendAPIService;
    private final PasswordUtil passwordUtil;
    private final DateUtil dateUtil;

    @Override
    public boolean chkAdminId(String adminId) {
        Admins adminInfo = adminRepos.findByAdminId(adminId);
        if(!Objects.isNull(adminInfo)) {
            return false;
        }

        return true;
    }

    @Override
    public boolean chkEmail(String email) {
        Admins adminInfo = adminRepos.findByEmail(email);
        if(!Objects.isNull(adminInfo)) {
            return false;
        }

        return true;
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

    public Admins toEntity(RequestAdminsDTO addDto) {
        Admins admin = new Admins();

        admin.setAdminId(addDto.getAdminId());
        admin.setEmail(addDto.getEmail());
        admin.setPassword(addDto.getPassword());
        admin.setAdminName(addDto.getAdminName());
        admin.setPhoneNo(addDto.getPhoneNo());
        admin.setMemo(addDto.getMemo());
        admin.setMarketingAllowed(addDto.getMarketingAllowed());
        admin.setMarketingAllowedAt(addDto.getMarketingAllowedAt());
        admin.setAdminType(addDto.getAdminType());

        return admin;
    }

    @Transactional
    @Override
    public Admins add(Admins admin, HttpServletRequest httpServletRequest) throws Exception {
        if(StringUtils.isNullOrEmpty(admin.getAdminId())) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "아이디를 입력해주세요.");
        }
        if(!chkAdminId(admin.getAdminId())) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "중복된 아이디입니다.");
        }
        if(Objects.isNull(admin)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 관리자입니다.");
        }

        admin.setAdminType(AdminType.ADMIN);
        admin.setDeleted(YN.N);
        if(admin.getMarketingAllowed() == YN.Y) {
            admin.setMarketingAllowedAt(dateUtil.getThisTime());
        }
        admin.setPassword(passwordUtil.encode(admin.getPassword()));

        Admins addedAdmin = adminRepos.save(admin);
        if(Objects.isNull(addedAdmin)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "DB 저장 중 오류가 발생하였습니다.");
        }

        return addedAdmin;
    }

    @Override
    public Admins find(long id, HttpServletRequest httpServletRequest) throws Exception {
        Admins oldAdmin =
            adminRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 관리자입니다."));

        return oldAdmin;
    }

    @Transactional
    @Override
    public Admins editPassword(String email, String newPass1, String newPass2,
            HttpServletRequest request) throws Exception {

        Admins oldAdmin = adminRepos.findByEmail(email);
        if(Objects.isNull(oldAdmin)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 관리자입니다.");
        }
        if(!newPass1.equals(newPass2)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "두 비밀번호가 일치하지 않습니다.");
        }

        String newPass = passwordUtil.encode(newPass1);
        oldAdmin.setPassword(newPass);

        Admins newAdmin = adminRepos.save(oldAdmin);
        return newAdmin;
    }

    public Admins toEntityModify(RequestAdminsModifyDTO modifyDTO) {
        Admins admin = new Admins();

        admin.setAdminId(modifyDTO.getAdminId());
        admin.setAdminName(modifyDTO.getAdminName());
        admin.setPhoneNo(modifyDTO.getPhoneNo());
        admin.setMemo(modifyDTO.getMemo());

        return admin;
    }

    @Transactional
    @Override
    public Admins edit(Admins admin, HttpServletRequest request) throws Exception {
        Admins oldAdmin =
            adminRepos.findById(admin.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 관리자입니다."));

        oldAdmin.edit(admin);

        Admins editedAdmin = adminRepos.save(oldAdmin);
        return editedAdmin;
    }

    @Override
    public void remove(long id, HttpServletRequest httpServletRequest) {
        Admins admin =
            adminRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 관리자입니다."));

        admin.setDeleted(YN.Y);
        admin.setDeleteYn(true);

        adminRepos.save(admin);
    }

    @Override
    public ListResponse<Admins> findAll(Admins admin, Pages pages, HttpServletRequest request) {
        long count = adminsDslRepos.countAll(admin);
        List<Admins> list = adminsDslRepos.findAll(admin, pages);

        return new ListResponse(count, list, pages);
    }

    @Override
    public Admins generate(Admins oldAdmin) { return null; }

}