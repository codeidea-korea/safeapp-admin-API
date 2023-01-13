package com.safeapp.admin.web.service;

import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.Messages;

public interface MessagesService extends CRUDService<Messages> {
    Messages generate(Messages userSeq);
}
