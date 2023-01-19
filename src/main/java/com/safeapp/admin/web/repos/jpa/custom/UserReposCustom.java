package com.safeapp.admin.web.repos.jpa.custom;

import com.safeapp.admin.web.dto.response.ResponseUsersDTO;
import com.safeapp.admin.web.model.entity.Users;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface UserReposCustom extends JpaRepository<ResponseUsersDTO, Long> {

    List<ResponseUsersDTO> findAllByCondition(String userID, String userName, String email, Pageable page);

}