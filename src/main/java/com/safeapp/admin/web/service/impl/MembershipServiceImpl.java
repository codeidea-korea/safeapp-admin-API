package com.safeapp.admin.web.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.utils.DateUtil;
import com.safeapp.admin.utils.PasswordUtil;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.request.RequestMembershipModifyDTO;
import com.safeapp.admin.web.dto.request.RequestUsersModifyDTO;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.Auth;
import com.safeapp.admin.web.model.entity.UserAuth;
import com.safeapp.admin.web.model.entity.Users;
import com.safeapp.admin.web.repos.direct.DirectQuery;
import com.safeapp.admin.web.repos.jpa.AuthRepos;
import com.safeapp.admin.web.repos.jpa.UserAuthRepos;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.safeapp.admin.web.service.MembershipService;

@Service
@AllArgsConstructor
@Slf4j
public class MembershipServiceImpl implements MembershipService {

    private final UserAuthRepos userAuthRepos;
    private final DirectQuery dirRepos;

    @Transactional
    @Override
    public UserAuth add(UserAuth userAuth, HttpServletRequest request) throws Exception {
        if(Objects.isNull(userAuth)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 멤버쉽 결제입니다.");
        }

        UserAuth addedMembership = userAuthRepos.save(generate(userAuth));
        if(Objects.isNull(addedMembership)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "DB 저장 중 오류가 발생하였습니다.");
        }

        return addedMembership;
    }

    @Override
    public UserAuth find(long id, HttpServletRequest request) throws Exception {
        UserAuth oldUserAuth =
                userAuthRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 멤버쉽 결제입니다."));

        return oldUserAuth;
    }

    @Override
    public Map<String, Object> findMembership(long id, HttpServletRequest request) {
        Map<String, Object> oldMemberShip = dirRepos.findMembership(id);

        return oldMemberShip;
    }

    @Override
    public UserAuth toEntityModify(RequestMembershipModifyDTO modifyDto) {
        UserAuth userAuth = new UserAuth();

        userAuth.setEfectiveStartAt(LocalDateTime.parse(modifyDto.getEfectiveStartAt()));
        userAuth.setEfectiveEndAt(LocalDateTime.parse(modifyDto.getEfectiveEndAt()));
        userAuth.setMemo(modifyDto.getMemo());

        return userAuth;
    }

    @Override
    public UserAuth edit(UserAuth userAuth, HttpServletRequest request) throws Exception {
        UserAuth oldUserAuth =
            userAuthRepos.findById(userAuth.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 멤버쉽 결제입니다."));

        oldUserAuth.edit(userAuth);

        UserAuth editedUserAuth = userAuthRepos.save(oldUserAuth);
        return editedUserAuth;
    }

    @Override
    public void unsubscribe(long id, HttpServletRequest request) {

        dirRepos.unsubscribe(id);
    }

    @Override
    public void remove(long id, HttpServletRequest httpServletRequest) {
        UserAuth userAuth =
            userAuthRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 멤버쉽 결제입니다."));

        userAuth.setDeleteYn(true);
        userAuthRepos.save(userAuth);
    }

    @Override
    public Long countMembershipList(String userName, String orderType, String status,
            String createdAtStart, String createdAtEnd) {

        return dirRepos.countMembershipList(userName, orderType, status, createdAtStart, createdAtEnd);
    }

    @Override
    public List<Map<String, Object>> findMembershipList(String userName, String orderType, String status,
            String createdAtStart, String createdAtEnd, int pageNo, int pageSize, HttpServletRequest request) {

        return dirRepos.findMembershipList(userName, orderType, status, createdAtStart, createdAtEnd, pageNo, pageSize, request);
    }

    @Override
    public UserAuth generate(UserAuth oldUserAuth) {
        return
            UserAuth.builder()
            .id(oldUserAuth.getId())
            .efectiveStartAt(oldUserAuth.getEfectiveStartAt())
            .efectiveEndAt(oldUserAuth.getEfectiveEndAt())
            .memo(oldUserAuth.getMemo())
            .build();
    }

    @Override
    public ListResponse<UserAuth> findAll(UserAuth userAuth, Pages pages, HttpServletRequest request) throws Exception {

        return null;
    }

}