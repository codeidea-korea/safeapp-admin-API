package com.safeapp.admin.web.repos.jpa.dsl.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.CheckListTemplate;
import com.safeapp.admin.web.model.entity.QCheckListTemplate;
import com.safeapp.admin.web.repos.jpa.dsl.CheckListTemplateDslRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import io.netty.util.internal.StringUtil;

@Repository
public class CheckListTemplateDslReposImpl extends QuerydslRepositorySupport implements CheckListTemplateDslRepos {

    @PersistenceContext(name = "entityManager")
    private EntityManager entityManager;

    @Autowired
    public CheckListTemplateDslReposImpl() { super(CheckListTemplate.class); }

    private JPAQuery selectFromWhere(CheckListTemplate chkTmp, QCheckListTemplate qChecklistTemplate) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery query = jpaQueryFactory.selectFrom(qChecklistTemplate);

        if(!StringUtil.isNullOrEmpty(chkTmp.getName())) {
            query.where(qChecklistTemplate.name.like(chkTmp.getName()));
        }
        if(!StringUtil.isNullOrEmpty(chkTmp.getTag())) {
            query.where(qChecklistTemplate.tag.contains(chkTmp.getTag()));
        }
        if(chkTmp.getUser().getId() > 0) {
            query.where(qChecklistTemplate.user.id.eq(chkTmp.getUser().getId()));
        }
        if(chkTmp.getId() > 0) {
            query.where(qChecklistTemplate.id.eq(chkTmp.getId()));
        }

        return query;
    }

    @Override
    public long countAll(CheckListTemplate chkTmp) {
        QCheckListTemplate qFile = QCheckListTemplate.checkListTemplate;
        JPAQuery query = selectFromWhere(chkTmp, qFile);

        return query.fetchCount();
    }

    @Override
    public List<CheckListTemplate> findAll(CheckListTemplate chkTmp, Pages pages) {
        QCheckListTemplate qChecklistTemplate = QCheckListTemplate.checkListTemplate;
        JPAQuery query = selectFromWhere(chkTmp, qChecklistTemplate);

        query
            .offset(pages.getOffset())
            .limit(pages.getPageSize())
            .orderBy(new OrderSpecifier(com.querydsl.core.types.Order.DESC, new PathBuilder(QCheckListTemplate.class, qChecklistTemplate.id.getMetadata())));

        return query.fetch();
    }

}