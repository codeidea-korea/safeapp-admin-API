package com.binoofactory.cornsqure.web.repos.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.binoofactory.cornsqure.web.model.entity.ChecklistProjectResult;

@Repository
public interface ChecklistProjectResultRepos extends JpaRepository<ChecklistProjectResult, Long> {
}
