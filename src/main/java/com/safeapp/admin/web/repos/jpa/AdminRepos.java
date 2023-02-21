package com.safeapp.admin.web.repos.jpa;

import com.safeapp.admin.web.model.entity.Admins;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepos extends JpaRepository<Admins, Long> {

    Admins findByEmail(String email);

    Admins findByAdminId(String adminId);

    Admins findByAdminNameAndPhoneNo(String adminName, String phoneNo);

    Admins findByEmailAndAdminNameAndPhoneNo(String email, String adminName, String phoneNo);

}