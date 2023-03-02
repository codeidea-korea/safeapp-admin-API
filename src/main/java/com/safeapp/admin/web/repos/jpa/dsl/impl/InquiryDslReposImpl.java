package com.safeapp.admin.web.repos.jpa.dsl.impl;

import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.safeapp.admin.web.model.cmmn.Pages;
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

    private JPAQuery selectFromWhere(Inquiry inquiry, QInquiry qInquiry) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery query = jpaQueryFactory.selectFrom(qInquiry);

        if(!Objects.isNull(inquiry.getIsAnswer())) {
            query.where(qInquiry.isAnswer.eq(inquiry.getIsAnswer()));
        }
        if(!Objects.isNull(inquiry.getIsAnswer())) {
            query.where(qInquiry.isAnswer.eq(inquiry.getIsAnswer()));
        }
        query.where(qInquiry.deleteYn.eq(false));

        return query;
    }

    @Override
    public long countAll(Inquiry inquiry) {
        QInquiry qInquiry = QInquiry.inquiry;
        JPAQuery query = selectFromWhere(inquiry, qInquiry);

        return query.fetchCount();
    }

    @Override
    public List<Inquiry> findAll(Inquiry inquiry, Pages pages) {
        QInquiry qInquiry = QInquiry.inquiry;
        JPAQuery query = selectFromWhere(inquiry, qInquiry);

        query
            .offset(pages.getOffset())
            .limit(pages.getPageSize())
            .orderBy(new OrderSpecifier(com.querydsl.core.types.Order.DESC, new PathBuilder(QInquiry.class, qInquiry.id.getMetadata())));

        return query.fetch();
    }

}