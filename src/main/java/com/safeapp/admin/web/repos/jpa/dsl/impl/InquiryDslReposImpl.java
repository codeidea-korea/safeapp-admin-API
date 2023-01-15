package com.safeapp.admin.web.repos.jpa.dsl.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.safeapp.admin.web.model.cmmn.BfPage;
import com.safeapp.admin.web.model.entity.Inquiry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.safeapp.admin.web.model.entity.QInquiry;
import com.safeapp.admin.web.repos.jpa.dsl.InquiryDslRepos;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import io.netty.util.internal.StringUtil;

@Repository
public class InquiryDslReposImpl extends QuerydslRepositorySupport implements InquiryDslRepos {

    @PersistenceContext(name = "entityManager")
    private EntityManager entityManager;

    @Autowired
    public InquiryDslReposImpl() {
        super(Inquiry.class);
    }

    private JPAQuery selectFromWhere(Inquiry instance, QInquiry qInquiry) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery query = jpaQueryFactory.selectFrom(qInquiry);

        if (!StringUtil.isNullOrEmpty(instance.getTitle())) {
            query.where(qInquiry.title.like(instance.getTitle()));
        }
        if (!StringUtil.isNullOrEmpty(instance.getServiceName())) {
            query.where(qInquiry.serviceName.like(instance.getServiceName()));
        }
        if (!StringUtil.isNullOrEmpty(instance.getContents())) {
            query.where(qInquiry.contents.contains(instance.getContents()));
        }
        if (instance.getIsAnswer() != null) {
            query.where(qInquiry.isAnswer.eq(instance.getIsAnswer()));
        }
        if (instance.getId() > 0) {
            query.where(qInquiry.id.eq(instance.getId()));
        }
        return query;
    }

    @Override
    public List<Inquiry> findAll(Inquiry instance, BfPage bfPage) {
        QInquiry qInquiry = QInquiry.inquiry;
        JPAQuery query = selectFromWhere(instance, qInquiry);

        query
            .offset(bfPage.getOffset())
            .limit(bfPage.getPageSize())
            .orderBy(new OrderSpecifier(com.querydsl.core.types.Order.DESC,
                new PathBuilder(QInquiry.class, qInquiry.id.getMetadata())));

        return query.fetch();
    }

    @Override
    public long countAll(Inquiry instance) {
        QInquiry qFile = QInquiry.inquiry;
        JPAQuery query = selectFromWhere(instance, qFile);
        return query.fetchCount();
    }
}