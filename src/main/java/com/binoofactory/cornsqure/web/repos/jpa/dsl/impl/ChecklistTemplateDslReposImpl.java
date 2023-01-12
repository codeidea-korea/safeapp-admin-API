package com.binoofactory.cornsqure.web.repos.jpa.dsl.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.binoofactory.cornsqure.web.model.cmmn.BfPage;
import com.binoofactory.cornsqure.web.model.entity.ChecklistTemplate;
import com.binoofactory.cornsqure.web.model.entity.QChecklistTemplate;
import com.binoofactory.cornsqure.web.repos.jpa.dsl.ChecklistTemplateDslRepos;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import io.netty.util.internal.StringUtil;

@Repository
public class ChecklistTemplateDslReposImpl extends QuerydslRepositorySupport implements ChecklistTemplateDslRepos {

    @PersistenceContext(name = "entityManager")
    private EntityManager entityManager;

    @Autowired
    public ChecklistTemplateDslReposImpl() {
        super(ChecklistTemplate.class);
    }

    private JPAQuery selectFromWhere(ChecklistTemplate instance, QChecklistTemplate qChecklistTemplate) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery query = jpaQueryFactory.selectFrom(qChecklistTemplate);

        if (!StringUtil.isNullOrEmpty(instance.getName())) {
            query.where(qChecklistTemplate.name.like(instance.getName()));
        }
        if (!StringUtil.isNullOrEmpty(instance.getTag())) {
            query.where(qChecklistTemplate.tag.contains(instance.getTag()));
        }
        if (instance.getUser().getId() > 0) {
            query.where(qChecklistTemplate.user.id.eq(instance.getUser().getId()));
        }
        if (instance.getId() > 0) {
            query.where(qChecklistTemplate.id.eq(instance.getId()));
        }
        return query;
    }

    @Override
    public List<ChecklistTemplate> findAll(ChecklistTemplate instance, BfPage bfPage) {
        QChecklistTemplate qChecklistTemplate = QChecklistTemplate.checklistTemplate;
        JPAQuery query = selectFromWhere(instance, qChecklistTemplate);

        query
            .offset(bfPage.getOffset())
            .limit(bfPage.getPageSize())
            .orderBy(new OrderSpecifier(com.querydsl.core.types.Order.DESC,
                new PathBuilder(QChecklistTemplate.class, qChecklistTemplate.id.getMetadata())));

        return query.fetch();
    }

    @Override
    public long countAll(ChecklistTemplate instance) {
        QChecklistTemplate qFile = QChecklistTemplate.checklistTemplate;
        JPAQuery query = selectFromWhere(instance, qFile);
        return query.fetchCount();
    }
}