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

    Users editPassword(String userID, String password, String newPassword, HttpServletRequest httpServletRequest)
            throws Exception;
    
    boolean sendAuthSMSCode(String phoneNo) throws Exception;
    
    boolean isCorrectSMSCode(String phoneNo, String authNo) throws Exception;

    Users toEntity (RequestUserDTO dto);

    Users toEntitySNS (RequestSNSUserDTO dto);

}