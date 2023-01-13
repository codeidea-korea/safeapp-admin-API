package com.safeapp.admin.web.service.cmmn;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.data.UserType;
import com.safeapp.admin.web.model.cmmn.Token;
import com.safeapp.admin.web.model.entity.Users;

public interface JwtService {
    Users getUserInfoByToken(HttpServletRequest httpServletRequest);
    
    Users getUserInfoByTokenAnyway(HttpServletRequest httpServletRequest);
    
    boolean checkUserTokenPriority(HttpServletRequest httpServletRequest, UserType lessType);

	Token generateToken(Users user, String userCode);
    
    boolean checkTokenExpired(HttpServletRequest httpServletRequest);

    Token generateTokenByRefreshToken(String refreshToken);
}
