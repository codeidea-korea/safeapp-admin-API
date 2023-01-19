package com.safeapp.admin.web.model.cmmn.service;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;

public interface CRUDService<T> {

    T add(T obj, HttpServletRequest httpServletRequest) throws Exception;

    T find(long seq, HttpServletRequest httpServletRequest) throws Exception;

    T edit(T obj, HttpServletRequest httpServletRequest) throws Exception;

    void remove(long seq, HttpServletRequest httpServletRequest) throws Exception;

    ListResponse<T> findAll(T obj, Pages pages, HttpServletRequest httpServletRequest) throws Exception;

    T generate(T obj);

}