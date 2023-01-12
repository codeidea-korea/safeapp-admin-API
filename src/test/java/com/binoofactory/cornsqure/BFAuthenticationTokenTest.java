package com.binoofactory.cornsqure;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.binoofactory.cornsqure.utils.BfJwtUtil;
import com.binoofactory.cornsqure.web.model.entity.Users;

public class BFAuthenticationTokenTest {
    
    public static void main(String[] args) {
        
        BfJwtUtil jwtUtil = new BfJwtUtil();
        jwtUtil.jwtKey = "3B1B7D2AA409A0B810B6A2FDAE34B5117784306E3B1B7D2AA409A0B810B6A2FDAE34B5117784306E3B1B7D2AA409A0B810B6A2FDAE34B5117784306E3B1B7D2AA409A0B810B6A2FDAE34B5117784306E";
        
        String token = jwtUtil.generateToken(Users.builder()
            .id(50L)
            .userId("botbinoo@naver.com")
            .build(), 5000);
        
        Assertions.assertNotNull(token);
        System.out.println(token);
        
        String auth = "Bearer " + token;
        auth.replace(auth, "bearer ").replace(auth, "Bearer ");
        // eyJhbGciOiJIUzUxMiJ9.eyJpZCI6ImJvdGJpbm9vQG5hdmVyLmNvbSIsInMiOjUwLCJpYXQiOjE2NzE4ODU0NTgsImV4cCI6MTY3MTg4NTQ2M30.qSukCF1FjQwkbEMroEOiPTA-OGpWTGAU50DU90Ej30nAXfFVTg-TLHjKf1-wr0iGeYBde43oGQN_RPHAhtCWhQ
        // eyJhbGciOiJIUzI1NiJ9.eyJrZXkiOiJ2YWx1ZSIsImV4cCI6MTYxMDk1OTUxMiwiaXNzIjoibW9vbnNpcmkifQ.SaVEHFP3I7c5tRnPC096eWMj2-ulWSOa5xhsddvHfIM
        
        String user = jwtUtil.getUserId(token);
        System.out.println(user);
    }
}
