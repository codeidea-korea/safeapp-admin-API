package com.safeapp.admin;

import com.safeapp.admin.web.model.entity.Admins;
import com.safeapp.admin.web.model.entity.Users;
import org.junit.jupiter.api.Assertions;

import com.safeapp.admin.utils.JwtUtil;

public class BFAuthenticationTokenTest {
    
    public static void main(String[] args) {
        JwtUtil jwtUtil = new JwtUtil();
        jwtUtil.jwtKey = "3B1B7D2AA409A0B810B6A2FDAE34B5117784306E3B1B7D2AA409A0B810B6A2FDAE34B5117784306E3B1B7D2AA409A0B810B6A2FDAE34B5117784306E3B1B7D2AA409A0B810B6A2FDAE34B5117784306E";
        
        String token =
            jwtUtil
            .generateAccessToken(Admins.builder()
            .id(50L)
            .adminID("botbinoo@naver.com")
            .build(), 5000);
        Assertions.assertNotNull(token);
        
        String auth = "Bearer " + token;
        auth.replace(auth, "bearer ").replace(auth, "Bearer ");
        
        String admin = jwtUtil.getAdminIDOrUserIDByAccessToken(token);
    }

}