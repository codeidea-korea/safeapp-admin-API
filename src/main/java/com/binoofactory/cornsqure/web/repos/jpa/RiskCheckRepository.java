package com.binoofactory.cornsqure.web.repos.jpa;

import com.binoofactory.cornsqure.web.repos.jpa.custom.RIskCheckRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.binoofactory.cornsqure.web.model.entity.RiskCheck;

@Repository
public interface RiskCheckRepository extends JpaRepository<RiskCheck, Long>, RIskCheckRepositoryCustom {
}
