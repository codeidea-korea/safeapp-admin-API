package com.safeapp.admin.web.service;

import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.Inquiry;

public interface InquiryService extends CRUDService<Inquiry> {
    Inquiry generate(Inquiry userSeq);
}
