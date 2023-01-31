package com.safeapp.admin.web.service.impl;

import java.time.LocalDateTime;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.dto.request.RequestCheckListProjectModifyDTO;
import com.safeapp.admin.web.dto.request.RequestUsersDTO;
import com.safeapp.admin.utils.DateUtil;
import com.safeapp.admin.utils.PasswordUtil;
import com.safeapp.admin.web.data.UserType;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.request.RequestUsersModifyDTO;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.CheckListProject;
import com.safeapp.admin.web.model.entity.CheckListProjectDetail;
import com.safeapp.admin.web.model.entity.SmsAuthHistory;
import com.safeapp.admin.web.model.entity.Users;
import com.safeapp.admin.web.repos.jpa.SmsAuthHistoryRepos;
import com.safeapp.admin.web.repos.jpa.UserRepos;
import com.safeapp.admin.web.repos.jpa.dsl.UsersDslRepos;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
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
    private final SmsAuthHistoryRepos smsAuthHistoryRepos;
    private final DirectSendAPIService directSendAPIService;
    private final PasswordUtil passwordUtil;
    private final DateUtil dateUtil;

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

    public Users toEntity(RequestUsersDTO addDto) {
        Users user = new Users();

        user.setEmail(addDto.getEmail());
        user.setPassword(addDto.getPassword());
        user.setPhoneNo(addDto.getPhoneNo());
        user.setUserType(addDto.getUserType());
        user.setUserId(addDto.getUserId());
        user.setUserName(addDto.getUserName());
        user.setMarketingAllowed(addDto.getMarketingAllowed());
        user.setMarketingAllowedAt(addDto.getMarketingAllowedAt());

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
        if(Objects.isNull(user)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다.");
        }

        user.setUserType(UserType.NORMAL);
        user.setDeleted(YN.N);
        user.setDeleted(YN.N);
        log.error("user.getMarketingAllowed(): {}", user.getMarketingAllowed());
        if(user.getMarketingAllowed() == YN.Y) {
            user.setMarketingAllowedAt(dateUtil.getThisTime());
        } else {
            user.setMarketingAllowedAt(null);
        }
        user.setPassword(passwordUtil.encode(user.getPassword()));
        log.error("user: {}", user);
        Users addedUser = userRepos.save(user);
        if(Objects.isNull(addedUser)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "DB 저장 중 오류가 발생하였습니다.");
        }

        return addedUser;
    }

    @Override
    public Users find(long id, HttpServletRequest request) throws Exception {
        Users oldUser =
            userRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다."));

        return oldUser;
    }

    @Transactional
    @Override
    public Users editPassword(String userId, String newPass1, String newPass2,
            HttpServletRequest request) throws Exception {

        Users oldUser = userRepos.findByUserId(userId);
        if(Objects.isNull(oldUser)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다.");
        }
        if(!newPass1.equals(newPass2)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "두 비밀번호가 일치하지 않습니다.");
        }

        String newPass = passwordUtil.encode(newPass1);
        oldUser.setPassword(newPass);

        Users newUser = userRepos.save(oldUser);
        return newUser;
    }

    @Override
    public Users toEntityModify(RequestUsersModifyDTO modifyDto) {
        Users user = new Users();

        user.setEmail(modifyDto.getEmail());
        user.setPhoneNo(modifyDto.getPhoneNo());
        user.setMessageAllowed(modifyDto.getMessageAllowed());
        if(modifyDto.getMessageAllowed() == YN.Y) {
            user.setMessageAllowedAt(dateUtil.getThisTime());
        } else {
            user.setMessageAllowedAt(null);
        }
        user.setMarketingAllowed(modifyDto.getMarketingAllowed());
        if(modifyDto.getMarketingAllowed() == YN.Y) {
            user.setMarketingAllowedAt(dateUtil.getThisTime());
        } else {
            user.setMarketingAllowedAt(null);
        }

        return user;
    }

    @Transactional
    @Override
    public Users edit(Users user, HttpServletRequest request) throws Exception {
        Users oldUser = 
            userRepos.findById(user.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다."));

        oldUser.edit(user);

        Users editedUser = userRepos.save(oldUser);
        return editedUser;
    }

    @Override
    public void remove(long id, HttpServletRequest request) {
        Users user =
            userRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다."));

        user.setDeleted(YN.Y);
        userRepos.save(user);
    }

    @Override
    public Users generate(Users oldUser) {
        return null;
    }

    @Override
    public ListResponse<Users> findAll(Users user, Pages pages, HttpServletRequest request) {
        long count = userDslRepos.countAll(user);
        List<Users> list = userDslRepos.findAll(user, pages);

        return new ListResponse(count, list, pages);
    }

}