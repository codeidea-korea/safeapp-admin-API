package com.safeapp.admin.web.service;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.dto.request.RequestUserDTO;
import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.Admins;
import com.safeapp.admin.web.model.entity.Users;

public interface AdminService extends CRUDService<Admins> {

    boolean chkAdminID(String adminID);

    Admins findMe(HttpServletRequest httpServletRequest);

    Users findByNameAndPhoneNo(String userName, String phoneNo) throws Exception;

    boolean changePasswordByNameAndPhoneNo(String userName, String phoneNo) throws Exception;

    Users modifyPassword(String userId, String password, String newpassword, HttpServletRequest httpServletRequest)
            throws Exception;

    Users toEntity (RequestUserDTO dto);

    boolean sendAuthSMSCode(String phoneNo) throws Exception;

    boolean isCurrectSMSCode(String phoneNo, String authNo) throws Exception;

}