package com.safeapp.admin.web.repos.jpa.custom;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.response.ResponseAccidentCaseDTO;
import com.safeapp.admin.web.model.entity.AccidentExp;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface AccidentExpRepositoryCustom {

    Long countAllByCondition(String keyword, String adminName, String phoneNo,
        LocalDateTime createdAtStart, LocalDateTime createdAtEnd);

    List<AccidentExp> findAllByConditionAndOrderBy(String keyword, String adminName, String phoneNo,
        LocalDateTime createdAtStart, LocalDateTime createdAtEnd, YN createdAtDesc, YN viewsDesc,
        int pageNo, int pageSize);

}