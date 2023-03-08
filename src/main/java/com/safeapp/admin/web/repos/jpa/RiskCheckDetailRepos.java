package com.safeapp.admin.web.repos.jpa;

import com.safeapp.admin.web.model.entity.RiskCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.safeapp.admin.web.model.entity.RiskCheckDetail;

import java.util.List;

@Repository
public interface RiskCheckDetailRepos extends JpaRepository<RiskCheckDetail, Long> {

    List<RiskCheckDetail> findAllByRiskCheck(RiskCheck riskChk);

}