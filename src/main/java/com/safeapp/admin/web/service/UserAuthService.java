package com.safeapp.admin.web.service;

import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.UserAuth;

public interface UserAuthService extends CRUDService<UserAuth> {
    UserAuth generate(UserAuth userSeq);

    UserAuth getEfectiveAuthByUserId(String userId);
}
