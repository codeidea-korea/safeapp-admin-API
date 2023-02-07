package com.safeapp.admin.web.service;

import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.Auth;
import com.safeapp.admin.web.model.entity.UserAuth;

public interface MembershipService extends CRUDService<UserAuth> {

    UserAuth generate(UserAuth oldUserAuth);

}