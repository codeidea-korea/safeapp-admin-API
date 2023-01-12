package com.binoofactory.cornsqure.web.service;

import javax.servlet.http.HttpServletRequest;

import com.binoofactory.cornsqure.web.data.SNSType;
import com.binoofactory.cornsqure.web.dto.request.RequestSNSUserDTO;
import com.binoofactory.cornsqure.web.dto.request.RequestUserDTO;
import com.binoofactory.cornsqure.web.model.cmmn.BfToken;
import com.binoofactory.cornsqure.web.model.cmmn.service.BfCRUDService;
import com.binoofactory.cornsqure.web.model.entity.Users;
import org.springframework.transaction.annotation.Transactional;

public interface UserService extends BfCRUDService<Users> {

    @Transactional
    Users snsAdd(RequestSNSUserDTO dto, HttpServletRequest httpServletRequest) throws Exception;

    boolean checkUserId(String userId);

    Users findMe(HttpServletRequest httpServletRequest);

    BfToken login(Users user, HttpServletRequest httpServletRequest) throws Exception;

    BfToken snsLogin(String snsValue, SNSType snsType, HttpServletRequest httpServletRequest) throws Exception;

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
