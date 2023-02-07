package com.safeapp.admin.web.repos.jpa.custom;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.response.ResponseCheckListProjectDTO;
import com.safeapp.admin.web.model.entity.CheckListProject;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public interface CheckListProjectRepositoryCustom {

    @Transactional(readOnly = true)
    List<String> findContentsByCheckListId(Long checkListId);

    Long countAllByCondition(String keyword, String userName, String phoneNo, YN visibled,
        LocalDateTime createdAtStart, LocalDateTime createdAtEnd);

    List<CheckListProject> findAllByConditionAndOrderBy(String keyword, String userName, String phoneNo, YN visibled,
        LocalDateTime createdAtStart, LocalDateTime createdAtEnd, YN createdAtDesc, YN likesDesc, YN viewsDesc,
        int pageNo, int pageSize);

}