package com.safeapp.admin.web.repos.jpa;

import com.safeapp.admin.web.model.entity.ProjectGroup;
import com.safeapp.admin.web.repos.jpa.custom.ProjectGroupReposCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectGroupRepos extends JpaRepository<ProjectGroup, Long>, ProjectGroupReposCustom {

}
