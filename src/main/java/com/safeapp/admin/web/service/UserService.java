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

    @Transactional
    Users snsAdd(RequestSNSUserDTO dto, HttpServletRequest httpServletRequest) throws Exception;

    boolean checkUserId(String userId);

    Users findMe(HttpServletRequest httpServletRequest);

    Token login(Users user, HttpServletRequest httpServletRequest) throws Exception;

    Token snsLogin(String snsValue, SNSType snsType, HttpServletRequest httpServletRequest) throws Exception;

    Users generateUserinfo(long userSeq);
    
    boolean sendAuthSMSCode(String phoneNo) throws Exception;
    
    boolean isCurrectSMSCode(String phoneNo, String authNo) throws Exception;

    Users findByUserId(String userId) throws Exception;

    Users findByNameAndPhoneNo(String userName, String phoneNo) throws Exception;

    boolean changePasswordByNameAndPhoneNo(String userName, String phoneNo) throws Exception;

    Users modifyPassword(String userId, String password, String newpassword, HttpServletRequest httpServletRequest)
        throws Exception;

    Users toEntity (RequestUserDTO dto);

    Users toEntitySNS (RequestSNSUserDTO dto);
}
