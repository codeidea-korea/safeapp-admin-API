package com.safeapp.admin.web.repos.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.safeapp.admin.web.model.entity.Inquiry;

@Repository
public interface InquiryRepos extends JpaRepository<Inquiry, Long> {
}
