package com.safeapp.admin.web.repos.jpa.dsl.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.RiskTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.safeapp.admin.web.model.entity.QRiskTemplate;
import com.safeapp.admin.web.repos.jpa.dsl.RiskTemplateDslRepos;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import io.netty.util.internal.StringUtil;

@Repository
public class RiskTemplateDslReposImpl extends QuerydslRepositorySupport implements RiskTemplateDslRepos {

    @PersistenceContext(name = "entityManager")
    private EntityManager entityManager;

    @Autowired
    public RiskTemplateDslReposImpl() {
        super(RiskTemplate.class);
    }

    private JPAQuery selectFromWhere(RiskTemplate instance, QRiskTemplate qRiskTemplate) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery query = jpaQueryFactory.selectFrom(qRiskTemplate);

        if (!StringUtil.isNullOrEmpty(instance.getTag())) {
            query.where(qRiskTemplate.tag.contains(instance.getTag()));
        }
        if (!StringUtil.isNullOrEmpty(instance.getName())) {
            query.where(qRiskTemplate.name.like(instance.getName()));
        }
        if (instance.getVisibled() != null) {
            query.where(qRiskTemplate.visibled.eq(instance.getVisibled()));
        }
        if (instance.getUser().getId() > 0) {
            query.where(qRiskTemplate.user.id.eq(instance.getUser().getId()));
        }
        if (instance.getId() > 0) {
            query.where(qRiskTemplate.id.eq(instance.getId()));
        }
        return query;
    }

    @Override
    public List<RiskTemplate> findAll(RiskTemplate instance, Pages bfPage) {
        QRiskTemplate qRiskTemplate = QRiskTemplate.riskTemplate;
        JPAQuery query = selectFromWhere(instance, qRiskTemplate);

        query
            .offset(bfPage.getOffset())
            .limit(bfPage.getPageSize())
            .orderBy(new OrderSpecifier(com.querydsl.core.types.Order.DESC,
                new PathBuilder(QRiskTemplate.class, qRiskTemplate.id.getMetadata())));

        return query.fetch();
    }

    @Override
    public long countAll(RiskTemplate instance) {
        QRiskTemplate qFile = QRiskTemplate.riskTemplate;
        JPAQuery query = selectFromWhere(instance, qFile);
        return query.fetchCount();
    }
}