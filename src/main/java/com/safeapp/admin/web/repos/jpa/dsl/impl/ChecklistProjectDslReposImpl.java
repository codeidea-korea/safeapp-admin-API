package com.safeapp.admin.web.repos.jpa.dsl.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.cmmn.BfPage;
import com.safeapp.admin.web.model.entity.ChecklistProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.binoofactory.cornsqure.web.model.entity.QChecklistProject;
import com.safeapp.admin.web.repos.jpa.dsl.ChecklistProjectDslRepos;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import io.netty.util.internal.StringUtil;

@Repository
public class ChecklistProjectDslReposImpl extends QuerydslRepositorySupport implements ChecklistProjectDslRepos {

    @PersistenceContext(name = "entityManager")
    private EntityManager entityManager;

    @Autowired
    public ChecklistProjectDslReposImpl() {
        super(ChecklistProject.class);
    }

    private JPAQuery selectFromWhere(ChecklistProject instance, QChecklistProject qChecklistProject) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery query = jpaQueryFactory.selectFrom(qChecklistProject);

        if (!StringUtil.isNullOrEmpty(instance.getName())) {
            query.where(qChecklistProject.name.like(instance.getName()));
        }
        if (!StringUtil.isNullOrEmpty(instance.getDetailContents())) {
            query.where(qChecklistProject.checklistProjectDetailList.any().contents.contains(instance.getDetailContents()));
        }
        if (!StringUtil.isNullOrEmpty(instance.getTag())) {
            query.where(qChecklistProject.tag.contains(instance.getTag()));
        }
        if (instance.getVisibled() != null) {
            query.where(qChecklistProject.visibled.eq(instance.getVisibled()));
        }
        if (instance.getProject().getId() > 0) {
            query.where(qChecklistProject.project.id.eq(instance.getProject().getId()));
        }
        if (instance.getUser().getId() > 0) {
            query.where(qChecklistProject.user.id.eq(instance.getUser().getId()));
        }
        if (instance.getId() > 0) {
            query.where(qChecklistProject.id.eq(instance.getId()));
        }
        return query;
    }

    private JPAQuery orderByFromWhere(ChecklistProject instance, QChecklistProject qChecklistProject, JPAQuery query) {
        if(instance.getCreatedAtDescended() == null && instance.getLikesDescended() == null && instance.getViewsDescended() == null) {
            query.orderBy(new OrderSpecifier(com.querydsl.core.types.Order.DESC,
                new PathBuilder(QChecklistProject.class, qChecklistProject.id.getMetadata())));
        } else {
            if (instance.getCreatedAtDescended() != null) {
                query.orderBy(new OrderSpecifier(
                    (instance.getCreatedAtDescended() == YN.Y ? com.querydsl.core.types.Order.DESC : com.querydsl.core.types.Order.ASC),
                    new PathBuilder(QChecklistProject.class, qChecklistProject.createdAt.getMetadata())));
            }
            if (instance.getLikesDescended() != null) {
                query.orderBy(new OrderSpecifier(
                    (instance.getLikesDescended() == YN.Y ? com.querydsl.core.types.Order.DESC : com.querydsl.core.types.Order.ASC),
                    new PathBuilder(QChecklistProject.class, qChecklistProject.likes.getMetadata())));
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
    public List<ChecklistProject> findAll(ChecklistProject instance, BfPage bfPage) {
        QChecklistProject qChecklistProject = QChecklistProject.checklistProject;
        JPAQuery query = selectFromWhere(instance, qChecklistProject);

        query
            .offset(bfPage.getOffset())
            .limit(bfPage.getPageSize());
        query = orderByFromWhere(instance, qChecklistProject, query);

        return query.fetch();
    }

    @Override
    public long countAll(ChecklistProject instance) {
        QChecklistProject qFile = QChecklistProject.checklistProject;
        JPAQuery query = selectFromWhere(instance, qFile);
        return query.fetchCount();
    }
}