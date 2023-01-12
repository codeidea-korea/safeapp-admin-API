package com.binoofactory.cornsqure.web.service;

import javax.servlet.http.HttpServletRequest;

import com.binoofactory.cornsqure.web.dto.request.RequestChecklistProjectDTO;
import com.binoofactory.cornsqure.web.model.cmmn.service.BfCRUDService;
import com.binoofactory.cornsqure.web.model.entity.ChecklistProject;
import com.binoofactory.cornsqure.web.model.entity.ProjectManager;
import org.apache.ibatis.javassist.NotFoundException;

public interface ProjectManagersService extends BfCRUDService<ProjectManager> {

    void removeByProjectId(long projectId, HttpServletRequest httpServletRequest);

}
