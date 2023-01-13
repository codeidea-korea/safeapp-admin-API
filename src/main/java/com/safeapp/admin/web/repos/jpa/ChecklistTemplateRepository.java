package com.safeapp.admin.web.repos.jpa;

import com.safeapp.admin.web.repos.jpa.custom.ChecklistTemplateRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.safeapp.admin.web.model.entity.ChecklistTemplate;

@Repository
public interface ChecklistTemplateRepository extends JpaRepository<ChecklistTemplate, Long>, ChecklistTemplateRepositoryCustom {
}
