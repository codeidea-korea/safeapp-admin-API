package com.binoofactory.cornsqure.web.model.cmmn.service;

import javax.servlet.http.HttpServletRequest;

import com.binoofactory.cornsqure.web.model.cmmn.BfListResponse;
import com.binoofactory.cornsqure.web.model.cmmn.BfPage;

public interface BfCRUDService<T> {
    T add(T obj, HttpServletRequest httpServletRequest) throws Exception;

    T edit(T obj, HttpServletRequest httpServletRequest) throws Exception;

    void remove(long seq, HttpServletRequest httpServletRequest) throws Exception;

    T find(long seq, HttpServletRequest httpServletRequest) throws Exception;

    BfListResponse<T> findAll(T obj, BfPage bfPage, HttpServletRequest httpServletRequest) throws Exception;

    T generate(T obj);
}
