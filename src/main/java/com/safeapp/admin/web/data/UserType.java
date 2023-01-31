package com.safeapp.admin.web.data;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.Getter;

@Getter
public enum UserType implements GrantedAuthority {

	NONE("비회원", 1), 
    NORMAL("일반 회원", 101),
    PERSONAL("개인 회원 = 개인 결제자 or 유효한 팀에 속한 사람", 111),
    TEAM_USER("팀(그룹 구성원) 회원", 121), 
    TEAM_MANAGER("팀(그룹 관리자) 회원", 122),
    TEAM_MASTER("팀(그룹 마스터관리자 = 팀 결제자) 회원", 129);
	
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