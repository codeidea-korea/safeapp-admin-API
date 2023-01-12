package com.binoofactory.cornsqure.web.service;

import com.binoofactory.cornsqure.web.model.cmmn.service.BfCRUDService;
import com.binoofactory.cornsqure.web.model.entity.UserAuth;

public interface UserAuthService extends BfCRUDService<UserAuth> {
    UserAuth generate(UserAuth userSeq);

    UserAuth getEfectiveAuthByUserId(String userId);
}
