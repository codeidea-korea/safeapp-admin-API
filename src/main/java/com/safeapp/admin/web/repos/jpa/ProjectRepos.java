package com.safeapp.admin.web.repos.jpa;

import com.safeapp.admin.web.repos.jpa.custom.ProjectReposCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.safeapp.admin.web.model.entity.Project;

@Repository
//public interface ProjectRepos extends JpaRepository<Project, Long>, ProjectReposCustom {
public interface ProjectRepos extends JpaRepository<Project, Long> {

}