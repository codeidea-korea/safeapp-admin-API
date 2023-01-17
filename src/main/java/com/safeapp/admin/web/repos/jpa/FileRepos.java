package com.safeapp.admin.web.repos.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.safeapp.admin.web.model.entity.cmmn.Files;

@Repository
public interface FileRepos extends JpaRepository<Files, Long> {
}
