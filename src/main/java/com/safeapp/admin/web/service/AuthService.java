package com.safeapp.admin.web.service;

import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.Auth;

public interface AuthService extends CRUDService<Auth> {
    Auth generate(Auth userSeq);
}
