package com.safeapp.admin.web.repos.jpa;

import com.safeapp.admin.web.model.entity.CheckListTemplateDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckListTemplateDetailRepos extends JpaRepository<CheckListTemplateDetail, Long> {

}