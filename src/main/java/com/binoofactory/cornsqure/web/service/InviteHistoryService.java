package com.binoofactory.cornsqure.web.service;

import javax.servlet.http.HttpServletRequest;

import com.binoofactory.cornsqure.web.model.cmmn.service.BfCRUDService;
import com.binoofactory.cornsqure.web.model.docs.InviteHistory;

public interface InviteHistoryService extends BfCRUDService<InviteHistory> {

    void remove(String seq, HttpServletRequest httpServletRequest) throws Exception;

    InviteHistory find(String seq, HttpServletRequest httpServletRequest) throws Exception;
    
    InviteHistory generate(InviteHistory instance);
}
