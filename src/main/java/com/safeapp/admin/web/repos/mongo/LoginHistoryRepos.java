package com.safeapp.admin.web.repos.mongo;

import com.safeapp.admin.web.model.docs.LoginHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginHistoryRepos extends MongoRepository<LoginHistory, String> {
}
