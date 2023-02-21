package com.safeapp.admin.web.service.impl;

import java.util.Objects;

import com.safeapp.admin.web.data.AdminType;
import com.safeapp.admin.web.model.AdminDetails;
import com.safeapp.admin.web.model.entity.Admins;
import com.safeapp.admin.web.repos.jpa.AdminRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.safeapp.admin.web.service.OAuthService;
import com.safeapp.admin.web.service.RedisCacheService;
import com.querydsl.core.util.StringUtils;

import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

@Service("userDetailsService")
public class OAuthServiceImpl implements OAuthService {

    private final AdminRepos adminRepos;
    private final RedisCacheService redisCacheService;

    @Autowired
    public OAuthServiceImpl(AdminRepos adminRepos, RedisCacheService redisCacheService) {
        this.adminRepos = adminRepos;
        this.redisCacheService = redisCacheService;
    }

    private Admins convertString2User(String adminInfo) {
        JSONParser parser = new JSONParser(JSONParser.MODE_JSON_SIMPLE);

        try {
            return parser.parse(adminInfo, Admins.class);
        } catch (ParseException e) {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        AdminDetails details = new AdminDetails();

        details.setAdminType(AdminType.ADMIN);
        details.setEnabled(false);
        details.setExpired(true);

        Admins admin = adminRepos.findByAdminId(id);
        if(Objects.isNull(admin)) {
            return details;
        }

        details.setEnabled(true);

        String cacheUserInfo = redisCacheService.getValue("user-id" + id);
        if(StringUtils.isNullOrEmpty(cacheUserInfo)) {
            return details;
        }

        admin = convertString2User(cacheUserInfo);
        if(Objects.isNull(admin)) {
            return details;
        }

        details.setExpired(false);
        return details;
    }

}