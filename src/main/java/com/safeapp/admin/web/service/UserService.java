package com.safeapp.admin.web.service;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.dto.request.RequestUsersDTO;
import com.safeapp.admin.web.dto.request.RequestUsersModifyDTO;
import com.safeapp.admin.web.dto.response.ResponseRiskcheckDTO;
import com.safeapp.admin.web.dto.response.ResponseUsersDTO;
import com.safeapp.admin.web.model.cmmn.Token;
import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.Admins;
import com.safeapp.admin.web.model.entity.Users;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UserService extends CRUDService<Users> {

    boolean chkUserId(String userId);

    boolean sendAuthSMSCode(String phoneNo) throws Exception;

    boolean isCorrectSMSCode(String phoneNo, String authNo) throws Exception;

    Map<String, Object> findMyAuth(long id, HttpServletRequest request);

    List<Map<String, Object>> findMyProject(long id, HttpServletRequest request);

    Users toEntity(RequestUsersDTO addDto);

    Users editPassword(String userId, String newPass1, String newPass2, HttpServletRequest request) throws Exception;

    Users toEntityModify(RequestUsersModifyDTO modifyDto);

    Users generate(Users oldUser);

}