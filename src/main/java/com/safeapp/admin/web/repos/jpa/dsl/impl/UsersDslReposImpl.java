package com.safeapp.admin.web.repos.jpa.dsl.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.safeapp.admin.web.model.cmmn.BfPage;
import com.safeapp.admin.web.model.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.binoofactory.cornsqure.web.model.entity.QUsers;
import com.safeapp.admin.web.repos.jpa.dsl.UsersDslRepos;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import io.netty.util.internal.StringUtil;

@Repository
public class UsersDslReposImpl extends QuerydslRepositorySupport implements UsersDslRepos {

    @PersistenceContext(name = "entityManager")
    private EntityManager entityManager;

    @Autowired
    public UsersDslReposImpl() {
        super(Users.class);
    }

    private JPAQuery selectFromWhere(Users instance, QUsers qUsers) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery query = jpaQueryFactory.selectFrom(qUsers);

        if (!StringUtil.isNullOrEmpty(instance.getPhoneNo())) {
            query.where(qUsers.phoneNo.like(instance.getPhoneNo()));
        }
        if (!StringUtil.isNullOrEmpty(instance.getEmail())) {
            query.where(qUsers.email.like(instance.getEmail()));
        }
        if (!StringUtil.isNullOrEmpty(instance.getUserName())) {
            query.where(qUsers.userName.like(instance.getUserName()));
        }
        if (instance.getDeleted() != null) {
            query.where(qUsers.deleted.eq(instance.getDeleted()));
        }
        if (instance.getMarketingAllowed() != null) {
            query.where(qUsers.marketingAllowed.eq(instance.getMarketingAllowed()));
        }
        if (instance.getMessageAllowed() != null) {
            query.where(qUsers.messageAllowed.eq(instance.getMessageAllowed()));
        }
        if (instance.getSnsAllowed() != null) {
            query.where(qUsers.snsAllowed.eq(instance.getSnsAllowed()));
        }
        if (instance.getType() != null) {
            query.where(qUsers.type.eq(instance.getType()));
        }
        if (instance.getId() > 0) {
            query.where(qUsers.id.eq(instance.getId()));
        }
        return query;
    }

    @Override
    public List<Users> findAll(Users instance, BfPage bfPage) {
        QUsers qUsers = QUsers.users;
        JPAQuery query = selectFromWhere(instance, qUsers);

        query
            .offset(bfPage.getOffset())
            .limit(bfPage.getPageSize())
            .orderBy(new OrderSpecifier(com.querydsl.core.types.Order.DESC,
                new PathBuilder(QUsers.class, qUsers.id.getMetadata())));

        return query.fetch();
    }

    @Override
    public long countAll(Users instance) {
        QUsers qFile = QUsers.users;
        JPAQuery query = selectFromWhere(instance, qFile);
        return query.fetchCount();
    }
}