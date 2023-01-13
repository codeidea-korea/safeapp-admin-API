package com.safeapp.admin.web.service;

import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.ProjectGroup;

public interface ProjectGroupService extends CRUDService<ProjectGroup> {
    ProjectGroup generate(ProjectGroup userSeq);
}
