package com.binoofactory.cornsqure.web.service;

import com.binoofactory.cornsqure.web.model.cmmn.service.BfCRUDService;
import com.binoofactory.cornsqure.web.model.entity.Notice;

public interface NoticeService extends BfCRUDService<Notice> {
    Notice generate(Notice userSeq);
}
