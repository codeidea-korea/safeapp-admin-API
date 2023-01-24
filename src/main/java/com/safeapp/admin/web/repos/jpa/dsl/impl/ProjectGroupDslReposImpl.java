package com.safeapp.admin.web.repos.jpa.dsl.impl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.ProjectGroup;
import com.safeapp.admin.web.model.entity.QProjectGroup;
import com.safeapp.admin.web.repos.jpa.dsl.ProjectGroupDslRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ProjectGroupDslReposImpl extends QuerydslRepositorySupport implements ProjectGroupDslRepos {

    @PersistenceContext(name = "entityManager")
    private EntityManager entityManager;

    @Autowired
    public ProjectGroupDslReposImpl() {
        super(ProjectGroup.class);
    }

    private JPAQuery selectFromWhere(ProjectGroup prjGr, QProjectGroup qProjectGroup) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery query = jpaQueryFactory.selectFrom(qProjectGroup);

        /*
        if(prjGr.getProject() > 0) {
            query.where(qProjectGroup.project.eq(prjGr.getProject()));
        }
        */

        return query;
    }

    @Override
    public long countAll(ProjectGroup prjGr) {
        QProjectGroup qFile = QProjectGroup.projectGroup;
        JPAQuery query = selectFromWhere(prjGr, qFile);

        return query.fetchCount();
    }

    @Override
    public List<ProjectGroup> findAll(ProjectGroup prjGr, Pages pages) {
        QProjectGroup qProjectGroup = QProjectGroup.projectGroup;
        JPAQuery query = selectFromWhere(prjGr, qProjectGroup);

        query
            .offset(pages.getOffset())
            .limit(pages.getPageSize())
            .orderBy(
                new OrderSpecifier(com.querydsl.core.types.Order.DESC, new PathBuilder(QProjectGroup.class, qProjectGroup.id.getMetadata()))
            );

        return query.fetch();
    }

}