package com.binoofactory.cornsqure.web.repos.jpa.custom;

import com.binoofactory.cornsqure.web.data.YN;
import com.binoofactory.cornsqure.web.dto.response.ResponseRiskcheckDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RIskCheckRepositoryCustom {
    List<ResponseRiskcheckDTO> findAllByCondition(
            Long userId,
            Long projectId,
            String name,
            String tag,
            YN visibled,
            String status,
            YN created_at_descended,
            YN views_descended,
            YN likes_descended,
            String detail_contents,
            Pageable page
    );
}
