package com.safeapp.admin.web.service;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.dto.request.RequestUsersDTO;
import com.safeapp.admin.web.dto.request.RequestUsersEditDTO;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.Users;

import java.util.List;
import java.util.Map;

public interface UserService extends CRUDService<Users> {

    boolean chkUserId(String userId);

    boolean sendAuthSMSCode(String phoneNo) throws Exception;

    boolean isCorrectSMSCode(String phoneNo, String authNo) throws Exception;

    Users toAddEntity(RequestUsersDTO addDto);

    Map<String, Object> findMyAuth(long id, HttpServletRequest request);

    long countMyProjectList(long id, HttpServletRequest request);

    List<Map<String, Object>> findMyProjectList(long id, int pageNo, int pageSize, HttpServletRequest request);

    Users editPassword(String userId, String newPass1, String newPass2, HttpServletRequest request) throws Exception;

    Users toEditEntity(RequestUsersEditDTO editDto);

}