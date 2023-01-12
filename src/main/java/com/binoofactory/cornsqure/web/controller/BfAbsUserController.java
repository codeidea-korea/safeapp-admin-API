package com.binoofactory.cornsqure.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;

import com.binoofactory.cornsqure.utils.ResponseUtil;
import com.binoofactory.cornsqure.web.data.UserType;
import com.binoofactory.cornsqure.web.model.cmmn.BfToken;
import com.binoofactory.cornsqure.web.model.entity.Users;
import com.binoofactory.cornsqure.web.service.UserService;
import com.binoofactory.cornsqure.web.service.cmmn.UserJwtService;

public abstract class BfAbsUserController {

    protected final UserService userService;

    protected final UserJwtService userJwtService;

    public BfAbsUserController(UserService userService, UserJwtService userJwtService) {
        this.userService = userService;
        this.userJwtService = userJwtService;
    }

    public ResponseEntity findMe(HttpServletRequest request) throws Exception {
        return ResponseUtil.sendResponse(userService.findMe(request));
    }

    public ResponseEntity login(String username, String password, HttpServletRequest request) throws Exception {
        Users user = new Users();
        user.setUserId(username);
        user.setPassword(password);

        return ResponseUtil.sendResponse(userService.login(user, request));
    }

    public ResponseEntity checkDuplicated(String userId) throws Exception {
        return ResponseUtil.sendResponse(userService.checkUserId(userId));
    }

    // NOTICE: 접근자가 예상되는 권한보다 더 높은 권한을 가진자인지 확인
    protected void checkUpperUserPriority(UserType userType, HttpServletRequest request) {
        if (!userJwtService.checkUserTokenPriority(request, userType)) {
            throw new HttpServerErrorException(HttpStatus.UNAUTHORIZED, "권한이 부족합니다.");
        }
    }

    protected boolean hasTokenExpired(HttpServletRequest request) {
        return userJwtService.checkTokenExpired(request);
    }
}
