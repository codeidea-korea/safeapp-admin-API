package com.safeapp.admin.web.service;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.data.SNSType;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.request.RequestSNSUserDTO;
import com.safeapp.admin.web.dto.request.RequestUsersDTO;
import com.safeapp.admin.web.dto.request.RequestUsersModifyDTO;
import com.safeapp.admin.web.dto.response.ResponseRiskcheckDTO;
import com.safeapp.admin.web.dto.response.ResponseUsersDTO;
import com.safeapp.admin.web.model.cmmn.Token;
import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.Users;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService extends CRUDService<Users> {

    boolean chkUserId(String userId);

    boolean sendAuthSMSCode(String phoneNo) throws Exception;

    boolean isCorrectSMSCode(String phoneNo, String authNo) throws Exception;

    Users toEntity(RequestUsersDTO addDto);

    Users editPassword(String userId, String newPass1, String newPass2, HttpServletRequest httpServletRequest) throws Exception;

    Users toEntityModify(RequestUsersModifyDTO modifyDto);

}