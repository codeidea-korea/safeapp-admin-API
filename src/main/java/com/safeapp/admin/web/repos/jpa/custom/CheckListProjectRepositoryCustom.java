package com.safeapp.admin.web.repos.jpa.custom;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.response.ResponseCheckListProjectDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CheckListProjectRepositoryCustom {

    List<ResponseCheckListProjectDTO> findAllByConditionAndOrderBy(String tag, YN visibled, YN descendedCreatedDate,
            YN descendedLike, YN descendedView, Pageable pageable);

}