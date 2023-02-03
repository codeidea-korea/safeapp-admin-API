package com.safeapp.admin.web.repos.jpa.dsl.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.querydsl.core.types.Projections;
import com.safeapp.admin.web.dto.response.ResponseProjectGroupDTO;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.QUserAuth;
import com.safeapp.admin.web.model.entity.UserAuth;
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

    private JPAQuery selectFromWhere(Users users, QUsers qUsers, QUserAuth qUserAuth) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);

        JPAQuery query =
            jpaQueryFactory

            .select
            (
                Projections.fields
                (
                    Users.class,
                    qUsers.id.as("id"),
                    qUsers.createdAt.as("createdAt"),
                    qUsers.updatedAt.as("updatedAt"),
                    qUsers.email.as("email"),
                    qUsers.image.as("image"),
                    qUsers.marketingAllowed.as("marketingAllowed"),
                    qUsers.marketingAllowedAt.as("marketingAllowedAt"),
                    qUsers.messageAllowed.as("messageAllowed"),
                    qUsers.messageAllowedAt.as("messageAllowedAt"),
                    qUsers.phoneNo.as("phoneNo"),
                    qUsers.snsAllowed.as("snsAllowed"),
                    qUsers.userType.as("userType"),
                    qUsers.userId.as("userId"),
                    qUsers.userName.as("userName"),
                    qUsers.snsType.as("snsType"),
                    qUsers.maxProjectGroupCount.as("maxProjectGroupCount"),
                    qUsers.emailAllowed.as("emailAllowed"),
                    qUserAuth.efectiveStartAt.as("efectiveStartAt"),
                    qUserAuth.efectiveEndAt.as("efectiveEndAt"),
                    qUserAuth.orderType.as("orderType")
                )
            ).from(qUsers)
            .leftJoin(qUserAuth).on(qUsers.id.eq(qUserAuth.user));

            if(!StringUtil.isNullOrEmpty(users.getUserId())) {
                query.where(qUsers.userId.like("%" + users.getUserId() + "%"));
            } else if(!StringUtil.isNullOrEmpty(users.getUserName())) {
                query.where(qUsers.userName.like("%" + users.getUserName()  + "%"));
            } else if(!StringUtil.isNullOrEmpty(users.getPhoneNo())) {
                query.where(qUsers.phoneNo.like("%" + users.getPhoneNo()  + "%"));
            }
            query.where(qUsers.deleteYn.eq(false));
            query.where(qUserAuth.status.eq("ing"));

        return query;
    }

    @Override
    public long countAll(Users users) {
        QUsers qUsers = QUsers.users;
        QUserAuth qUserAuth = QUserAuth.userAuth;

        JPAQuery query = selectFromWhere(users, qUsers, qUserAuth);
        return query.fetchCount();
    }

    @Override
    public List<Users> findAll(Users users, Pages pages) {
        QUsers qUsers = QUsers.users;
        QUserAuth qUserAuth = QUserAuth.userAuth;

        JPAQuery query = selectFromWhere(users, qUsers, qUserAuth);

        query
            .offset(pages.getOffset())
            .limit(pages.getPageSize())
            .orderBy
            (
                new OrderSpecifier(com.querydsl.core.types.Order.DESC, new PathBuilder(QUsers.class, qUsers.id.getMetadata()))
            );

        return query.fetch();
    }

}