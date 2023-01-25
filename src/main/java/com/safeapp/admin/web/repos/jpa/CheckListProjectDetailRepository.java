package com.safeapp.admin.web.repos.jpa;

import com.safeapp.admin.web.model.entity.CheckListProjectDetail;
import com.safeapp.admin.web.repos.jpa.custom.CheckListProjectDetailRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckListProjectDetailRepository extends JpaRepository<CheckListProjectDetail, Long>, CheckListProjectDetailRepositoryCustom {

}