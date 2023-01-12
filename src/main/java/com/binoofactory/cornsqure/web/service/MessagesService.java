package com.binoofactory.cornsqure.web.service;

import com.binoofactory.cornsqure.web.model.cmmn.service.BfCRUDService;
import com.binoofactory.cornsqure.web.model.entity.Messages;

public interface MessagesService extends BfCRUDService<Messages> {
    Messages generate(Messages userSeq);
}
