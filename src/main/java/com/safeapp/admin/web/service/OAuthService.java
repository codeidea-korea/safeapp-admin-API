package com.safeapp.admin.web.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface OAuthService extends UserDetailsService {

    @Override
    UserDetails loadUserByUsername(String username);

}