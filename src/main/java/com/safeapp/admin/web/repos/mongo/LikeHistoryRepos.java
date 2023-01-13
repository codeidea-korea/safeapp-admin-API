package com.safeapp.admin.web.repos.mongo;

import com.safeapp.admin.web.model.docs.LikeHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeHistoryRepos extends MongoRepository<LikeHistory, String> {
    LikeHistory findByUserIdAndTypeAndBoardId(long userId, String type, long boardId);
}
