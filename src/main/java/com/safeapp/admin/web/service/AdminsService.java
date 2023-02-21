package com.safeapp.admin.web.service;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.dto.request.*;
import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.Admins;
import com.safeapp.admin.web.model.entity.Users;

public interface AdminsService extends CRUDService<Admins> {

    boolean chkAdminId(String adminId);

    boolean chkEmail(String email);

    boolean sendAuthSMSCode(String phoneNo) throws Exception;

    boolean isCorrectSMSCode(String phoneNo, String authNo) throws Exception;

    Admins toAddEntity(RequestAdminsDTO addDto);

    Admins editPassword(String adminId, String newPass1, String newPass2, HttpServletRequest request) throws Exception;

    Admins toEditEntity(RequestAdminsEditDTO editDTO);

    Admins generate(Admins newAdmin);

}