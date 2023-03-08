package com.safeapp.admin.web.repos.jpa.custom;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface RiskCheckRepositoryCustom {

    @Transactional(readOnly = true)
    List<String> findContentsByRiskCheckId(long riskCheckId);

}