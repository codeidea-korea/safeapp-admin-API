package com.binoofactory.cornsqure.web.repos.jpa.custom;

import com.binoofactory.cornsqure.web.data.YN;
import com.binoofactory.cornsqure.web.dto.response.ResponseRiskTemplateDTO;
import com.binoofactory.cornsqure.web.dto.response.ResponseRiskcheckDTO;

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
