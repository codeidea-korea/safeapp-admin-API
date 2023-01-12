package com.binoofactory.cornsqure.web.repos.jpa;

import com.binoofactory.cornsqure.web.repos.jpa.custom.RIskTemplateRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.binoofactory.cornsqure.web.model.entity.RiskTemplate;

@Repository
public interface RiskTemplateRepository extends JpaRepository<RiskTemplate, Long>, RIskTemplateRepositoryCustom {
}
