package com.binoofactory.cornsqure.web.repos.jpa;

import com.binoofactory.cornsqure.web.data.SNSType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.binoofactory.cornsqure.web.data.YN;
import com.binoofactory.cornsqure.web.model.entity.Users;

@Repository
public interface UserRepos extends JpaRepository<Users, Long> {
    Users findByUserId(String userId);

    Users findByUserNameAndPhoneNoAndDeleted(String userId, String phoneNo, YN deleted);

    Users findUsersBySnsValueAndSnsType(String snsValue, SNSType snsType);
}
