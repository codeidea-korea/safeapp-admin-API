package com.safeapp.admin.web.repos.jpa.dsl.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.safeapp.admin.web.model.entity.QUsers;
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

    private JPAQuery selectFromWhere(Users users, QUsers qUsers) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery query = jpaQueryFactory.selectFrom(qUsers);

        if(!StringUtil.isNullOrEmpty(users.getUserId())) {
            query.where(qUsers.userId.like("%" + users.getUserId() + "%"));
        } else if(!StringUtil.isNullOrEmpty(users.getUserName())) {
            query.where(qUsers.userName.like("%" + users.getUserName()  + "%"));
        } else if(!StringUtil.isNullOrEmpty(users.getPhoneNo())) {
            query.where(qUsers.phoneNo.like("%" + users.getPhoneNo()  + "%"));
        }

        return query;
    }

    @Override
    public long countAll(Users users) {
        QUsers qFile = QUsers.users;
        JPAQuery query = selectFromWhere(users, qFile);

        return query.fetchCount();
    }

    @Override
    public List<Users> findAll(Users users, Pages pages) {
        QUsers qUsers = QUsers.users;
        JPAQuery query = selectFromWhere(users, qUsers);

        query
            .offset(pages.getOffset())
            .limit(pages.getPageSize())
            .orderBy(
                new OrderSpecifier(com.querydsl.core.types.Order.DESC, new PathBuilder(QUsers.class, qUsers.id.getMetadata()))
            );

        return query.fetch();
    }

}