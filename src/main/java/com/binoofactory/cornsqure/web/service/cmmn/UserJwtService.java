package com.binoofactory.cornsqure.web.service.cmmn;

import javax.servlet.http.HttpServletRequest;

import com.binoofactory.cornsqure.web.data.UserType;
import com.binoofactory.cornsqure.web.model.cmmn.BfToken;
import com.binoofactory.cornsqure.web.model.entity.Users;

public interface UserJwtService {
    Users getUserInfoByToken(HttpServletRequest httpServletRequest);
    
    Users getUserInfoByTokenAnyway(HttpServletRequest httpServletRequest);
    
    boolean checkUserTokenPriority(HttpServletRequest httpServletRequest, UserType lessType);

	BfToken generateToken(Users user, String userCode);
    
    boolean checkTokenExpired(HttpServletRequest httpServletRequest);

    BfToken generateTokenByRefreshToken(String refreshToken);
}
