package com.safeapp.admin.web.repos.jpa.dsl.impl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.*;
import com.safeapp.admin.web.repos.jpa.dsl.AdminsDslRepos;
import com.safeapp.admin.web.repos.jpa.dsl.RiskChkDslRepos;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Repository
@Slf4j
public class RiskChkDslReposImpl extends QuerydslRepositorySupport implements RiskChkDslRepos {

    @PersistenceContext(name = "entityManager")
    private EntityManager entityManager;

    @Autowired
    public RiskChkDslReposImpl() {
        super(RiskCheck.class);
    }

    private JPAQuery selectFromWhere(RiskCheck riskChk, QRiskCheck qRiskChk) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery query = jpaQueryFactory.selectFrom(qRiskChk);

        if(!StringUtil.isNullOrEmpty(riskChk.getKeyword())) {
            query.where(qRiskChk.name.contains(riskChk.getKeyword()).or(qRiskChk.tag.contains(riskChk.getKeyword())));
        }
        if(!StringUtil.isNullOrEmpty(riskChk.getUserName())) {
            query.where(qRiskChk.user.userName.contains(riskChk.getUserName()));
        }
        if(!StringUtil.isNullOrEmpty(riskChk.getPhoneNo())) {
            query.where(qRiskChk.user.phoneNo.contains(riskChk.getPhoneNo()));
        }
        if(!Objects.isNull(riskChk.getVisibled())) {
            query.where(qRiskChk.visibled.eq(riskChk.getVisibled()));
        }
        if(!StringUtil.isNullOrEmpty(riskChk.getCreatedAtStart())) {
            query.where(qRiskChk.createdAt.after(LocalDateTime.parse(riskChk.getCreatedAtStart(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))));
        } else {
            query.where(qRiskChk.createdAt.after(LocalDateTime.parse("1000-01-01 00:00:00.000", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))));
        }
        if(!StringUtil.isNullOrEmpty(riskChk.getCreatedAtEnd())) {
            query.where(qRiskChk.createdAt.before(LocalDateTime.parse(riskChk.getCreatedAtEnd(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")).plusDays(1)));
        } else {
            query.where(qRiskChk.createdAt.before(LocalDateTime.parse("9999-12-31 23:59:59.999", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))));
        }
        query.where(qRiskChk.deleteYn.eq(false));

        return query;
    }

    private JPAQuery orderBy(RiskCheck riskChk, QRiskCheck qRiskChk, JPAQuery query) {
        if(riskChk.getCreatedAtDesc() == null && riskChk.getViewsDesc() == null) {
            query.orderBy
            (
                new OrderSpecifier(com.querydsl.core.types.Order.DESC,
                new PathBuilder(QRiskCheck.class, qRiskChk.id.getMetadata()))
            );
        } else {
            if(riskChk.getCreatedAtDesc() != null) {
                query.orderBy
                (
                    new OrderSpecifier((riskChk.getCreatedAtDesc() == YN.Y ? com.querydsl.core.types.Order.DESC : com.querydsl.core.types.Order.ASC),
                    new PathBuilder(QAccidentExp.class, qRiskChk.createdAt.getMetadata()))
                );
            }
            if(riskChk.getViewsDesc() != null) {
                query.orderBy
                (
                    new OrderSpecifier((riskChk.getViewsDesc() == YN.Y ? com.querydsl.core.types.Order.DESC : com.querydsl.core.types.Order.ASC),
                    new PathBuilder(QAccidentExp.class, qRiskChk.views.getMetadata()))
                );
            }
        }

        return query;
    }

    @Override
    public long countAll(RiskCheck riskChk) {
        QRiskCheck qRiskChk = QRiskCheck.riskCheck;
        JPAQuery query = selectFromWhere(riskChk, qRiskChk);

        return query.fetchCount();
    }

    @Override
    public List<RiskCheck> findAll(RiskCheck riskChk, Pages pages) {
        QRiskCheck qRiskChk = QRiskCheck.riskCheck;
        JPAQuery query = selectFromWhere(riskChk, qRiskChk);

        query
            .offset(pages.getOffset())
            .limit(pages.getPageSize());
        query = orderBy(riskChk, qRiskChk, query);

        return query.fetch();
    }

}