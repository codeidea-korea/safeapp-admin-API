package com.safeapp.admin.web.service.impl;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.dto.request.RequestUsersDTO;
import com.safeapp.admin.utils.DateUtil;
import com.safeapp.admin.utils.PasswordUtil;
import com.safeapp.admin.web.data.UserType;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.request.RequestUsersEditDTO;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.SmsAuthHistory;
import com.safeapp.admin.web.model.entity.Users;
import com.safeapp.admin.web.repos.direct.DirectQuery;
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

    private final DirectSendAPIService directSendAPIService;

    private final PasswordUtil passwordUtil;
    private final DateUtil dateUtil;

    private final UserRepos userRepos;
    private final SmsAuthHistoryRepos smsAuthHistoryRepos;
    private final DirectQuery dirRepos;

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

    public Users toAddEntity(RequestUsersDTO addDto) {
        Users newUser = new Users();

        newUser.setEmail(addDto.getEmail());
        newUser.setPassword(addDto.getPassword());
        newUser.setPhoneNo(addDto.getPhoneNo());
        newUser.setUserType(addDto.getUserType());
        newUser.setUserId(addDto.getUserId());
        newUser.setUserName(addDto.getUserName());
        newUser.setMarketingAllowed(addDto.getMarketingAllowed());
        newUser.setMarketingAllowedAt(addDto.getMarketingAllowedAt());
        newUser.setMaxProjectGroupCount(0);

        return newUser;
    }

    @Transactional
    @Override
    public Users add(Users newUser, HttpServletRequest request) throws Exception {
        if(StringUtils.isNullOrEmpty(newUser.getUserId())) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "아이디를 입력해주세요.");
        }
        if(!chkUserId(newUser.getUserId())) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "중복된 아이디입니다.");
        }
        if(Objects.isNull(newUser)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다.");
        }

        newUser.setUserType(UserType.NORMAL);
        newUser.setDeleted(YN.N);
        newUser.setDeleteYn(false);
        if(newUser.getMarketingAllowed() == YN.Y) {
            newUser.setMarketingAllowedAt(dateUtil.getThisTime());
        } else {
            newUser.setMarketingAllowedAt(null);
        }
        newUser.setPassword(passwordUtil.encode(newUser.getPassword()));

        Users addedUser = userRepos.save(newUser);
        if(Objects.isNull(addedUser)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "DB 저장 중 오류가 발생하였습니다.");
        }

        return addedUser;
    }

    @Override
    public Users find(long id, HttpServletRequest request) throws Exception {
        Users user =
            userRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다."));

        return user;
    }

    @Override
    public Map<String, Object> findMyAuth(long id, HttpServletRequest request) {
        Map<String, Object> myAuth = dirRepos.findMyAuth(id);

        return myAuth;
    }

    @Override
    public long countMyProjectList(long id, HttpServletRequest request) {
        long count = dirRepos.countMyProjectList(id);

        return count;
    }

    @Override
    public List<Map<String, Object>> findMyProjectList(long id, int pageNo, int pageSize, HttpServletRequest request) {
        Pages pages = new Pages(pageNo, pageSize);
        List<Map<String, Object>> myProjectList = dirRepos.findMyProjectList(id, pages);

        return myProjectList;
    }

    @Transactional
    @Override
    public Users editPassword(String userId, String newPass1, String newPass2,
            HttpServletRequest request) throws Exception {

        Users user = userRepos.findByUserId(userId);
        if(Objects.isNull(user)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다.");
        }
        if(!newPass1.equals(newPass2)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "두 비밀번호가 일치하지 않습니다.");
        }

        String newPass = passwordUtil.encode(newPass1);
        user.setPassword(newPass);

        Users editedUser = userRepos.save(user);
        return editedUser;
    }

    @Override
    public Users toEditEntity(RequestUsersEditDTO editDto) {
        Users newUser = new Users();

        newUser.setUserName(editDto.getUserName());
        newUser.setEmail(editDto.getEmail());
        newUser.setPhoneNo(editDto.getPhoneNo());
        newUser.setEmailAllowed(editDto.getEmailAllowed());
        newUser.setMarketingAllowed(editDto.getMarketingAllowed());
        if(editDto.getMarketingAllowed() == YN.Y) {
            newUser.setMarketingAllowedAt(dateUtil.getThisTime());
        } else {
            newUser.setMarketingAllowedAt(null);
        }

        return newUser;
    }

    @Transactional
    @Override
    public Users edit(Users newUser, HttpServletRequest request) throws Exception {
        Users user =
            userRepos.findById(newUser.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다."));

        user.edit(newUser);

        Users editedUser = userRepos.save(user);
        return editedUser;
    }

    @Override
    public void remove(long id, HttpServletRequest request) {
        Users user =
            userRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다."));

        user.setDeleted(YN.Y);
        user.setDeleteYn(true);

        userRepos.save(user);
    }

    @Override
    public ListResponse<Users> findAll(Users user, Pages pages, HttpServletRequest request) {
        long count = dirRepos.countUserList(user);

        List<Map<String, Object>> userList = dirRepos.findUserList(user, pages);
        userList.forEach(u -> {
            u.put("myProjectCnt", dirRepos.countProjectList(Long.parseLong(u.get("id").toString())));
        });

        return new ListResponse(count, userList, pages);
    }

    @Override
    public Users generate(Users newUser) { return null; }

}