package com.safeapp.admin.web.repos.jpa.dsl.impl;

import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.Notice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.safeapp.admin.web.model.entity.QNotice;
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

    private JPAQuery selectFromWhere(Notice notice, QNotice qNotice) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery query = jpaQueryFactory.selectFrom(qNotice);

        if(!Objects.isNull(notice.getType())) {
            query.where(qNotice.type.eq(notice.getType()));
        }
        query.where(qNotice.deleteYn.eq(false));

        return query;
    }

    @Override
    public long countAll(Notice notice) {
        QNotice qNotice = QNotice.notice;
        JPAQuery query = selectFromWhere(notice, qNotice);

        return query.fetchCount();
    }

    @Override
    public List<Notice> findAll(Notice notice, Pages pages) {
        QNotice qNotice = QNotice.notice;
        JPAQuery query = selectFromWhere(notice, qNotice);

        query
            .offset(pages.getOffset())
            .limit(pages.getPageSize())
            .orderBy(new OrderSpecifier(com.querydsl.core.types.Order.DESC, new PathBuilder(QNotice.class, qNotice.id.getMetadata())));

        return query.fetch();
    }

}