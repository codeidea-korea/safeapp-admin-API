package com.safeapp.admin.web.repos.jpa;

import com.safeapp.admin.web.model.entity.CheckListTemplate;
import com.safeapp.admin.web.repos.jpa.custom.CheckListTemplateRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckListTemplateRepository extends JpaRepository<CheckListTemplate, Long>, CheckListTemplateRepositoryCustom {

}