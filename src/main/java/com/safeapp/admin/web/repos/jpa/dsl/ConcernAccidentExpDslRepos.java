package com.safeapp.admin.web.repos.jpa.dsl;

import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.cmmn.repos.IFDslRepos;
import com.safeapp.admin.web.model.entity.ConcernAccidentExp;
import com.safeapp.admin.web.model.entity.Reports;

import java.util.List;

public interface ConcernAccidentExpDslRepos extends IFDslRepos<ConcernAccidentExp> {

    List<Reports> findReports(long id);

    long countAllReport(ConcernAccidentExp conExp);

    List<ConcernAccidentExp> findAllReport(ConcernAccidentExp conExp, Pages pages);

}