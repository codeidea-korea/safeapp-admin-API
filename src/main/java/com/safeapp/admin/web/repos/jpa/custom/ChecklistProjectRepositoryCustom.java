package com.safeapp.admin.web.repos.jpa.custom;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.response.ResponseChecklistProjectDTO;
import com.safeapp.admin.web.model.entity.ChecklistProject;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChecklistProjectRepositoryCustom {
    List<ResponseChecklistProjectDTO> findAllByConditionAndOrderBy(
            Long userId,
            Long projectId,
            String name,
            String tag,
            YN visibled,
            YN descendedCreatedDate,
            YN descendedLike,
            YN descendedView,
            String contents,
            Pageable page);

    List<String> findContentsByProjectId(Long projectId);

    List<ChecklistProject> findAllByEntity();
}
