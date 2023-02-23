package com.safeapp.admin.web.service;

import com.safeapp.admin.web.dto.request.RequestMembershipEditDTO;
import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.UserAuth;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface MembershipService extends CRUDService<UserAuth> {

    Map<String, Object> findMembership(long id, HttpServletRequest request);

    UserAuth toEditEntity(RequestMembershipEditDTO editDto);

    void unsubscribe(long id, HttpServletRequest request);

    Long countMembershipList(String userName, String orderType, String status,
        String createdAtStart, String createdAtEnd);

    List<Map<String, Object>> findMembershipList(String userName, String orderType, String status,
        String createdAtStart, String createdAtEnd, int pageNo, int pageSize);

}