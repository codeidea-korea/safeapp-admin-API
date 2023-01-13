package com.safeapp.admin.web.repos.mongo;

import com.safeapp.admin.web.model.docs.InviteHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InviteHistoryRepos extends MongoRepository<InviteHistory, String> {
}
