package com.safeapp.admin.conf;

import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class AuthenticationToken extends AbstractAuthenticationToken {

    private String time;
    private String id;
    private String credentials;

    public AuthenticationToken(String time, String id, String credentials,
                               Collection<? extends GrantedAuthority> authorities) {

        super(authorities);

        this.time = time;
        this.id = id;
        this.credentials = credentials;
    }

    public AuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.id;
    }

}