package com.binoofactory.cornsqure.web.repos.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.binoofactory.cornsqure.web.model.docs.LoginHistory;

@Repository
public interface LoginHistoryRepos extends MongoRepository<LoginHistory, String> {
}
