package com.safeapp.admin.web.repos.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.safeapp.admin.web.model.entity.ProjectManager;

@Repository
public interface ProjectManagersRepos extends JpaRepository<ProjectManager, Long> {
    List<ProjectManager> findAllByProjectId(long projectId);
}
