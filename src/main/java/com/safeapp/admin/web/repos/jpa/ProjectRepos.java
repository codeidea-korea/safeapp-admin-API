package com.safeapp.admin.web.repos.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.safeapp.admin.web.repos.jpa.custom.ProjectReposCustom;

import com.safeapp.admin.web.model.entity.Project;

@Repository
//public interface ProjectRepos extends JpaRepository<Project, Long>, ProjectReposCustom {
public interface ProjectRepos extends JpaRepository<Project, Long> {

}