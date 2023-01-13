package com.safeapp.admin.web.repos.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.safeapp.admin.web.model.entity.ChecklistProjectResultImg;

@Repository
public interface ChecklistProjectResultImgRepos extends JpaRepository<ChecklistProjectResultImg, Long> {
    List<ChecklistProjectResultImg> findAllByResultId(long resultId);
}
