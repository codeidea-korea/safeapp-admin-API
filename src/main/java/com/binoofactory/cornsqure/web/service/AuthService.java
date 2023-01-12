package com.binoofactory.cornsqure.web.service;

import com.binoofactory.cornsqure.web.model.cmmn.service.BfCRUDService;
import com.binoofactory.cornsqure.web.model.entity.Auth;

public interface AuthService extends BfCRUDService<Auth> {
    Auth generate(Auth userSeq);
}
