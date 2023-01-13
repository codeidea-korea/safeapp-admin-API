package com.safeapp.admin.web.service;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.docs.InviteHistory;

public interface InviteHistoryService extends CRUDService<InviteHistory> {

    void remove(String seq, HttpServletRequest httpServletRequest) throws Exception;

    InviteHistory find(String seq, HttpServletRequest httpServletRequest) throws Exception;
    
    InviteHistory generate(InviteHistory instance);
}
