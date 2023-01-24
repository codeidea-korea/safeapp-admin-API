package com.safeapp.admin.web.repos.jpa;

import com.safeapp.admin.web.model.entity.CheckListProject;
import com.safeapp.admin.web.repos.jpa.custom.CheckListProjectRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckListProjectRepository extends JpaRepository<CheckListProject, Long>, CheckListProjectRepositoryCustom {

}