package com.safeapp.admin.web.repos.jpa.dsl.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.Admins;
import com.safeapp.admin.web.model.entity.QAdmins;
import com.safeapp.admin.web.repos.jpa.dsl.AdminsDslRepos;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import io.netty.util.internal.StringUtil;

@Repository
@Slf4j
public class AdminsDslReposImpl extends QuerydslRepositorySupport implements AdminsDslRepos {

    @PersistenceContext(name = "entityManager")
    private EntityManager entityManager;

    @Autowired
    public AdminsDslReposImpl() {
        super(Admins.class);
    }

    private JPAQuery selectFromWhere(Admins admins, QAdmins qAdmins) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery query = jpaQueryFactory.selectFrom(qAdmins);

        if(!StringUtil.isNullOrEmpty(admins.getAdminId())) {
            query.where(qAdmins.adminId.like("%" + admins.getAdminId() + "%"));
        } else if(!StringUtil.isNullOrEmpty(admins.getAdminName())) {
            query.where(qAdmins.adminName.like("%" + admins.getAdminName()  + "%"));
        } else if(!StringUtil.isNullOrEmpty(admins.getEmail())) {
            query.where(qAdmins.email.like("%" + admins.getEmail()  + "%"));
        } else if(!StringUtil.isNullOrEmpty(admins.getPhoneNo())) {
            query.where(qAdmins.phoneNo.like("%" + admins.getPhoneNo()  + "%"));
        }
        query.where(qAdmins.deleteYn.eq(false));

        return query;
    }

    @Override
    public long countAll(Admins admins) {
        QAdmins qAdmins = QAdmins.admins;
        JPAQuery query = selectFromWhere(admins, qAdmins);

        return query.fetchCount();
    }

    @Override
    public List<Admins> findAll(Admins admins, Pages pages) {
        QAdmins qAdmins = QAdmins.admins;
        JPAQuery query = selectFromWhere(admins, qAdmins);

        query
            .offset(pages.getOffset())
            .limit(pages.getPageSize())
            .orderBy(
                new OrderSpecifier(com.querydsl.core.types.Order.DESC, new PathBuilder(QAdmins.class, qAdmins.id.getMetadata()))
            );

        return query.fetch();
    }

}