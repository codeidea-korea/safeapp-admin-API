package com.safeapp.admin.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.utils.ResponseUtil;
import com.safeapp.admin.web.model.entity.Admins;
import com.safeapp.admin.web.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/login")
@Api(tags = {"Login"}, description = "로그인 관리")
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
    @ApiOperation(value = "로그인된 관리자 단독 조회", notes = "로그인된 관리자 단독 조회")
    public ResponseEntity findMe(HttpServletRequest request) {
        
        return ResponseUtil.sendResponse(loginService.findMe(request));
    }

    @GetMapping(value = "/findEmail")
    @ApiOperation(value = "이메일 찾기", notes = "이메일 찾기")
    public ResponseEntity findEmail(
            @RequestParam(value = "adminName") String adminName,
            @RequestParam(value = "phoneNo") String phoneNo,
            HttpServletRequest request) throws Exception {

        Admins admin = loginService.findEmail(adminName, phoneNo, request);
        return ResponseUtil.sendResponse(admin.getEmail());
    }

    @GetMapping(value = "/editPass")
    @ApiOperation(value = "비밀번호 재설정(가능 여부 확인)", notes = "비밀번호 재설정(가능 여부 확인)")
    public ResponseEntity editPass(
            @RequestParam(value = "email") String email,
            @RequestParam(value = "adminName") String adminName,
            @RequestParam(value = "phoneNo") String phoneNo,
            HttpServletRequest request) throws Exception {

        Admins admin = loginService.editPass(email, adminName, phoneNo, request);
        return ResponseUtil.sendResponse(admin.getEmail());
    }

}