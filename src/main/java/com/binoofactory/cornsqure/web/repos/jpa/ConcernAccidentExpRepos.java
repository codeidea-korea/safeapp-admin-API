package com.binoofactory.cornsqure.web.repos.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.binoofactory.cornsqure.web.model.entity.ConcernAccidentExp;

@Repository
public interface ConcernAccidentExpRepos extends JpaRepository<ConcernAccidentExp, Long> {
}
