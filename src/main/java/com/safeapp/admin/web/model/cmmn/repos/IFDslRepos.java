package com.safeapp.admin.web.model.cmmn.repos;

import java.util.List;

import com.safeapp.admin.web.model.cmmn.Pages;

public interface IFDslRepos<T> {
    List<T> findAll(T obj, Pages bfPage);

    long countAll(T obj);
}
