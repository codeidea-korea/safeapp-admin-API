package com.safeapp.admin.web.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.dto.request.RequestUserDTO;
import com.safeapp.admin.utils.DateUtil;
import com.safeapp.admin.utils.PasswordUtil;
import com.safeapp.admin.web.data.UserType;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.SmsAuthHistory;
import com.safeapp.admin.web.model.entity.Users;
import com.safeapp.admin.web.repos.jpa.SmsAuthHistoryRepos;
import com.safeapp.admin.web.repos.jpa.UserRepos;
import com.safeapp.admin.web.repos.jpa.dsl.UsersDslRepos;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.safeapp.admin.web.service.UserService;
import com.safeapp.admin.web.service.cmmn.DirectSendAPIService;
import com.querydsl.core.util.StringUtils;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepos userRepos;
    private final UsersDslRepos userDslRepos;
    private final PasswordUtil passwordUtil;
    private final DateUtil dateUtil;
    private final SmsAuthHistoryRepos smsAuthHistoryRepos;
    private final DirectSendAPIService directSendAPIService;

    @Override
    public boolean chkUserId(String userId) {
        Users userInfo = userRepos.findByUserId(userId);
        if(!Objects.isNull(userInfo)) {
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

    public Users toEntity(RequestUserDTO dto) {
        Users user = new Users();

        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setPhoneNo(dto.getPhoneNo());
        user.setType(dto.getType());
        user.setUserId(dto.getUserId());
        user.setUserName(dto.getUserName());
        user.setMarketingAllowed(dto.getMarketingAllowed());
        user.setMarketingAllowedAt(dto.getMarketingAllowedAt());

        return user;
    }

    @Transactional
    @Override
    public Users add(Users user, HttpServletRequest request) throws Exception {
        if(StringUtils.isNullOrEmpty(user.getUserId())) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "아이디를 입력해주세요.");
        }
        if(!chkUserId(user.getUserId())) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "중복된 아이디입니다.");
        }

        user.setType(UserType.NORMAL);
        user.setDeleted(YN.N);
        if(user.getMarketingAllowed() == YN.Y) {
            user.setMarketingAllowedAt(dateUtil.getThisTime());
        }
        if(user.getMessageAllowed() == YN.Y) {
            user.setMessageAllowedAt(dateUtil.getThisTime());
        }
        user.setPassword(passwordUtil.encode(user.getPassword()));

        Users userInfo = userRepos.save(user);
        if(Objects.isNull(userInfo)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "DB 저장 중 오류가 발생하였습니다.");
        }

        return userInfo;
    }

    @Override
    public Users find(long id, HttpServletRequest request) throws Exception {
        Users oldUser = userRepos.findById(id).orElse(null);
        //if(Objects.isNull(oldUser) || oldUser.getType() != UserType.ADMIN) {
        if(Objects.isNull(oldUser)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다.");
        }

        return oldUser;
    }

    @Transactional
    @Override
    public Users editPassword(String userId, String newPass1, String newPass2,
                              HttpServletRequest request) throws Exception {

        Users userInfo = userRepos.findByUserId(userId);
        if(userInfo == null) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다.");
        }

        if(!newPass1.equals(newPass2)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "두 비밀번호가 일치하지 않습니다.");
        }
        String newPass = passwordUtil.encode(newPass1);
        userInfo.setPassword(newPass);

        return userRepos.save(userInfo);
    }

    @Transactional
    @Override
    public Users edit(Users user, HttpServletRequest request) throws Exception {
        Users userInfo = userRepos.findByUserId(user.getUserId());
        if(Objects.isNull(userInfo)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다.");
        }

        user.setPassword(passwordUtil.encode(user.getPassword()));
        user = userRepos.save(user);

        return user;
    }

    @Override
    public void remove(long id, HttpServletRequest request) {
        Users userInfo = userRepos.findById(id).orElse(null);
        if(Objects.isNull(userInfo)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다.");
        }

        userInfo.setDeleted(YN.Y);
        userRepos.save(userInfo);
    }

    @Override
    public ListResponse<Users> findAll(Users user, Pages pages, HttpServletRequest request) {
        long count = userDslRepos.countAll(user);
        List<Users> list = userDslRepos.findAll(user, pages);

        return new ListResponse(count, list, pages);
    }

    @Override
    public Users generate(Users obj) {
        return null;
    }

}