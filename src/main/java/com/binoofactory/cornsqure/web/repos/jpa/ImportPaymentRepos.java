package com.binoofactory.cornsqure.web.repos.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.binoofactory.cornsqure.web.model.entity.ImportPayment;

@Repository
public interface ImportPaymentRepos extends JpaRepository<ImportPayment, Long> {
    ImportPayment findByMerchantUid(String merchantUid);
}
