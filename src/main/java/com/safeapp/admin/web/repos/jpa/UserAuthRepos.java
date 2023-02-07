package com.safeapp.admin.web.repos.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.safeapp.admin.web.model.entity.UserAuth;

@Repository
public interface UserAuthRepos extends JpaRepository<UserAuth, Long> {

    //List<UserAuth> findAllByUserIdAndEfectiveEndAtAfter(long userId, LocalDateTime targetTime);

    UserAuth findTopByUserAndStatusOrderByIdDesc(long user, String status);

}