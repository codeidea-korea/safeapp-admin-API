package com.binoofactory.cornsqure.web.repos.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.binoofactory.cornsqure.web.model.docs.LikeHistory;

@Repository
public interface LikeHistoryRepos extends MongoRepository<LikeHistory, String> {
    LikeHistory findByUserIdAndTypeAndBoardId(long userId, String type, long boardId);
}
