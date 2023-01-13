package com.safeapp.admin.web.repos.jpa.dsl.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.safeapp.admin.web.model.cmmn.BfPage;
import com.safeapp.admin.web.model.entity.Notice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.binoofactory.cornsqure.web.model.entity.QNotice;
import com.safeapp.admin.web.repos.jpa.dsl.NoticeDslRepos;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import io.netty.util.internal.StringUtil;

@Repository
public class NoticeDslReposImpl extends QuerydslRepositorySupport implements NoticeDslRepos {

    @PersistenceContext(name = "entityManager")
    private EntityManager entityManager;

    @Autowired
    public NoticeDslReposImpl() {
        super(Notice.class);
    }

    private JPAQuery selectFromWhere(Notice instance, QNotice qNotice) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery query = jpaQueryFactory.selectFrom(qNotice);

        if (!StringUtil.isNullOrEmpty(instance.getTitle())) {
            query.where(qNotice.title.like(instance.getTitle()));
        }
        if (!StringUtil.isNullOrEmpty(instance.getContents())) {
            query.where(qNotice.contents.contains(instance.getContents()));
        }
        if (instance.getType() != null) {
            query.where(qNotice.type.eq(instance.getType()));
        }
        if (instance.getUserId() > 0) {
            query.where(qNotice.userId.eq(instance.getUserId()));
        }
        if (instance.getId() > 0) {
            query.where(qNotice.id.eq(instance.getId()));
        }
        return query;
    }

    @Override
    public List<Notice> findAll(Notice instance, BfPage bfPage) {
        QNotice qNotice = QNotice.notice;
        JPAQuery query = selectFromWhere(instance, qNotice);

        query
            .offset(bfPage.getOffset())
            .limit(bfPage.getPageSize())
            .orderBy(new OrderSpecifier(com.querydsl.core.types.Order.DESC,
                new PathBuilder(QNotice.class, qNotice.id.getMetadata())));

        return query.fetch();
    }

    @Override
    public long countAll(Notice instance) {
        QNotice qFile = QNotice.notice;
        JPAQuery query = selectFromWhere(instance, qFile);
        return query.fetchCount();
    }
}