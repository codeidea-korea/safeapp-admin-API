package com.safeapp.admin.web.repos.jpa.dsl.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.RiskCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.safeapp.admin.web.model.entity.QCheckListProject;
import com.safeapp.admin.web.model.entity.QRiskCheck;
import com.safeapp.admin.web.repos.jpa.dsl.RiskCheckDslRepos;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import io.netty.util.internal.StringUtil;

@Repository
public class RiskCheckDslReposImpl extends QuerydslRepositorySupport implements RiskCheckDslRepos {

    @PersistenceContext(name = "entityManager")
    private EntityManager entityManager;

    @Autowired
    public RiskCheckDslReposImpl() {
        super(RiskCheck.class);
    }

    private JPAQuery selectFromWhere(RiskCheck instance, QRiskCheck qRiskCheck) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery query = jpaQueryFactory.selectFrom(qRiskCheck);

        if (!StringUtil.isNullOrEmpty(instance.getTag())) {
            query.where(qRiskCheck.tag.contains(instance.getTag()));
        }
        if (!StringUtil.isNullOrEmpty(instance.getName())) {
            query.where(qRiskCheck.name.like(instance.getName()));
        }

        if (instance.getVisibled() != null) {
            query.where(qRiskCheck.visibled.eq(instance.getVisibled()));
        }

        if (instance.getId() > 0) {
            query.where(qRiskCheck.id.eq(instance.getId()));
        }
        return query;
    }

    private JPAQuery orderByFromWhere(RiskCheck instance, QRiskCheck qRiskCheck, JPAQuery query) {
        if(instance.getCreatedAtDescended() == null && instance.getLikesDescended() == null && instance.getViewsDescended() == null) {
            query.orderBy(new OrderSpecifier(com.querydsl.core.types.Order.DESC,
                new PathBuilder(QCheckListProject.class, qRiskCheck.id.getMetadata())));
        } else {
            if (instance.getCreatedAtDescended() != null) {
                query.orderBy(new OrderSpecifier(
                    (instance.getCreatedAtDescended() == YN.Y ? com.querydsl.core.types.Order.DESC : com.querydsl.core.types.Order.ASC),
                    new PathBuilder(QCheckListProject.class, qRiskCheck.createdAt.getMetadata())));
            }
            if (instance.getLikesDescended() != null) {
                query.orderBy(new OrderSpecifier(
                    (instance.getLikesDescended() == YN.Y ? com.querydsl.core.types.Order.DESC : com.querydsl.core.types.Order.ASC),
                    new PathBuilder(QCheckListProject.class, qRiskCheck.likes.getMetadata())));
            }
            if (instance.getViewsDescended() != null) {
                query.orderBy(new OrderSpecifier(
                    (instance.getViewsDescended() == YN.Y ? com.querydsl.core.types.Order.DESC : com.querydsl.core.types.Order.ASC),
                    new PathBuilder(QCheckListProject.class, qRiskCheck.views.getMetadata())));
            }
        }

        return query;
    }

    @Override
    public List<RiskCheck> findAll(RiskCheck instance, Pages bfPage) {
        QRiskCheck qRiskCheck = QRiskCheck.riskCheck;
        JPAQuery query = selectFromWhere(instance, qRiskCheck);

        query
            .offset(bfPage.getOffset())
            .limit(bfPage.getPageSize());
        query = orderByFromWhere(instance, qRiskCheck, query);

        return query.fetch();
    }

    @Override
    public long countAll(RiskCheck instance) {
        QRiskCheck qFile = QRiskCheck.riskCheck;
        JPAQuery query = selectFromWhere(instance, qFile);
        return query.fetchCount();
    }
}