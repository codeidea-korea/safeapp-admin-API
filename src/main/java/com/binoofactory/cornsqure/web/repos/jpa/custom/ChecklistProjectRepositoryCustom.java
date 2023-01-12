package com.binoofactory.cornsqure.web.repos.jpa.custom;

import com.binoofactory.cornsqure.web.data.YN;
import com.binoofactory.cornsqure.web.dto.response.ResponseChecklistProjectDTO;
import com.binoofactory.cornsqure.web.model.entity.ChecklistProject;
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
