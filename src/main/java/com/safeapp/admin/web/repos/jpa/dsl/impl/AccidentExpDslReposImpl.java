package com.safeapp.admin.web.repos.jpa.dsl.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.cmmn.BfPage;
import com.safeapp.admin.web.model.entity.AccidentExp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.binoofactory.cornsqure.web.model.entity.QAccidentExp;
import com.binoofactory.cornsqure.web.model.entity.QChecklistProject;
import com.safeapp.admin.web.repos.jpa.dsl.AccidentExpDslRepos;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import io.netty.util.internal.StringUtil;

@Repository
public class AccidentExpDslReposImpl extends QuerydslRepositorySupport implements AccidentExpDslRepos {

    @PersistenceContext(name = "entityManager")
    private EntityManager entityManager;

    @Autowired
    public AccidentExpDslReposImpl() {
        super(AccidentExp.class);
    }

    private JPAQuery selectFromWhere(AccidentExp instance, QAccidentExp qAccidentExp) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery query = jpaQueryFactory.selectFrom(qAccidentExp);

        if (!StringUtil.isNullOrEmpty(instance.getTitle())) {
            query.where(qAccidentExp.title.like(instance.getTitle()));
        }
        if (!StringUtil.isNullOrEmpty(instance.getName())) {
            query.where(qAccidentExp.name.like(instance.getName()));
        }
        if (!StringUtil.isNullOrEmpty(instance.getDetailContents())) {
            query.where(qAccidentExp.accidentReason.contains(instance.getDetailContents())
                .or(qAccidentExp.accidentCause.contains(instance.getDetailContents()))
                .or(qAccidentExp.causeDetail.contains(instance.getDetailContents()))
                .or(qAccidentExp.response.contains(instance.getDetailContents())));
        }
        if (!StringUtil.isNullOrEmpty(instance.getTags())) {
            query.where(qAccidentExp.tags.contains(instance.getTags()));
        }
        if (instance.getUser().getId() > 0) {
            query.where(qAccidentExp.user.id.eq(instance.getUser().getId()));
        }
        if (instance.getId() > 0) {
            query.where(qAccidentExp.id.eq(instance.getId()));
        }
        return query;
    }

    private JPAQuery orderByFromWhere(AccidentExp instance, QAccidentExp qChecklistProject, JPAQuery query) {
        if(instance.getCreatedAtDescended() == null && instance.getViewsDescended() == null) {
            query.orderBy(new OrderSpecifier(com.querydsl.core.types.Order.DESC,
                new PathBuilder(QChecklistProject.class, qChecklistProject.id.getMetadata())));
        } else {
            if (instance.getCreatedAtDescended() != null) {
                query.orderBy(new OrderSpecifier(
                    (instance.getCreatedAtDescended() == YN.Y ? com.querydsl.core.types.Order.DESC : com.querydsl.core.types.Order.ASC),
                    new PathBuilder(QChecklistProject.class, qChecklistProject.createdAt.getMetadata())));
            }
            if (instance.getViewsDescended() != null) {
                query.orderBy(new OrderSpecifier(
                    (instance.getViewsDescended() == YN.Y ? com.querydsl.core.types.Order.DESC : com.querydsl.core.types.Order.ASC),
                    new PathBuilder(QChecklistProject.class, qChecklistProject.views.getMetadata())));
            }
        }

        return query;
    }

    @Override
    public List<AccidentExp> findAll(AccidentExp instance, BfPage bfPage) {
        QAccidentExp qAccidentExp = QAccidentExp.accidentExp;
        JPAQuery query = selectFromWhere(instance, qAccidentExp);

        query
            .offset(bfPage.getOffset())
            .limit(bfPage.getPageSize());
        query = orderByFromWhere(instance, qAccidentExp, query);

        return query.fetch();
    }

    @Override
    public long countAll(AccidentExp instance) {
        QAccidentExp qFile = QAccidentExp.accidentExp;
        JPAQuery query = selectFromWhere(instance, qFile);
        return query.fetchCount();
    }
}
