package com.binoofactory.cornsqure.web.repos.jpa;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.binoofactory.cornsqure.web.model.entity.UserAuth;

@Repository
public interface UserAuthRepos extends JpaRepository<UserAuth, Long> {
    List<UserAuth> findAllByUserIdAndEfectiveEndAtAfter(long userId, LocalDateTime targetTime);
}
