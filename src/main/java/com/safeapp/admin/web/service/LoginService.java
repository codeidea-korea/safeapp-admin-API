package com.safeapp.admin.web.service;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.cmmn.Token;
import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.docs.LoginHistory;
import com.safeapp.admin.web.model.entity.Admins;
import com.safeapp.admin.web.model.entity.Users;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;

import java.time.LocalDateTime;
import java.util.Objects;

public interface LoginService {

    Token login(String email, String password, HttpServletRequest httpServletRequest) throws Exception;

}