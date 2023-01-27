package com.safeapp.admin.web.repos.jpa.dsl.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.Project;
import com.safeapp.admin.web.model.entity.ProjectGroup;
import com.safeapp.admin.web.model.entity.QProjectGroup;
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

    private JPAQuery selectFromWhere(Project project, QProject qProject) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery query = jpaQueryFactory.selectFrom(qProject);

        if(!StringUtil.isNullOrEmpty(project.getName())) {
            query.where(qProject.name.like(project.getName()));
        }
        if(!StringUtil.isNullOrEmpty(project.getContents())) {
            query.where(qProject.name.like(project.getContents()));
        }
        if(project.getStatus() != null) {
            query.where(qProject.status.eq(project.getStatus()));
        }
        if(project.getId() > 0) {
            query.where(qProject.id.eq(project.getId()));
        }

        return query;
    }

    @Override
    public long countAll(Project project) {
        QProject qFile = QProject.project;
        JPAQuery query = selectFromWhere(project, qFile);

        return query.fetchCount();
    }

    @Override
    public List<Project> findAll(Project project, Pages pages) {
        QProject qProject = QProject.project;
        JPAQuery query = selectFromWhere(project, qProject);

        query
            .offset(pages.getOffset())
            .limit(pages.getPageSize())
            .orderBy(
                new OrderSpecifier(com.querydsl.core.types.Order.DESC, new PathBuilder(QProject.class, qProject.id.getMetadata()))
            );

        return query.fetch();
    }

}