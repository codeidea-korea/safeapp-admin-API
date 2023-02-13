package com.safeapp.admin.web.repos.jpa.custom;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.response.ResponseRiskCheckDTO;
import com.safeapp.admin.web.model.entity.RiskCheck;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface RIskCheckRepositoryCustom {

    @Transactional(readOnly = true)
    List<String> findContentsByRiskChecktId(Long riskCheckId);

    Long countAllByCondition(String keyword, String userName, String phoneNo, YN visibled,
        LocalDateTime createdAtStart, LocalDateTime createdAtEnd);

    List<RiskCheck> findAllByConditionAndOrderBy(String keyword, String userName, String phoneNo, YN visibled,
        LocalDateTime createdAtStart, LocalDateTime createdAtEnd, YN createdAtDesc, YN likesDesc, YN viewsDesc,
        int pageNo, int pageSize);

}