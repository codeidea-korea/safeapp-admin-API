package com.safeapp.admin.web.repos.jpa.custom;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.response.ResponseCheckListTemplateDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChecklistTemplateRepositoryCustom {
    List<ResponseCheckListTemplateDTO> findAllByCondition(
            Long userId,
            Long projectId,
            String name,
            String tag,
            YN created_at_descended,
            YN views_descended,
            YN likes_descended,
            String detail_contents,
            Pageable page
    );
}
