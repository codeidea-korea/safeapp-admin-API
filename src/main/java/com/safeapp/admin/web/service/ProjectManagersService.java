package com.safeapp.admin.web.service;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.ProjectManager;

public interface ProjectManagersService extends CRUDService<ProjectManager> {

    void removeByProjectId(long projectId, HttpServletRequest httpServletRequest);

}
