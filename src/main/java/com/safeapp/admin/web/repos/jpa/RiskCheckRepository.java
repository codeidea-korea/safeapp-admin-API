package com.safeapp.admin.web.repos.jpa;

import com.safeapp.admin.web.repos.jpa.custom.RiskCheckRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.safeapp.admin.web.model.entity.RiskCheck;

@Repository
public interface RiskCheckRepository extends JpaRepository<RiskCheck, Long>, RiskCheckRepositoryCustom {

}