package com.binoofactory.cornsqure.web.service;

import com.binoofactory.cornsqure.web.model.cmmn.service.BfCRUDService;
import com.binoofactory.cornsqure.web.model.entity.Inquiry;

public interface InquiryService extends BfCRUDService<Inquiry> {
    Inquiry generate(Inquiry userSeq);
}
