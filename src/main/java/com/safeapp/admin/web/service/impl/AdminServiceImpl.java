package com.safeapp.admin.web.service.impl;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.dto.request.RequestUserDTO;
import com.safeapp.admin.utils.DateUtil;
import com.safeapp.admin.utils.PasswordUtil;
import com.safeapp.admin.web.data.UserType;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.cmmn.BfListResponse;
import com.safeapp.admin.web.model.cmmn.BfPage;
import com.safeapp.admin.web.model.entity.Admins;
import com.safeapp.admin.web.model.entity.Users;
import com.safeapp.admin.web.repos.jpa.AdminRepos;
import com.safeapp.admin.web.repos.jpa.SmsAuthHistoryRepos;
import com.safeapp.admin.web.repos.jpa.UserRepos;
import com.safeapp.admin.web.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.safeapp.admin.web.repos.mongo.LoginHistoryRepos;
import com.safeapp.admin.web.service.cmmn.DirectSendAPIService;
import com.safeapp.admin.web.service.cmmn.JwtService;
import com.querydsl.core.util.StringUtils;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepos adminRepos;

    private final LoginHistoryRepos loginHistoryRepos;

    private final SmsAuthHistoryRepos smsAuthHistoryRepos;

    private final DirectSendAPIService directSendAPIService;

    private final JwtService jwtService;

    private final PasswordUtil passwordUtil;

    private final DateUtil dateUtil;

    @Autowired
    public AdminServiceImpl(UserRepos userRepos, LoginHistoryRepos loginHistoryRepos,
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

    @Override
    public boolean chkAdminID(String adminID) {
        Users userInfos = userRepos.findByUserId(userId);

        if (! Objects.isNull(userInfos)) {
            return false;
        }

        return true;
    }

    @Transactional
    @Override
    public Admins add(Admins admin, HttpServletRequest httpServletRequest) throws Exception {

        if(Objects.isNull(admin)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "관리자 정보가 없습니다.");
        }

        if(admin.getType() != UserType.ADMIN) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "관리자 정보가 아닌 정보로는 가입하실 수 없습니다.");
        }

        if(StringUtils.isNullOrEmpty(admin.getUserId())){
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "ID를 입력해주세요.");
        }

        if(!chkAdminID(admin.getUserId())){
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "중복된 아이디입니다.");
        }

        // TODO: validation
        user.setType(UserType.NORMAL);
        user.setDeleted(YN.N);
        if (user.getMarketingAllowed() == YN.Y) {
            user.setMarketingAllowedAt(dateUtil.getThisTime());
        }
        if (user.getMessageAllowed() == YN.Y) {
            user.setMessageAllowedAt(dateUtil.getThisTime());
        }
        user.setPassword(passwordUtil.encode(user.getPassword()));
        Users userInfo = userRepos.save(user);

        if (Objects.isNull(userInfo)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "저장중 오류가 발생했습니다.");
        }

        return userInfo;
    }\

    @Transactional
    @Override
    public Users edit(Users user, HttpServletRequest httpServletRequest) throws Exception {
        Users userInfos = userRepos.findByUserId(user.getUserId());

        if (Objects.isNull(userInfos)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }

        Users accessUser = jwtService.getUserInfoByToken(httpServletRequest); // 현재 접근 유저
        if (userInfos.getId() != accessUser.getId()
                && accessUser.getType().getCode() < UserType.ADMIN.getCode()) {
            // NOTICE: 접근한 사람이 본인이 아니면서, 일반 회원이거나 비회원인 경우 수정 불가능함 <-- 남의 정보 수정 X

            throw new HttpServerErrorException(HttpStatus.UNAUTHORIZED, "권한이 부족합니다.");
        }

        user.setPassword(passwordUtil.encode(user.getPassword()));
        user = userRepos.save(user);
        return user;
    }

    @Override
    public void remove(long seq, HttpServletRequest httpServletRequest) {
        Users userInfos = userRepos.findById(seq).orElse(null);

        if (Objects.isNull(userInfos)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }
        // TODO 관리자이거나 자기 자신인 경우만 수정이 가능함. 토큰으로 확인할 예정

        Users accessUser = jwtService.getUserInfoByToken(httpServletRequest); // 현재 접근 유저
        if (userInfos.getId() != accessUser.getId()
                && accessUser.getType().getCode() < UserType.ADMIN.getCode()) {
            // NOTICE: 접근한 사람이 본인이 아니면서, 일반 회원이거나 비회원인 경우 수정 불가능함 <-- 남의 정보 수정 X

            throw new HttpServerErrorException(HttpStatus.UNAUTHORIZED, "권한이 부족합니다.");
        }

        if (userInfos.getType() == UserType.NORMAL) {
            userInfos.setDeleted(YN.Y);
            userRepos.save(userInfos);
        }
    }

    @Override
    public Users find(long seq, HttpServletRequest httpServletRequest) throws Exception {
        boolean isUseable = jwtService.checkUserTokenPriority(httpServletRequest, UserType.ADMIN);
        if (!isUseable) {
            throw new HttpServerErrorException(HttpStatus.UNAUTHORIZED, "권한이 부족합니다.");
        }
        Users oldUser = userRepos.findById(seq).orElse(null);
        if (Objects.isNull(oldUser) || oldUser.getType() != UserType.ADMIN) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "가입되지 않은 회원입니다.");
        }
        return oldUser;
    }

    @Override
    public BfListResponse<Users> findAll(Users user, BfPage bfPage, HttpServletRequest httpServletRequest)
            throws Exception {

        boolean isUseable = jwtService.checkUserTokenPriority(httpServletRequest, UserType.ADMIN);
        if (!isUseable) {
            throw new HttpServerErrorException(HttpStatus.UNAUTHORIZED, "권한이 부족합니다.");
        }

        return null;
    }

    @Transactional
    @Override
    public Users modifyPassword(String userId, String password, String newpassword, HttpServletRequest httpServletRequest) throws Exception {
        Users myInfo = jwtService.getUserInfoByTokenAnyway(httpServletRequest);
        if (myInfo == null || myInfo.getType() == UserType.NONE) {
            // 회원가입/로그인에서 진입시 토큰 없을 수 있음.
            myInfo = userRepos.findByUserId(userId);
            if(myInfo == null) {
                throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 계정입니다.");
            }
            String passwordCheck = passwordUtil.encode(password);
            String passwordConfirm = passwordUtil.encode(newpassword);
            if(!passwordCheck.equals(passwordConfirm)) {
                throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
            }
            myInfo.setPassword(passwordCheck);
            return userRepos.save(myInfo);
        }

        // 마이페이지 같은 곳에서 진입시
        if(myInfo.getPassword().equals(passwordUtil.encode(password))) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "기존 비밀번호가 일치하지 않습니다.");
        }
        myInfo.setPassword(passwordUtil.encode(newpassword));
        return userRepos.save(myInfo);
    }

    public Users toEntity (RequestUserDTO dto){
        Users user = new Users();
        user.setType(dto.getType());
        user.setUserId(dto.getUserId());
        user.setPhoneNo(dto.getPhoneNo());
        user.setUserName(dto.getUserName());
        user.setEmail(dto.getEmail());
        user.setSnsAllowed(dto.getSnsAllowed());
        user.setMarketingAllowed(dto.getMarketingAllowed());
        user.setMarketingAllowedAt(dto.getMarketingAllowedAt());
        user.setMessageAllowed(dto.getMessageAllowed());
        user.setMessageAllowedAt(dto.getMessageAllowedAt());
        user.setPassword(dto.getPassword());
        user.setSnsType(dto.getSnsType());

        return user;
    }

}