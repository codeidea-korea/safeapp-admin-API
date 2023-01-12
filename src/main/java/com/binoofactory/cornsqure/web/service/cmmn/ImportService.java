package com.binoofactory.cornsqure.web.service.cmmn;

import javax.servlet.http.HttpServletRequest;

import com.binoofactory.cornsqure.web.model.entity.ImportPayment;

public interface ImportService {
    String generateOrderNumber(ImportPayment payment, HttpServletRequest httpServletRequest) throws Exception;

    String callBackImportPayment(String imp_uid, String merchant_uid) throws Exception;
}
