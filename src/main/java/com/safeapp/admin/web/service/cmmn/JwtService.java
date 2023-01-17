package com.safeapp.admin.web.service.cmmn;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.data.AdminType;
import com.safeapp.admin.web.data.UserType;
import com.safeapp.admin.web.model.cmmn.Token;
import com.safeapp.admin.web.model.entity.Admins;
import com.safeapp.admin.web.model.entity.Users;

public interface JwtService {

    Admins getAdminInfoByToken(HttpServletRequest httpServletRequest);

    Admins getAdminInfoByTokenAnyway(HttpServletRequest httpServletRequest);
    
    boolean checkAdminTokenPriority(HttpServletRequest httpServletRequest, AdminType lessType);

    Users getUserInfoByToken(HttpServletRequest httpServletRequest);

    Users getUserInfoByTokenAnyway(HttpServletRequest httpServletRequest);

    boolean checkUserTokenPriority(HttpServletRequest httpServletRequest, UserType lessType);

	Token generateAccessToken(Admins admins, String adminCode);

    Token generateAccessTokenByRefreshToken(String refreshToken);
    
    boolean checkTokenExpired(HttpServletRequest httpServletRequest);

}