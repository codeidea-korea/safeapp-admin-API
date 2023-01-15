package com.safeapp.admin.web.repos.jpa.dsl.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.safeapp.admin.web.model.entity.QProjectManager;
import com.safeapp.admin.web.model.cmmn.BfPage;
import com.safeapp.admin.web.model.entity.ProjectManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.safeapp.admin.web.repos.jpa.dsl.ProjectManagersDslRepos;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class ProjectManagersDslReposImpl extends QuerydslRepositorySupport implements ProjectManagersDslRepos {

    @PersistenceContext(name = "entityManager")
    private EntityManager entityManager;

    @Autowired
    public ProjectManagersDslReposImpl() {
        super(ProjectManager.class);
    }

    private JPAQuery selectFromWhere(ProjectManager instance, QProjectManager qProjectManager) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery query = jpaQueryFactory.selectFrom(qProjectManager);

        if (instance.getUser().getId() > 0) {
            query.where(qProjectManager.user.id.eq(instance.getId()));
        }
        if (instance.getId() > 0) {
            query.where(qProjectManager.id.eq(instance.getId()));
        }
        return query;
    }

    @Override
    public List<ProjectManager> findAll(ProjectManager instance, BfPage bfPage) {
        QProjectManager qProjectManagers = QProjectManager.projectManager;
        JPAQuery query = selectFromWhere(instance, qProjectManagers);

        query
            .offset(bfPage.getOffset())
            .limit(bfPage.getPageSize())
            .orderBy(new OrderSpecifier(com.querydsl.core.types.Order.DESC,
                new PathBuilder(QProjectManager.class, qProjectManagers.id.getMetadata())));

        return query.fetch();
    }

    @Override
    public long countAll(ProjectManager instance) {
        QProjectManager qFile = QProjectManager.projectManager;
        JPAQuery query = selectFromWhere(instance, qFile);
        return query.fetchCount();
    }
}