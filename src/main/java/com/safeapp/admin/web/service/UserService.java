package com.safeapp.admin.web.service;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.data.SNSType;
import com.safeapp.admin.web.dto.request.RequestSNSUserDTO;
import com.safeapp.admin.web.dto.request.RequestUserDTO;
import com.safeapp.admin.web.model.cmmn.Token;
import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.Users;
import org.springframework.transaction.annotation.Transactional;

public interface UserService extends CRUDService<Users> {

    boolean chkUserID(String userID);

    boolean sendAuthSMSCode(String phoneNo) throws Exception;

    boolean isCorrectSMSCode(String phoneNo, String authNo) throws Exception;

    Users editPassword(String userID, String newPass1, String newPass2, HttpServletRequest httpServletRequest)
            throws Exception;

    Users toEntity (RequestUserDTO dto);

}