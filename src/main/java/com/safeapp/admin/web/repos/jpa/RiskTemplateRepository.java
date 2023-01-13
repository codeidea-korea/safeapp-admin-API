package com.safeapp.admin.web.repos.jpa;

import com.safeapp.admin.web.repos.jpa.custom.RIskTemplateRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.safeapp.admin.web.model.entity.RiskTemplate;

@Repository
public interface RiskTemplateRepository extends JpaRepository<RiskTemplate, Long>, RIskTemplateRepositoryCustom {
}
