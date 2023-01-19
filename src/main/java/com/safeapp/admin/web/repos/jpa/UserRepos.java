package com.safeapp.admin.web.repos.jpa;

import com.safeapp.admin.web.data.SNSType;
import com.safeapp.admin.web.dto.response.ResponseChecklistTemplateDTO;
import com.safeapp.admin.web.repos.jpa.custom.UserReposCustom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.entity.Users;

import java.util.List;

@Repository
public interface UserRepos extends JpaRepository<Users, Long> {

    Users findByUserID(String userID);

}