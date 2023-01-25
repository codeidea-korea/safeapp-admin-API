package com.safeapp.admin.web.model.cmmn.service;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;

public interface CRUDService<T> {

    T add(T obj, HttpServletRequest request) throws Exception;

    T find(long id, HttpServletRequest request) throws Exception;

    T generate(T obj);

    T edit(T obj, HttpServletRequest request) throws Exception;

    void remove(long id, HttpServletRequest request) throws Exception;

    ListResponse<T> findAll(T obj, Pages pages, HttpServletRequest request) throws Exception;

}