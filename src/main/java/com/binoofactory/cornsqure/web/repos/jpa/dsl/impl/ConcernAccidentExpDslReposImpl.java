package com.binoofactory.cornsqure.web.repos.jpa.dsl.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.binoofactory.cornsqure.web.data.YN;
import com.binoofactory.cornsqure.web.model.cmmn.BfPage;
import com.binoofactory.cornsqure.web.model.entity.ConcernAccidentExp;
import com.binoofactory.cornsqure.web.model.entity.QChecklistProject;
import com.binoofactory.cornsqure.web.model.entity.QConcernAccidentExp;
import com.binoofactory.cornsqure.web.repos.jpa.dsl.ConcernAccidentExpDslRepos;
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

    private JPAQuery selectFromWhere(ConcernAccidentExp instance, QConcernAccidentExp qConcernAccidentExp) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery query = jpaQueryFactory.selectFrom(qConcernAccidentExp);

        if (!StringUtil.isNullOrEmpty(instance.getName())) {
            query.where(qConcernAccidentExp.name.like(instance.getName()));
        }
        if (!StringUtil.isNullOrEmpty(instance.getTitle())) {
            query.where(qConcernAccidentExp.title.like(instance.getTitle()));
        }
        if (!StringUtil.isNullOrEmpty(instance.getDetailContents())) {
            query.where(qConcernAccidentExp.accidentReason.contains(instance.getDetailContents())
                .or(qConcernAccidentExp.accidentCause.contains(instance.getDetailContents()))
                .or(qConcernAccidentExp.causeDetail.contains(instance.getDetailContents()))
                .or(qConcernAccidentExp.response.contains(instance.getDetailContents()))
                .or(qConcernAccidentExp.accidentUserName.contains(instance.getDetailContents()))
                .or(qConcernAccidentExp.accidentType.contains(instance.getDetailContents())));
        }
        if (!StringUtil.isNullOrEmpty(instance.getTags())) {
            query.where(qConcernAccidentExp.tags.contains(instance.getTags()));
        }
        if (instance.getUserId() > 0) {
            query.where(qConcernAccidentExp.userId.eq(instance.getUserId()));
        }
        if (instance.getId() > 0) {
            query.where(qConcernAccidentExp.id.eq(instance.getId()));
        }
        return query;
    }

    private JPAQuery orderByFromWhere(ConcernAccidentExp instance, QConcernAccidentExp qChecklistProject, JPAQuery query) {
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
    public List<ConcernAccidentExp> findAll(ConcernAccidentExp instance, BfPage bfPage) {
        QConcernAccidentExp qConcernAccidentExp = QConcernAccidentExp.concernAccidentExp;
        JPAQuery query = selectFromWhere(instance, qConcernAccidentExp);

        query
            .offset(bfPage.getOffset())
            .limit(bfPage.getPageSize());
        query = orderByFromWhere(instance, qConcernAccidentExp, query);

        return query.fetch();
    }

    @Override
    public long countAll(ConcernAccidentExp instance) {
        QConcernAccidentExp qFile = QConcernAccidentExp.concernAccidentExp;
        JPAQuery query = selectFromWhere(instance, qFile);
        return query.fetchCount();
    }
}