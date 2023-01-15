package com.safeapp.admin.web.repos.jpa;

import com.safeapp.admin.web.data.SNSType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.entity.Users;

@Repository
public interface UserRepos extends JpaRepository<Users, Long> {

    Users findByUserID(String userID);

}