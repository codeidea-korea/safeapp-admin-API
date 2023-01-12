package com.binoofactory.cornsqure.web.repos.jpa;

import com.binoofactory.cornsqure.web.repos.jpa.custom.ChecklistProjectDetailRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.binoofactory.cornsqure.web.model.entity.ChecklistProjectDetail;

@Repository
public interface ChecklistProjectDetailRepository extends JpaRepository<ChecklistProjectDetail, Long>, ChecklistProjectDetailRepositoryCustom {
}
