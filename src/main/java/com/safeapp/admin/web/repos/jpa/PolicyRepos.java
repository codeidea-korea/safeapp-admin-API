package com.safeapp.admin.web.repos.jpa;

import com.safeapp.admin.web.model.entity.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PolicyRepos extends JpaRepository<Policy, Long> {

}