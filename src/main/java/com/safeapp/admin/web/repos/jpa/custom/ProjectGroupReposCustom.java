package com.safeapp.admin.web.repos.jpa.custom;

import com.safeapp.admin.web.dto.response.ResponseProjectGroupDTO;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ProjectGroupReposCustom {

    @Transactional(readOnly = true)
    List<ResponseProjectGroupDTO> findAllById(long id, int pageNo, int pageSize, HttpServletRequest request);

}