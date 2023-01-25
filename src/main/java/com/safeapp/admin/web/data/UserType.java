package com.safeapp.admin.web.data;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.Getter;

@Getter
public enum UserType implements GrantedAuthority {

	NONE("비회원", 1), 
    NORMAL("일반회원", 101), 
    PERSONAL("Personal 회원", 111),
    TEAM_USER("Team 회원(그룹 구성원)", 121), 
    TEAM_MANAGER("Team 회원(그룹 관리자)", 122),
    TEAM_MASTER("Team 회원(그룹 마스터관리자 = 결제자)", 129);
	
	private final String description;
    private final int code;
	
	UserType(String description, int code) {
		this.description = description;
		this.code = code;
	}

    @Override
    public String getAuthority() {
        return description;
    }

}