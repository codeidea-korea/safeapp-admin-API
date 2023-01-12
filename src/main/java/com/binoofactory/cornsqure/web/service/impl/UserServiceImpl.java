package com.binoofactory.cornsqure.web.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.binoofactory.cornsqure.web.data.SNSType;
import com.binoofactory.cornsqure.web.dto.request.RequestSNSUserDTO;
import com.binoofactory.cornsqure.web.dto.request.RequestUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.binoofactory.cornsqure.utils.DateUtil;
import com.binoofactory.cornsqure.utils.PasswordUtil;
import com.binoofactory.cornsqure.web.data.UserType;
import com.binoofactory.cornsqure.web.data.YN;
import com.binoofactory.cornsqure.web.model.cmmn.BfListResponse;
import com.binoofactory.cornsqure.web.model.cmmn.BfPage;
import com.binoofactory.cornsqure.web.model.cmmn.BfToken;
import com.binoofactory.cornsqure.web.model.docs.LoginHistory;
import com.binoofactory.cornsqure.web.model.entity.SmsAuthHistory;
import com.binoofactory.cornsqure.web.model.entity.Users;
import com.binoofactory.cornsqure.web.repos.jpa.SmsAuthHistoryRepos;
import com.binoofactory.cornsqure.web.repos.jpa.UserRepos;
import com.binoofactory.cornsqure.web.repos.mongo.LoginHistoryRepos;
import com.binoofactory.cornsqure.web.service.UserService;
import com.binoofactory.cornsqure.web.service.cmmn.DirectSendAPIService;
import com.binoofactory.cornsqure.web.service.cmmn.UserJwtService;
import com.querydsl.core.util.StringUtils;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepos userRepos;

    private final LoginHistoryRepos loginHistoryRepos;

    private final SmsAuthHistoryRepos smsAuthHistoryRepos;

    private final DirectSendAPIService directSendAPIService;

    private final UserJwtService userJwtService;

    private final PasswordUtil passwordUtil;

    private final DateUtil dateUtil;

    @Autowired
    public UserServiceImpl(UserRepos userRepos, LoginHistoryRepos loginHistoryRepos,
        SmsAuthHistoryRepos smsAuthHistoryRepos, DirectSendAPIService directSendAPIService,
        UserJwtService userJwtService, PasswordUtil passwordUtil, DateUtil dateUtil) {

        this.userRepos = userRepos;
        this.loginHistoryRepos = loginHistoryRepos;
        this.userJwtService = userJwtService;
        this.smsAuthHistoryRepos = smsAuthHistoryRepos;
        this.directSendAPIService = directSendAPIService;
        this.passwordUtil = passwordUtil;
        this.dateUtil = dateUtil;
    }

    @Transactional
    @Override
    public Users add(Users user, HttpServletRequest httpServletRequest) throws Exception {

        if (Objects.isNull(user)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }

        if (user.getType() == UserType.ADMIN) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "관리자는 가입할 수 없습니다.");
        }
        
        if(StringUtils.isNullOrEmpty(user.getUserId())){
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "아이디를 입력해주세요.");
        }
        
        if(!checkUserId(user.getUserId())){
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
    }

    @Transactional
    @Override
    public Users snsAdd(RequestSNSUserDTO dto, HttpServletRequest httpServletRequest) throws Exception {
        Users user = this.toEntitySNS(dto);

        if (Objects.isNull(user)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }

        if (user.getType() == UserType.ADMIN) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "관리자는 가입할 수 없습니다.");
        }

        // TODO: validation
        user.setDeleted(YN.N);
        if (user.getMarketingAllowed() == YN.Y) {
            user.setMarketingAllowedAt(dateUtil.getThisTime());
        }
        if (user.getMessageAllowed() == YN.Y) {
            user.setMessageAllowedAt(dateUtil.getThisTime());
        }

        Users userInfo = userRepos.save(user);

        if (Objects.isNull(userInfo)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "저장중 오류가 발생했습니다.");
        }

        return userInfo;
    }

    @Transactional
    @Override
    public Users edit(Users user, HttpServletRequest httpServletRequest) throws Exception {
        Users userInfos = userRepos.findByUserId(user.getUserId());

        if (Objects.isNull(userInfos)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }

        Users accessUser = userJwtService.getUserInfoByToken(httpServletRequest); // 현재 접근 유저
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

        Users accessUser = userJwtService.getUserInfoByToken(httpServletRequest); // 현재 접근 유저
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
    public boolean checkUserId(String userId) {
        Users userInfos = userRepos.findByUserId(userId);

        if (! Objects.isNull(userInfos)) {
            return false;
        }
        return true;
    }

    @Override
    public Users findByUserId(String userId) throws Exception {
        return userRepos.findByUserId(userId);
    }
    // 이름이랑 휴대폰 번호로 아이디 찾기
    @Override
    public Users findByNameAndPhoneNo(String userName, String phoneNo) throws Exception {
        Users oldUser = userRepos.findByUserNameAndPhoneNoAndDeleted(userName, phoneNo, YN.N);
        if (Objects.isNull(oldUser)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "가입되지 않은 회원입니다.");
        }
        oldUser.setPassword("");
        return oldUser;
    }
    // 이메일/이름/휴대폰번호로 비밀번호 재설정 (인증키 검증후)
    @Override
    public boolean changePasswordByNameAndPhoneNo(String userName, String phoneNo) throws Exception {
        Users oldUser = userRepos.findByUserNameAndPhoneNoAndDeleted(userName, phoneNo, YN.N);
        if (Objects.isNull(oldUser)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "가입되지 않은 회원입니다.");
        }
        return true;
    }

    @Override
    public Users find(long seq, HttpServletRequest httpServletRequest) throws Exception {
        boolean isUseable = userJwtService.checkUserTokenPriority(httpServletRequest, UserType.ADMIN);
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

        boolean isUseable = userJwtService.checkUserTokenPriority(httpServletRequest, UserType.ADMIN);
        if (!isUseable) {
            throw new HttpServerErrorException(HttpStatus.UNAUTHORIZED, "권한이 부족합니다.");
        }

        return null;
    }

    @Override
    public Users findMe(HttpServletRequest httpServletRequest) {
        Users userInfos = userJwtService.getUserInfoByToken(httpServletRequest);

        if (Objects.isNull(userInfos)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }
        return userInfos;
    }

    @Override
    public BfToken login(Users user, HttpServletRequest httpServletRequest) throws Exception {

        String template = "Addr:" + httpServletRequest.getRemoteAddr()
            + "/Host:" + httpServletRequest.getRemoteHost()
            + "/User:" + httpServletRequest.getRemoteUser();

        // TODO Auto-generated method stub
        Users userInfos = userRepos.findByUserId(user.getUserId());

        if (Objects.isNull(userInfos) || userInfos.getDeleted() == YN.Y) {

            loginHistoryRepos.save(LoginHistory.builder()
                .createDt(LocalDateTime.now())
                .isSuccess(false)
                .userId(user.getUserId())
                .password(user.getPassword())
                .platformInfo(template)
                .build());
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }
        if (!passwordUtil.checkEqual(userInfos.getPassword(), user.getPassword())) {

            loginHistoryRepos.save(LoginHistory.builder()
                .createDt(LocalDateTime.now())
                .isSuccess(false)
                .userId(user.getUserId())
                .password(user.getPassword())
                .platformInfo(template)
                .build());
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "비밀번호가 틀립니다.");
        }
        BfToken token = userJwtService.generateToken(userInfos, template);

        loginHistoryRepos.save(LoginHistory.builder()
            .createDt(LocalDateTime.now())
            .isSuccess(true)
            .userId(user.getUserId())
            .password(user.getPassword())
            .platformInfo(template)
            .build());

        return token;
    }

    @Override
    public BfToken snsLogin(String snsValue, SNSType snsType, HttpServletRequest httpServletRequest) throws Exception {

        String template = "Addr:" + httpServletRequest.getRemoteAddr()
                + "/Host:" + httpServletRequest.getRemoteHost()
                + "/User:" + httpServletRequest.getRemoteUser();

        // TODO Auto-generated method stub
        Users userInfos = userRepos.findUsersBySnsValueAndSnsType(snsValue, snsType);

        if (Objects.isNull(userInfos) || userInfos.getDeleted() == YN.Y) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }

        BfToken token = userJwtService.generateToken(userInfos, template);

        loginHistoryRepos.save(LoginHistory.builder()
                .createDt(LocalDateTime.now())
                .isSuccess(true)
                .userId(userInfos.getUserId())
                .platformInfo(template)
                .build());

        return token;
    }

    @Override
    public Users generateUserinfo(long userSeq) {
        Users user = userRepos.findById(userSeq).orElse(null);
        if (Objects.isNull(user)) {
            return user;
        }
        user.setPassword("");
        return user;
    }

    @Override
    public Users generate(Users obj) {
        // TODO Auto-generated method stub
        return null;
    }

    @Transactional
    @Override
    public Users modifyPassword(String userId, String password, String newpassword, HttpServletRequest httpServletRequest) throws Exception {
        Users myInfo = userJwtService.getUserInfoByTokenAnyway(httpServletRequest);
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

    @Override
    public boolean sendAuthSMSCode(String phoneNo) throws Exception {

        LocalDateTime thisTime = dateUtil.getThisTime();
        String authCode = Math.round(Math.random() * 1000000) + "";

        SmsAuthHistory smsAuthHistory = SmsAuthHistory.builder()
            .createdAt(thisTime)
            .efectedEndedAt(thisTime.plusMinutes(3))
            .phoneNo(phoneNo)
            .authCode(authCode)
            .build();

        Map<String, String> bodyMap = new HashMap<String, String>();
        bodyMap.put("title", "인증번호입니다.");
        bodyMap.put("message", "인증번호는 [" + authCode + "]입니다.");
        bodyMap.put("name", "인증자");
        bodyMap.put("phoneNo", phoneNo);

        smsAuthHistoryRepos.save(smsAuthHistory);

        return directSendAPIService.sendSMS(phoneNo, bodyMap);
    }

    @Override
    public boolean isCurrectSMSCode(String phoneNo, String authNo) throws Exception {
        LocalDateTime thisTime = dateUtil.getThisTime();

        SmsAuthHistory history = smsAuthHistoryRepos.findFirstByPhoneNoAndEfectedEndedAtAfterOrderByIdDesc(phoneNo,
            thisTime);

        if (history == null) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "먼저 인증을 요청하여 주세요.");
        }
        if (history.getAuthCode().equals(authNo)) {
            return true;
        }
        return false;
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

    public Users toEntitySNS (RequestSNSUserDTO dto){
        Users user = new Users();
        user.setPhoneNo(dto.getPhoneNo());
        user.setUserName(dto.getUserName());
        user.setEmail(dto.getEmail());
        user.setMessageAllowed(dto.getMessageAllowed());
        user.setSnsValue(dto.getSnsValue());
        user.setType(dto.getUserType());
        user.setSnsType(dto.getSnsType());

        return user;
    }
}
