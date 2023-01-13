package com.safeapp.admin.web.repos.jpa;

import com.safeapp.admin.web.repos.jpa.custom.ChecklistProjectRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.safeapp.admin.web.model.entity.ChecklistProject;

@Repository
public interface ChecklistProjectRepository extends JpaRepository<ChecklistProject, Long>, ChecklistProjectRepositoryCustom {

}
