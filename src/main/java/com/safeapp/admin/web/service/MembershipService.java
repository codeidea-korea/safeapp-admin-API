package com.safeapp.admin.web.service;

import com.safeapp.admin.web.dto.request.RequestMembershipModifyDTO;
import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.Auth;
import com.safeapp.admin.web.model.entity.UserAuth;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface MembershipService extends CRUDService<UserAuth> {

    Map<String, Object> findMembership(long id, HttpServletRequest request);

    UserAuth toEntityModify(RequestMembershipModifyDTO modifyDto);

    void unsubscribe(long id, HttpServletRequest request);

    Long countMembershipList(String userName, String orderType, String status,
        String createdAtStart, String createdAtEnd);

    List<Map<String, Object>> findMembershipList(String userName, String orderType, String status,
        String createdAtStart, String createdAtEnd, int pageNo, int pageSize, HttpServletRequest request);

}