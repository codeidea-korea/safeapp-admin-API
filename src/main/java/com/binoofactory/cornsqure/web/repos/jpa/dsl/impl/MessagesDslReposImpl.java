package com.binoofactory.cornsqure.web.repos.jpa.dsl.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.binoofactory.cornsqure.web.model.cmmn.BfPage;
import com.binoofactory.cornsqure.web.model.entity.Messages;
import com.binoofactory.cornsqure.web.model.entity.QMessages;
import com.binoofactory.cornsqure.web.repos.jpa.dsl.MessagesDslRepos;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import io.netty.util.internal.StringUtil;

@Repository
public class MessagesDslReposImpl extends QuerydslRepositorySupport implements MessagesDslRepos {

    @PersistenceContext(name = "entityManager")
    private EntityManager entityManager;

    @Autowired
    public MessagesDslReposImpl() {
        super(Messages.class);
    }

    private JPAQuery selectFromWhere(Messages instance, QMessages qMessages) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery query = jpaQueryFactory.selectFrom(qMessages);

        if (!StringUtil.isNullOrEmpty(instance.getTitle())) {
            query.where(qMessages.title.like(instance.getTitle()));
        }
        if (!StringUtil.isNullOrEmpty(instance.getContents())) {
            query.where(qMessages.contents.contains(instance.getContents()));
        }
        if (instance.getUser().getId() > 0) {
            query.where(qMessages.user.id.eq(instance.getUser().getId()));
        }
        if (instance.getDeleted() != null) {
            query.where(qMessages.deleted.eq(instance.getDeleted()));
        }
        if (instance.getId() > 0) {
            query.where(qMessages.id.eq(instance.getId()));
        }
        return query;
    }

    @Override
    public List<Messages> findAll(Messages instance, BfPage bfPage) {
        QMessages qMessages = QMessages.messages;
        JPAQuery query = selectFromWhere(instance, qMessages);

        query
            .offset(bfPage.getOffset())
            .limit(bfPage.getPageSize())
            .orderBy(new OrderSpecifier(com.querydsl.core.types.Order.DESC,
                new PathBuilder(QMessages.class, qMessages.id.getMetadata())));

        return query.fetch();
    }

    @Override
    public long countAll(Messages instance) {
        QMessages qFile = QMessages.messages;
        JPAQuery query = selectFromWhere(instance, qFile);
        return query.fetchCount();
    }
}