package com.safeapp.admin.web.repos.jpa.dsl.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.safeapp.admin.web.model.entity.QProject;
import com.safeapp.admin.web.repos.jpa.dsl.ProjectDslRepos;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import io.netty.util.internal.StringUtil;

@Repository
public class ProjectDslReposImpl extends QuerydslRepositorySupport implements ProjectDslRepos {

    @PersistenceContext(name = "entityManager")
    private EntityManager entityManager;

    @Autowired
    public ProjectDslReposImpl() {
        super(Project.class);
    }

    private JPAQuery selectFromWhere(Project instance, QProject qProject) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery query = jpaQueryFactory.selectFrom(qProject);

        if (!StringUtil.isNullOrEmpty(instance.getName())) {
            query.where(qProject.name.like(instance.getName()));
        }
        if (!StringUtil.isNullOrEmpty(instance.getContents())) {
            query.where(qProject.name.like(instance.getContents()));
        }
        if (instance.getStatus() != null) {
            query.where(qProject.status.eq(instance.getStatus()));
        }
        if (instance.getId() > 0) {
            query.where(qProject.id.eq(instance.getId()));
        }
        return query;
    }

    @Override
    public List<Project> findAll(Project instance, Pages bfPage) {
        QProject qProject = QProject.project;
        JPAQuery query = selectFromWhere(instance, qProject);

        query
            .offset(bfPage.getOffset())
            .limit(bfPage.getPageSize())
            .orderBy(new OrderSpecifier(com.querydsl.core.types.Order.DESC,
                new PathBuilder(QProject.class, qProject.id.getMetadata())));

        return query.fetch();
    }

    @Override
    public long countAll(Project instance) {
        QProject qFile = QProject.project;
        JPAQuery query = selectFromWhere(instance, qFile);
        return query.fetchCount();
    }
}