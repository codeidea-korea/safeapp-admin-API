package com.safeapp.admin.web.repos.jpa.dsl.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.safeapp.admin.web.repos.jpa.dsl.ConcernAccidentExpDslRepos;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import io.netty.util.internal.StringUtil;

@Repository
public class ConcernAccidentExpDslReposImpl extends QuerydslRepositorySupport implements ConcernAccidentExpDslRepos {

    @PersistenceContext(name = "entityManager")
    private EntityManager entityManager;

    @Autowired
    public ConcernAccidentExpDslReposImpl() {
        super(ConcernAccidentExp.class);
    }

    private JPAQuery selectFromWhere(ConcernAccidentExp conExp, QConcernAccidentExp qConExp) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery query = jpaQueryFactory.selectFrom(qConExp);

        if(!StringUtil.isNullOrEmpty(conExp.getKeyword())) {
            query.where(qConExp.title.contains(conExp.getKeyword()).or(qConExp.tags.contains(conExp.getKeyword())));
        }
        if(!StringUtil.isNullOrEmpty(conExp.getAdminName())) {
            query.where(qConExp.admin.adminName.contains(conExp.getAdminName()));
        }
        if(!StringUtil.isNullOrEmpty(conExp.getPhoneNo())) {
            query.where(qConExp.admin.phoneNo.contains(conExp.getPhoneNo()));
        }
        if(!StringUtil.isNullOrEmpty(conExp.getCreatedAtStart())) {
            query.where(qConExp.createdAt.after(LocalDateTime.parse(conExp.getCreatedAtStart(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))));
        } else {
            query.where(qConExp.createdAt.after(LocalDateTime.parse("1000-01-01 00:00:00.000", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))));
        }
        if(!StringUtil.isNullOrEmpty(conExp.getCreatedAtEnd())) {
            query.where(qConExp.createdAt.before(LocalDateTime.parse(conExp.getCreatedAtEnd(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")).plusDays(1)));
        } else {
            query.where(qConExp.createdAt.before(LocalDateTime.parse("9999-12-31 23:59:59.999", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))));
        }
        query.where(qConExp.deleteYn.eq(false));

        return query;
    }

    private JPAQuery orderBy(ConcernAccidentExp conExp, QConcernAccidentExp qConExp, JPAQuery query) {
        if(conExp.getCreatedAtDesc() == null && conExp.getViewsDesc() == null) {
            query.orderBy
            (
                new OrderSpecifier(com.querydsl.core.types.Order.DESC,
                new PathBuilder(QConcernAccidentExp.class, qConExp.id.getMetadata()))
            );
        } else {
            if(conExp.getCreatedAtDesc() != null) {
                query.orderBy
                (
                    new OrderSpecifier((conExp.getCreatedAtDesc() == YN.Y ? com.querydsl.core.types.Order.DESC : com.querydsl.core.types.Order.ASC),
                    new PathBuilder(QConcernAccidentExp.class, qConExp.createdAt.getMetadata()))
                );
            }
            if(conExp.getViewsDesc() != null) {
                query.orderBy
                (
                    new OrderSpecifier((conExp.getViewsDesc() == YN.Y ? com.querydsl.core.types.Order.DESC : com.querydsl.core.types.Order.ASC),
                    new PathBuilder(QConcernAccidentExp.class, qConExp.views.getMetadata()))
                );
            }
        }

        return query;
    }

    @Override
    public long countAll(ConcernAccidentExp conExp) {
        QConcernAccidentExp qFile = QConcernAccidentExp.concernAccidentExp;
        JPAQuery query = selectFromWhere(conExp, qFile);

        return query.fetchCount();
    }

    @Override
    public List<ConcernAccidentExp> findAll(ConcernAccidentExp conExp, Pages pages) {
        QConcernAccidentExp qConExp = QConcernAccidentExp.concernAccidentExp;
        JPAQuery query = selectFromWhere(conExp, qConExp);

        query
            .offset(pages.getOffset())
            .limit(pages.getPageSize());
        query = orderBy(conExp, qConExp, query);

        return query.fetch();
    }

    private JPAQuery selectFromWhereReport(ConcernAccidentExp conExp, QConcernAccidentExp qConExp, QReports qReport) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery query = jpaQueryFactory.selectFrom(qConExp).innerJoin(qReport).on(qConExp.id.eq(qReport.concernAccidentExp.id));

        if(!StringUtil.isNullOrEmpty(conExp.getKeyword())) {
            query.where(qConExp.title.contains(conExp.getKeyword()).or(qConExp.tags.contains(conExp.getKeyword())));
        }
        query.where(qConExp.deleteYn.eq(false)).where(qReport.deleteYn.eq(false)).groupBy(qReport.concernAccidentExp);

        return query;
    }

    @Override
    public List<Reports> findReports(long id) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        QReports qReport = QReports.reports;

        JPAQuery query = jpaQueryFactory.selectFrom(qReport).where(qReport.concernAccidentExp.id.eq(id));
        query.orderBy(new OrderSpecifier(com.querydsl.core.types.Order.DESC, new PathBuilder(QReports.class, qReport.id.getMetadata())));

        return query.fetch();
    }

    @Override
    public long countAllReport(ConcernAccidentExp conExp) {
        QConcernAccidentExp qConExp = QConcernAccidentExp.concernAccidentExp;
        QReports qReport = QReports.reports;

        JPAQuery query = selectFromWhereReport(conExp, qConExp, qReport);

        return query.fetchCount();
    }

    @Override
    public List<ConcernAccidentExp> findAllReport(ConcernAccidentExp conExp, Pages pages) {
        QConcernAccidentExp qConExp = QConcernAccidentExp.concernAccidentExp;
        QReports qReport = QReports.reports;

        JPAQuery query = selectFromWhereReport(conExp, qConExp, qReport);

        query
            .offset(pages.getOffset())
            .limit(pages.getPageSize())
            .groupBy(qReport.concernAccidentExp);
        query = orderBy(conExp, qConExp, query);

        return query.fetch();
    }

}