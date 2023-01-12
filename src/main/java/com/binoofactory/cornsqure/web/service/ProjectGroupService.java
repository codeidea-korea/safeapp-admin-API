package com.binoofactory.cornsqure.web.service;

import com.binoofactory.cornsqure.web.model.cmmn.service.BfCRUDService;
import com.binoofactory.cornsqure.web.model.entity.ProjectGroup;

public interface ProjectGroupService extends BfCRUDService<ProjectGroup> {
    ProjectGroup generate(ProjectGroup userSeq);
}
