package com.safeapp.admin.web.service;

import com.safeapp.admin.web.dto.request.RequestInquiryAnswerDTO;
import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.Inquiry;
import com.safeapp.admin.web.model.entity.Notice;

import javax.servlet.http.HttpServletRequest;

public interface InquiryService extends CRUDService<Inquiry> {

    Inquiry toAnswerEntity(RequestInquiryAnswerDTO answerDto);

    Inquiry answer(Inquiry newInquiry, HttpServletRequest request) throws Exception;

}