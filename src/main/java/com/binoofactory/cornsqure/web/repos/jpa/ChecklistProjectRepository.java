package com.binoofactory.cornsqure.web.repos.jpa;

import com.binoofactory.cornsqure.web.repos.jpa.custom.ChecklistProjectRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.binoofactory.cornsqure.web.model.entity.ChecklistProject;

@Repository
public interface ChecklistProjectRepository extends JpaRepository<ChecklistProject, Long>, ChecklistProjectRepositoryCustom {

}
