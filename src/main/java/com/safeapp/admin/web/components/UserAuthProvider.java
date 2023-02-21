package com.safeapp.admin.web.components;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.safeapp.admin.web.service.OAuthService;

@Component
public class UserAuthProvider implements AuthenticationProvider {

    private final OAuthService oAuthService;

    @Autowired
    public UserAuthProvider(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String id = authentication.getName();
        String password = (String)authentication.getCredentials();

        UserDetails userDetails = oAuthService.loadUserByUsername(id);
        if(Objects.isNull(userDetails)) {
            throw new BadCredentialsException("Username is not found. Username = " + id);
        }

        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {

        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}