package com.safeapp.admin.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.utils.ResponseUtil;
import com.safeapp.admin.web.data.UserType;
import com.safeapp.admin.web.model.entity.Users;
import com.safeapp.admin.web.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;

import com.safeapp.admin.web.service.UserService;
import com.safeapp.admin.web.service.cmmn.JwtService;

public abstract class ABSAdminUserController {

    protected final AdminService adminService;
    protected final JwtService jwtService;

    public ABSAdminUserController(AdminService adminService, JwtService jwtService) {
        this.adminService = adminService;
        this.jwtService = jwtService;
    }

    public ResponseEntity chkAdminID(String adminID) throws Exception {

        return ResponseUtil.sendResponse(adminService.chkAdminID(adminID));
    }

    public ResponseEntity findMe(HttpServletRequest request) throws Exception {

        return ResponseUtil.sendResponse(adminService.findMe(request));
    }

}