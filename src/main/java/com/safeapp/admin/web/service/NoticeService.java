package com.safeapp.admin.web.service;

import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.Notice;

public interface NoticeService extends CRUDService<Notice> {
    Notice generate(Notice userSeq);
}
