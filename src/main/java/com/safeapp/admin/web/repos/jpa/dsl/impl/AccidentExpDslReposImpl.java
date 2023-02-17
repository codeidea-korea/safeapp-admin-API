package com.safeapp.admin.web.repos.jpa.dsl.impl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.AccidentExp;
import com.safeapp.admin.web.model.entity.QAccidentExp;
import com.safeapp.admin.web.repos.jpa.dsl.AccidentExpDslRepos;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class AccidentExpDslReposImpl extends QuerydslRepositorySupport implements AccidentExpDslRepos {

    @PersistenceContext(name = "entityManager")
    private EntityManager entityManager;

    @Autowired
    public AccidentExpDslReposImpl() {
        super(AccidentExp.class);
    }

    private JPAQuery selectFromWhere(AccidentExp accExp, QAccidentExp qAccExp) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery query = jpaQueryFactory.selectFrom(qAccExp);

        if(!StringUtil.isNullOrEmpty(accExp.getKeyword())) {
            query.where(qAccExp.title.contains(accExp.getKeyword()).or(qAccExp.tags.contains(accExp.getKeyword())));
        }
        if(!StringUtil.isNullOrEmpty(accExp.getAdminName())) {
            query.where(qAccExp.admin.adminName.contains(accExp.getAdminName()));
        }
        if(!StringUtil.isNullOrEmpty(accExp.getPhoneNo())) {
            query.where(qAccExp.admin.phoneNo.contains(accExp.getPhoneNo()));
        }
        if(!StringUtil.isNullOrEmpty(accExp.getCreatedAtStart())) {
            query.where(qAccExp.createdAt.after(LocalDateTime.parse(accExp.getCreatedAtStart(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))));
        } else {
            query.where(qAccExp.createdAt.after(LocalDateTime.parse("1000-01-01 00:00:00.000", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))));
        }
        if(!StringUtil.isNullOrEmpty(accExp.getCreatedAtEnd())) {
            query.where(qAccExp.createdAt.before(LocalDateTime.parse(accExp.getCreatedAtEnd(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")).plusDays(1)));
        } else {
            query.where(qAccExp.createdAt.before(LocalDateTime.parse("9999-12-31 23:59:59.999", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))));
        }
        query.where(qAccExp.deleteYn.eq(false));

        return query;
    }

    private JPAQuery orderBy(AccidentExp accExp, QAccidentExp qAccExp, JPAQuery query) {
        if(accExp.getCreatedAtDesc() == null && accExp.getViewsDesc() == null) {
            query.orderBy
            (
                new OrderSpecifier(com.querydsl.core.types.Order.DESC,
                new PathBuilder(QAccidentExp.class, qAccExp.id.getMetadata()))
            );
        } else {
            if(accExp.getCreatedAtDesc() != null) {
                query.orderBy
                (
                    new OrderSpecifier((accExp.getCreatedAtDesc() == YN.Y ? com.querydsl.core.types.Order.DESC : com.querydsl.core.types.Order.ASC),
                    new PathBuilder(QAccidentExp.class, qAccExp.createdAt.getMetadata()))
                );
            }
            if(accExp.getViewsDesc() != null) {
                query.orderBy
                (
                    new OrderSpecifier((accExp.getViewsDesc() == YN.Y ? com.querydsl.core.types.Order.DESC : com.querydsl.core.types.Order.ASC),
                    new PathBuilder(QAccidentExp.class, qAccExp.views.getMetadata()))
                );
            }
        }

        return query;
    }

    @Override
    public long countAll(AccidentExp accExp) {
        QAccidentExp qFile = QAccidentExp.accidentExp;
        JPAQuery query = selectFromWhere(accExp, qFile);

        return query.fetchCount();
    }

    @Override
    public List<AccidentExp> findAll(AccidentExp accExp, Pages pages) {
        QAccidentExp qAccExp = QAccidentExp.accidentExp;
        JPAQuery query = selectFromWhere(accExp, qAccExp);

        query
            .offset(pages.getOffset())
            .limit(pages.getPageSize());
        query = orderBy(accExp, qAccExp, query);

        return query.fetch();
    }

}