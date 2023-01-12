package com.binoofactory.cornsqure.web.service.impl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.binoofactory.cornsqure.web.data.UserType;
import com.binoofactory.cornsqure.web.model.BfUserDetails;
import com.binoofactory.cornsqure.web.model.entity.Users;
import com.binoofactory.cornsqure.web.repos.jpa.UserRepos;
import com.binoofactory.cornsqure.web.service.OAuthService;
import com.binoofactory.cornsqure.web.service.RedisCacheService;
import com.querydsl.core.util.StringUtils;

import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

@Service("userDetailsService")
public class OAuthServiceImpl implements OAuthService {

    private final UserRepos userRepos;

    private final RedisCacheService redisCacheService;

    @Autowired
    public OAuthServiceImpl(UserRepos userRepos, RedisCacheService redisCacheService) {
        this.userRepos = userRepos;
        this.redisCacheService = redisCacheService;
    }

    private Users convertString2User(String userInfo) {
        JSONParser parser = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
        try {
            return parser.parse(userInfo, Users.class);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

        BfUserDetails details = new BfUserDetails();
        details.setType(UserType.NONE);
        details.setExpired(true);
        details.setEnabled(false);

        Users users = userRepos.findByUserId(id);
        if (Objects.isNull(users)) {
            return details;
        }
        details.setEnabled(true);

        String cacheUserInfo = redisCacheService.getValue("user-id" + id);
        if (StringUtils.isNullOrEmpty(cacheUserInfo)) {
            return details;
        }
        Users user = convertString2User(cacheUserInfo);
        if (Objects.isNull(user)) {
            return details;
        }
        details.setExpired(false);

        return details;
    }
}
