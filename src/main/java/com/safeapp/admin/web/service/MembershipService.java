package com.safeapp.admin.web.service;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.Auth;
import com.safeapp.admin.web.model.entity.UserAuth;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface MembershipService extends CRUDService<UserAuth> {

    Map<String, Object> findMembership(long id, HttpServletRequest request);

    Long countMembershipList(String userName, String orderType, String status,
        LocalDateTime createdAtStart, LocalDateTime createdAtEnd);

    List<Map<String, Object>> findMembershipList(String userName, String orderType, String status,
        LocalDateTime createdAtStart, LocalDateTime createdAtEnd, int pageNo, int pageSize, HttpServletRequest request);

    UserAuth generate(UserAuth oldUserAuth);

}