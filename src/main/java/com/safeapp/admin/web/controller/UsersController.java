package com.safeapp.admin.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.data.SNSType;
import com.safeapp.admin.web.dto.request.RequestSNSUserDTO;
import com.safeapp.admin.web.dto.request.RequestUserDTO;
import com.safeapp.admin.utils.ResponseUtil;
import com.safeapp.admin.web.model.cmmn.BfListResponse;
import com.safeapp.admin.web.model.cmmn.BfPage;
import com.safeapp.admin.web.model.cmmn.Token;
import com.safeapp.admin.web.model.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safeapp.admin.web.service.UserService;
import com.safeapp.admin.web.service.cmmn.JwtService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api(tags = {"User"}, description = "사용자 계정", basePath = "")
public class UsersController extends ABSAdminUserController {

    @Autowired
    public UsersController(UserService userService, JwtService jwtService) {
        super(userService, jwtService);
    }

    @PostMapping(value = "/user/join")
    @ApiOperation(value = "회원가입", notes = "일반 사용자 계정 가입")
    public ResponseEntity join(
        @RequestBody RequestUserDTO dto) throws Exception {
        Users user = userService.toEntity(dto);
        return ResponseUtil.sendResponse(userService.add(user, null));
    }

    @PostMapping(value = "/user/sns-join")
    @ApiOperation(value = "SNS회원가입", notes = "SNS 사용자 계정 가입")
    public ResponseEntity snsJoin(
            @RequestBody RequestSNSUserDTO dto) throws Exception {
        return ResponseUtil.sendResponse(userService.snsAdd(dto, null));
    }

    @PutMapping(value = "/user")
    @ApiOperation(value = "수정", notes = "수정")
    public ResponseEntity modify(
        @RequestBody Users user, HttpServletRequest httpServletRequest) throws Exception {
        return ResponseUtil.sendResponse(userService.edit(user, httpServletRequest));
    }

    @DeleteMapping(value = "/user")
    @ApiOperation(value = "탈퇴", notes = "탈퇴")
    public ResponseEntity remove(HttpServletRequest httpServletRequest) throws Exception {
        Users user = jwtService.getUserInfoByToken(httpServletRequest);
        userService.remove(user.getId(), httpServletRequest);
        return ResponseUtil.sendResponse(null);
    }

    @PostMapping(value = "/user/login")
    @ApiOperation(value = "사용자 로그인+토큰 발급", notes = "사용자 로그인+토큰 발급")
    public ResponseEntity login(
        @RequestParam(value = "username", required = true, defaultValue = "test1") @ApiParam("사용자아이디") String username,
        @RequestParam(value = "password", required = true, defaultValue = "1234qwer") @ApiParam("비밀번호") String password,
        HttpServletRequest httpServletRequest) throws Exception {
        return super.login(username, password, httpServletRequest);
    }

    @PostMapping(value = "/user/sns-login")
    @ApiOperation(value = "SNS 로그인+토큰 발급", notes = "SNS 로그인+토큰 발급")
    public Token snsLogin(
            @RequestParam(value = "sns_value", required = true, defaultValue = "snstest") @ApiParam("사용자아이디") String snsValue,
            @RequestParam(value = "sns_type", required = true, defaultValue = "KAKAO") @ApiParam("사용자아이디") SNSType snsType,
            HttpServletRequest httpServletRequest) throws Exception {
        return userService.snsLogin(snsValue, snsType, httpServletRequest);
    }

    @GetMapping(value = "/user/check-duplicate/{userId}")
    @ApiOperation(value = "사용자 계정 중복 확인", notes = "사용자 계정의 중복을 검사한다")
    public ResponseEntity checkDuplicated(
        @PathVariable("userId") @ApiParam("사용자 아이디") String userId) throws Exception {
        return super.checkDuplicated(userId);
    }

    @GetMapping(value = "/user/me")
    @ApiOperation(value = "나의 계정 정보 조회", notes = "나의 계정 정보 조회")
    public ResponseEntity findMe(
        HttpServletRequest httpServletRequest) throws Exception {
        return super.findMe(httpServletRequest);
    }

    @GetMapping(value = "/users")
    @ApiOperation(value = "(관리자) 사용자 계정 정보 조회", notes = "(관리자) 사용자 계정 정보 조회")
    public ResponseEntity<BfListResponse<Users>> findUsersByAdmin(
        @RequestParam(value = "pageNo", required = false, defaultValue = "1") @ApiParam("페이지 번호") int pageNo,
        @RequestParam(value = "pageSize", required = false, defaultValue = "100") @ApiParam("한번에 보여줄 크기") int pageSize,
        HttpServletRequest request) throws Exception {

        return ResponseUtil.sendResponse(userService.findAll(
            new Users(),
            new BfPage(pageNo, pageSize),
            request));
    }

    @GetMapping(value = "/users/{id}")
    @ApiOperation(value = "(관리자) 사용자 계정 정보 조회", notes = "(관리자) 사용자 계정 정보 조회")
    public ResponseEntity<Users> findUserId(
        @PathVariable("id") @ApiParam("사용자 식별자") long id, HttpServletRequest httpServletRequest)
        throws Exception {
        return ResponseUtil.sendResponse(userService.find(id, httpServletRequest));
    }
    
    @PatchMapping(value = "/user/change/password")
    @ApiOperation(value = "비밀번호 수정", notes = "비밀번호 수정")
    public ResponseEntity modifyPassword(
        @RequestParam(value = "user_id", required = true, defaultValue = "1") String user_id,
        @RequestParam(value = "password", required = true, defaultValue = "1") String password,
        @RequestParam(value = "newpassword", required = true, defaultValue = "1") String newpassword,
        HttpServletRequest httpServletRequest) throws Exception {
        
        return ResponseUtil.sendResponse(userService.modifyPassword(user_id, password, newpassword, httpServletRequest));
    }
    
    @GetMapping(value = "/auth/user-phone/request-number")
    @ApiOperation(value = "핸드폰 본인인증 번호 요청", notes = "핸드폰 본인인증 번호 요청")
    public ResponseEntity sendAuthSMSCode(
        @RequestParam(value = "phone_no", required = true) String phone_no,
        HttpServletRequest httpServletRequest) throws Exception {
        
        return ResponseUtil.sendResponse(userService.sendAuthSMSCode(phone_no));
    }
    
    @PostMapping(value = "/auth/user-phone/check-number")
    @ApiOperation(value = "핸드폰 본인인증 번호 확인", notes = "핸드폰 본인인증 번호 확인")
    public ResponseEntity modifyPassword(
        @RequestParam(value = "phone_no", required = true) String phone_no,
        @RequestParam(value = "auth_no", required = true) String auth_no,
        HttpServletRequest httpServletRequest) throws Exception {
        
        return ResponseUtil.sendResponse(userService.isCurrectSMSCode(phone_no, auth_no));
    }
    
    @GetMapping(value = "/user/find/id")
    @ApiOperation(value = "아이디 찾기", notes = "아이디 찾기")
    public ResponseEntity findByNameAndPhoneNo(
        @RequestParam(value = "user_name", required = true, defaultValue = "1") String user_name,
        @RequestParam(value = "phone_no", required = true, defaultValue = "1") String phone_no,
        HttpServletRequest httpServletRequest) throws Exception {
        
        return ResponseUtil.sendResponse(userService.findByNameAndPhoneNo(user_name, phone_no));
    }
    
    @GetMapping(value = "/user/check/not-expired")
    @ApiOperation(value = "토큰 만료여부 체크", notes = "토큰 만료여부 체크")
    public ResponseEntity checkTokenExpired(HttpServletRequest httpServletRequest) throws Exception {
        return ResponseUtil.sendResponse(!jwtService.checkTokenExpired(httpServletRequest));
    }

    @PostMapping(value = "/user/regenerate-token/{refresh_token}")
    @ApiOperation(value = "사용자 토큰 재발급", notes = "사용자 토큰 재발급")
    public ResponseEntity regenerateToken(
        @PathVariable("refresh_token") @ApiParam(value = "리프레쉬 토큰", required = true) String refresh_token,
        HttpServletRequest httpServletRequest) throws Exception {        
        return ResponseUtil.sendResponse(jwtService.generateTokenByRefreshToken(refresh_token));
    }
}
