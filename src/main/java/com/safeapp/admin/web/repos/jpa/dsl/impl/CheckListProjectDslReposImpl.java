package com.safeapp.admin.web.repos.jpa.dsl.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.CheckListProject;
import com.safeapp.admin.web.model.entity.QCheckListProject;
import com.safeapp.admin.web.repos.jpa.dsl.CheckListProjectDslRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import io.netty.util.internal.StringUtil;

@Repository
public class CheckListProjectDslReposImpl extends QuerydslRepositorySupport implements CheckListProjectDslRepos {

    @PersistenceContext(name = "entityManager")
    private EntityManager entityManager;

    @Autowired
    public CheckListProjectDslReposImpl() {
        super(CheckListProject.class);
    }

    private JPAQuery selectFromWhere(CheckListProject chkPrj, QCheckListProject qChecklistProject) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery query = jpaQueryFactory.selectFrom(qChecklistProject);

        if(!StringUtil.isNullOrEmpty(chkPrj.getTag())) {
            query.where(qChecklistProject.tag.contains(chkPrj.getTag()));
        }
        if(chkPrj.getVisibled() != null) {
            query.where(qChecklistProject.visibled.eq(chkPrj.getVisibled()));
        }

        return query;
    }

    private JPAQuery orderByFromWhere(CheckListProject chkPrj, QCheckListProject qChecklistProject, JPAQuery query) {
        if(chkPrj.getCreatedAtDescended() == null && chkPrj.getLikesDescended() == null && chkPrj.getViewsDescended() == null) {
            query.orderBy(new OrderSpecifier(com.querydsl.core.types.Order.DESC, new PathBuilder(QCheckListProject.class, qChecklistProject.id.getMetadata())));

        } else {
            if(chkPrj.getCreatedAtDescended() != null) {
                query.orderBy(
                    new OrderSpecifier(
                        (chkPrj.getCreatedAtDescended() == YN.Y ? com.querydsl.core.types.Order.DESC : com.querydsl.core.types.Order.ASC),
                        new PathBuilder(QCheckListProject.class, qChecklistProject.createdAt.getMetadata())));
            }
            if(chkPrj.getLikesDescended() != null) {
                query.orderBy(
                    new OrderSpecifier(
                        (chkPrj.getLikesDescended() == YN.Y ? com.querydsl.core.types.Order.DESC : com.querydsl.core.types.Order.ASC),
                        new PathBuilder(QCheckListProject.class, qChecklistProject.likes.getMetadata())));
            }
            if(chkPrj.getViewsDescended() != null) {
                query.orderBy(
                    new OrderSpecifier(
                        (chkPrj.getViewsDescended() == YN.Y ? com.querydsl.core.types.Order.DESC : com.querydsl.core.types.Order.ASC),
                        new PathBuilder(QCheckListProject.class, qChecklistProject.views.getMetadata())));
            }
        }

        return query;
    }

    @Override
    public long countAll(CheckListProject chkPrj) {
        QCheckListProject qFile = QCheckListProject.checkListProject;
        JPAQuery query = selectFromWhere(chkPrj, qFile);

        return query.fetchCount();
    }

    @Override
    public List<CheckListProject> findAll(CheckListProject chkPrj, Pages pages) {
        QCheckListProject qChecklistProject = QCheckListProject.checkListProject;
        JPAQuery query = selectFromWhere(chkPrj, qChecklistProject);

        query
            .offset(pages.getOffset())
            .limit(pages.getPageSize());
        query = orderByFromWhere(chkPrj, qChecklistProject, query);

        return query.fetch();
    }
}