package com.safeapp.admin.web.service.cmmn;

import java.util.Map;

public interface DirectSendAPIService {

    boolean sendSMS(String phoneNo, Map<String, String> body) throws Exception;
    
    boolean sendMail(String mailAddress, Map<String, String> body) throws Exception;

}