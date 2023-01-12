package com.binoofactory.cornsqure.web.repos.jpa;

import com.binoofactory.cornsqure.web.repos.jpa.custom.ChecklistTemplateRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.binoofactory.cornsqure.web.model.entity.ChecklistTemplate;

@Repository
public interface ChecklistTemplateRepository extends JpaRepository<ChecklistTemplate, Long>, ChecklistTemplateRepositoryCustom {
}
