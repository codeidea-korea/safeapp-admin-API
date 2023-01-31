package com.safeapp.admin.web.service;

import com.safeapp.admin.web.dto.response.ResponseProjectGroupDTO;
import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.*;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ProjectService extends CRUDService<Project> {

    List<ResponseProjectGroupDTO> findAllGroupByCondition(long id, int pageNo, int pageSize, HttpServletRequest request) throws Exception;

    void removeGroup(long id, HttpServletRequest request) throws Exception;

}