package com.safeapp.admin.web.repos.jpa;

import com.safeapp.admin.web.repos.jpa.custom.ChecklistProjectDetailRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.safeapp.admin.web.model.entity.ChecklistProjectDetail;

@Repository
public interface ChecklistProjectDetailRepository extends JpaRepository<ChecklistProjectDetail, Long>, ChecklistProjectDetailRepositoryCustom {
}
