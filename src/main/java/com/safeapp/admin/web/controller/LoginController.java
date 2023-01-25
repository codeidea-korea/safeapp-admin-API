package com.safeapp.admin.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.utils.ResponseUtil;
import com.safeapp.admin.web.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/login")
@Api(tags = {"Login"}, description = "로그인")
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping(value = "")
    @ApiOperation(value = "로그인", notes = "로그인")
    public ResponseEntity login(
            @RequestParam(value = "email", defaultValue = "admin1@codeidea.dev") @ApiParam("이메일") String email,
            @RequestParam(value = "password", defaultValue = "admin1_") @ApiParam("비밀번호") String password,
            HttpServletRequest request) throws Exception {

        return ResponseUtil.sendResponse(loginService.login(email, password, request));
    }

    @GetMapping(value = "/findMe")
    @ApiOperation(value = "로그인된 계정정보 조회", notes = "로그인된 계정정보 조회")
    public ResponseEntity findMe(HttpServletRequest request) {
        
        return ResponseUtil.sendResponse(loginService.findMe(request));
    }

    /*
    @GetMapping(value = "/login/findEmail")
    @ApiOperation(value = "이메일 찾기", notes = "이메일 찾기")
    public ResponseEntity findEmail(
            @RequestParam(value = "user_name", required = true, defaultValue = "1") String user_name,
            @RequestParam(value = "phone_no", required = true, defaultValue = "1") String phone_no,
            HttpServletRequest httpServletRequest) throws Exception {

        return ResponseUtil.sendResponse(userService.findByNameAndPhoneNo(user_name, phone_no));
    }

    @GetMapping(value = "/login/findPW/reqAuthNum")
    @ApiOperation(value = "비밀번호 찾기 - 휴대폰 본인인증 인증번호 요청", notes = "비밀번호 찾기 - 휴대폰 본인인증 인증번호 요청")
    public ResponseEntity reqAuthNum(@RequestParam(value = "phone_no", required = true) String phone_no) throws Exception {

        return ResponseUtil.sendResponse(userService.sendAuthSMSCode(phone_no));
    }

    @PostMapping(value = "/login/findPW/chkAuthNum")
    @ApiOperation(value = "비밀번호 찾기 - 휴대폰 본인인증 인증번호 검증", notes = "비밀번호 찾기 - 휴대폰 본인인증 인증번호 검증")
    public ResponseEntity chkAuthNum(
            @RequestParam(value = "phone_no") String phone_no,
            @RequestParam(value = "auth_no") String auth_no) throws Exception {

        return ResponseUtil.sendResponse(userService.isCurrectSMSCode(phone_no, auth_no));
    }

    @GetMapping(value = "/login/chkTokenExpired")
    @ApiOperation(value = "로그인용 토큰 만료여부 검증", notes = "로그인용 토큰 만료여부 검증")
    public ResponseEntity chkTokenExpired(HttpServletRequest httpServletRequest) {

        return ResponseUtil.sendResponse(!jwtService.checkTokenExpired(httpServletRequest));
    }

    @PostMapping(value = "/login/refreshToken")
    @ApiOperation(value = "사용자 토큰 재발급", notes = "사용자 토큰 재발급")
    public ResponseEntity refreshToken(
            @PathVariable("refresh_token") @ApiParam(value = "재발급용 토큰", required = true) String refresh_token) {

        return ResponseUtil.sendResponse(jwtService.generateTokenByRefreshToken(refresh_token));
    }
    */

}