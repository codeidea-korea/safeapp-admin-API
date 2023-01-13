package com.safeapp.admin.web.model.cmmn.repos;

import java.util.List;

import com.safeapp.admin.web.model.cmmn.BfPage;

public interface BfIFDslRepos<T> {
    List<T> findAll(T obj, BfPage bfPage);

    long countAll(T obj);
}
