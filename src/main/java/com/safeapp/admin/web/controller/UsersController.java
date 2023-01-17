package com.safeapp.admin.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.dto.request.RequestUserDTO;
import com.safeapp.admin.utils.ResponseUtil;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api(tags = "User")
public class UsersController {

    private final UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/user/chk/{userID}")
    @ApiOperation(value = "신규회원 등록 시 아이디 중복여부 확인", notes = "신규회원 등록 시 아이디 중복여부 확인")
    public ResponseEntity chkUserID(
            @PathVariable("userID") @ApiParam("회원 아이디") String userID) throws Exception {

        return ResponseUtil.sendResponse(userService.chkUserID(userID));
    }

    @PostMapping(value = "/user/add")
    @ApiOperation(value = "신규회원 등록", notes = "신규회원 등록")
    public ResponseEntity add(@RequestBody RequestUserDTO dto) throws Exception {
        Users user = userService.toEntity(dto);

        return ResponseUtil.sendResponse(userService.add(user, null));
    }

    @GetMapping(value = "/user/find/{id}")
    @ApiOperation(value = "회원정보 확인(+ 수정)", notes = "회원정보 확인(+ 수정)")
    public ResponseEntity<Users> findUserId(@PathVariable("id") @ApiParam("회원 PK") long id,
            HttpServletRequest httpServletRequest) throws Exception {

        return ResponseUtil.sendResponse(userService.find(id, httpServletRequest));
    }

    @PatchMapping(value = "/user/editPassword")
    @ApiOperation(value = "회원 비밀번호 수정", notes = "회원 비밀번호 수정")
    public ResponseEntity editPassword(
            @RequestParam(value = "userID", defaultValue = "1") String userID,
            @RequestParam(value = "password", defaultValue = "123abcABC!@#") String password,
            @RequestParam(value = "newPassword", defaultValue = "123abcABC!@#") String newPassword,
            HttpServletRequest httpServletRequest) throws Exception {

        return ResponseUtil.sendResponse(userService.editPassword(userID, password, newPassword, httpServletRequest));
    }

    @PutMapping(value = "/user/edit")
    @ApiOperation(value = "회원정보 수정", notes = "회원정보 수정")
    public ResponseEntity edit(@RequestBody Users user, HttpServletRequest httpServletRequest) throws Exception {

        return ResponseUtil.sendResponse(userService.edit(user, httpServletRequest));
    }

    @DeleteMapping(value = "/user/remove")
    @ApiOperation(value = "회원 삭제", notes = "회원 삭제")
    public ResponseEntity remove(HttpServletRequest httpServletRequest) throws Exception {

        return ResponseUtil.sendResponse(null);
    }

    @GetMapping(value = "/user/list")
    @ApiOperation(value = "회원 목록 조회", notes = "회원 목록 조회")
    public ResponseEntity<ListResponse<Users>> findAll(
            @RequestParam(value = "pageNo", required = false, defaultValue = "1") @ApiParam("페이지 번호") int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "100") @ApiParam("한번에 보여줄 크기") int pageSize,
            HttpServletRequest request) throws Exception {

        return ResponseUtil.sendResponse(userService.findAll(new Users(), new Pages(pageNo, pageSize),request));
    }
    
    @GetMapping(value = "/user/requestNumber")
    @ApiOperation(value = "핸드폰 본인인증 번호 요청", notes = "핸드폰 본인인증 번호 요청")
    public ResponseEntity sendAuthSMSCode(@RequestParam(value = "phoneNo") String phoneNo) throws Exception {
        
        return ResponseUtil.sendResponse(userService.sendAuthSMSCode(phoneNo));
    }
    
    @PostMapping(value = "/user/responseNumber")
    @ApiOperation(value = "핸드폰 본인인증 번호 확인", notes = "핸드폰 본인인증 번호 확인")
    public ResponseEntity isCurrectSMSCode(@RequestParam(value = "phoneNo") String phoneNo,
            @RequestParam(value = "authNo") String authNo) throws Exception {
        
        return ResponseUtil.sendResponse(userService.isCorrectSMSCode(phoneNo, authNo));
    }

}