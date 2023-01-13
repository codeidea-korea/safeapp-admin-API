package com.safeapp.admin.web.repos.jpa.custom;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.response.ResponseRiskTemplateDTO;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface RIskTemplateRepositoryCustom {
    List<ResponseRiskTemplateDTO> findAllByCondition(
            Long checkerId,
            Long projectId,
            Long userId,
            String name,
            YN visibled,
            String tags,
            Pageable page
    );
}
