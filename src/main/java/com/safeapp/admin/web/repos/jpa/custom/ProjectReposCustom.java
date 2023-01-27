package com.safeapp.admin.web.repos.jpa.custom;

import com.safeapp.admin.web.dto.response.ResponseProjectGroupDTO;
import com.safeapp.admin.web.model.entity.Project;
import com.safeapp.admin.web.model.entity.ProjectGroup;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ProjectReposCustom {

    @Transactional(readOnly = true)
    List<ResponseProjectGroupDTO> findAllGroupByCondition(long id, Pageable pageable, HttpServletRequest request);

}