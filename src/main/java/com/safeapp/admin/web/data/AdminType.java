package com.safeapp.admin.web.data;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.Getter;

@Getter
public enum AdminType implements GrantedAuthority {

    ADMIN("관리자", 201);

    private final String description;
    private final int code;

    AdminType(String description, int code) {
        this.description = description;
        this.code = code;
    }

    @Override
    public String getAuthority() {
        return description;
    }

    public Collection<GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for(AdminType type : AdminType.values()) {
            if(type.code < this.code) {
                authorities.add(new SimpleGrantedAuthority(description));
            }
        }

        return authorities;
    }

}