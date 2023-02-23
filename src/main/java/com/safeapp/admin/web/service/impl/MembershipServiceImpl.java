package com.safeapp.admin.web.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.dto.request.RequestMembershipEditDTO;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.UserAuth;
import com.safeapp.admin.web.repos.direct.DirectQuery;
import com.safeapp.admin.web.repos.jpa.UserAuthRepos;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public UserAuth add(UserAuth newUserAuth, HttpServletRequest request) throws Exception {
        if(Objects.isNull(newUserAuth)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 멤버쉽 결제입니다.");
        }

        UserAuth addedMembership = userAuthRepos.save(generate(newUserAuth));
        if(Objects.isNull(addedMembership)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "DB 저장 중 오류가 발생하였습니다.");
        }

        return addedMembership;
    }

    @Override
    public UserAuth find(long id, HttpServletRequest request) throws Exception {
        UserAuth userAuth =
            userAuthRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 멤버쉽 결제입니다."));

        return userAuth;
    }

    @Override
    public Map<String, Object> findMembership(long id, HttpServletRequest request) {
        Map<String, Object> membership = dirRepos.findMembership(id);

        return membership;
    }

    @Override
    public UserAuth toEditEntity(RequestMembershipEditDTO editDto) {
        UserAuth newUserAuth = new UserAuth();

        newUserAuth.setEfectiveStartAt(LocalDateTime.parse(editDto.getEfectiveStartAt()));
        newUserAuth.setEfectiveEndAt(LocalDateTime.parse(editDto.getEfectiveEndAt()));
        newUserAuth.setMemo(editDto.getMemo());

        return newUserAuth;
    }

    @Override
    public UserAuth edit(UserAuth newUserAuth, HttpServletRequest request) throws Exception {
        UserAuth userAuth =
            userAuthRepos.findById(newUserAuth.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 멤버쉽 결제입니다."));

        userAuth.edit(newUserAuth);

        UserAuth editedUserAuth = userAuthRepos.save(userAuth);
        return editedUserAuth;
    }

    @Override
    public void unsubscribe(long id, HttpServletRequest request) {

        dirRepos.unsubscribe(id);
    }

    @Override
    public void remove(long id, HttpServletRequest request) {
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
            String createdAtStart, String createdAtEnd, int pageNo, int pageSize) {

        List<Map<String, Object>> membershipList =
            dirRepos.findMembershipList(userName, orderType, status, createdAtStart, createdAtEnd, pageNo, pageSize);

        Long sumMembershipList = dirRepos.sumMembershipList(userName, orderType, status, createdAtStart, createdAtEnd);
        membershipList.get(0).put("totalAmount", sumMembershipList);

        return membershipList;
    }

    @Override
    public UserAuth generate(UserAuth newUserAuth) {
        return
            UserAuth.builder()
            .id(newUserAuth.getId())
            .efectiveStartAt(newUserAuth.getEfectiveStartAt())
            .efectiveEndAt(newUserAuth.getEfectiveEndAt())
            .memo(newUserAuth.getMemo())
            .build();
    }

    @Override
    public ListResponse<UserAuth> findAll(UserAuth userAuth, Pages pages, HttpServletRequest request) throws Exception {

        return null;
    }

}