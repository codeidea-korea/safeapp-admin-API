package com.safeapp.admin.web.model.cmmn.repos;

import java.util.List;

import com.safeapp.admin.web.model.cmmn.Pages;

public interface IFDslRepos<T> {

    long countAll(T obj);

    List<T> findAll(T obj, Pages pages);

}