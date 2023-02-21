package com.safeapp.admin.web.repos.jpa.dsl.impl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.Notice;
import com.safeapp.admin.web.model.entity.Policy;
import com.safeapp.admin.web.model.entity.QNotice;
import com.safeapp.admin.web.model.entity.QPolicy;
import com.safeapp.admin.web.repos.jpa.dsl.NoticeDslRepos;
import com.safeapp.admin.web.repos.jpa.dsl.PolicyDslRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;

@Repository
public class PolicyDslReposImpl extends QuerydslRepositorySupport implements PolicyDslRepos {

    @PersistenceContext(name = "entityManager")
    private EntityManager entityManager;

    @Autowired
    public PolicyDslReposImpl() {
        super(Policy.class);
    }

    private JPAQuery selectFrom(Policy policy, QPolicy qPolicy) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery query = jpaQueryFactory.selectFrom(qPolicy);

        return query;
    }

    @Override
    public long countAll(Policy policy) {
        QPolicy qPolicy = QPolicy.policy;
        JPAQuery query = selectFrom(policy, qPolicy);

        return query.fetchCount();
    }

    @Override
    public List<Policy> findAll(Policy policy, Pages pages) {
        QPolicy qPolicy = QPolicy.policy;
        JPAQuery query = selectFrom(policy, qPolicy);

        query.orderBy(new OrderSpecifier(com.querydsl.core.types.Order.DESC, new PathBuilder(QPolicy.class, qPolicy.id.getMetadata())));

        return query.fetch();
    }

}