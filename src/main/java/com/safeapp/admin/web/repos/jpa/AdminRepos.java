package com.safeapp.admin.web.repos.jpa;

import com.safeapp.admin.web.data.SNSType;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.entity.Admins;
import com.safeapp.admin.web.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepos extends JpaRepository<Admins, Long> {

    Admins findByEmail(String email);

    Admins findByUserNameAndPhoneNoAndDeleted(String adminID, String phoneNo, YN deleted);

    Admins findUsersBySnsValueAndSnsType(String snsValue, SNSType snsType);

}