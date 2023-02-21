package com.safeapp.admin.web.repos.jpa;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.safeapp.admin.web.model.entity.SmsAuthHistory;

@Repository
public interface SmsAuthHistoryRepos extends JpaRepository<SmsAuthHistory, Long> {

    SmsAuthHistory findFirstByPhoneNoAndEfectedEndedAtAfterOrderByIdDesc(String phoneNo, LocalDateTime now);

}