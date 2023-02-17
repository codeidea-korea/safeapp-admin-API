package com.safeapp.admin.web.repos.jpa;

import com.safeapp.admin.web.model.entity.Reports;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepos extends JpaRepository<Reports, Long> {

}