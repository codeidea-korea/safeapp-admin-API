package com.safeapp.admin.web.service;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.dto.request.RequestSNSUserDTO;
import com.safeapp.admin.web.dto.request.RequestUsersDTO;
import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.Admins;
import com.safeapp.admin.web.model.entity.Users;

public interface AdminService extends CRUDService<Admins> {

    boolean chkAdminID(String adminID);

    Admins editPassword(String adminID, String password, String newPassword, HttpServletRequest httpServletRequest)
            throws Exception;

    boolean sendAuthSMSCode(String phoneNo) throws Exception;

    boolean isCorrectSMSCode(String phoneNo, String authNo) throws Exception;

    //Admins toEntity (RequestUsersDTO dto);

}