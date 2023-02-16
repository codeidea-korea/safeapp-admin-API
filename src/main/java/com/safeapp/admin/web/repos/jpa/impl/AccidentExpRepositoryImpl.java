package com.safeapp.admin.web.repos.jpa.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.entity.*;
import com.safeapp.admin.web.repos.jpa.custom.AccidentExpRepositoryCustom;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Slf4j
public class AccidentExpRepositoryImpl implements AccidentExpRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    final QAccidentExp accExp = QAccidentExp.accidentExp;
    final QAdmins admin = QAdmins.admins;

    private BooleanExpression isKeyword(String keyword) {

        return (keyword != null) ? accExp.name.contains(keyword).or(accExp.tags.contains(keyword)) : null;
    }
    private BooleanExpression isAdminName(String adminName) {

        return (adminName != null) ? accExp.admin.adminName.contains(adminName) : null;
    }
    private BooleanExpression isPhoneNo(String phoneNo) {

        return (phoneNo != null) ? accExp.admin.phoneNo.contains(phoneNo) : null;
    }
    private BooleanExpression isCreatedAtStart(LocalDateTime createdAtStart) {

        return (createdAtStart != null) ? accExp.createdAt.after(createdAtStart) : null;
    }
    private BooleanExpression isCreatedAtEnd(LocalDateTime createdAtEnd) {

        return (createdAtEnd != null) ? accExp.createdAt.before(createdAtEnd) : null;
    }

    private OrderSpecifier<?> orderBy(YN createdAtDesc, YN viewsDesc) {
        if(createdAtDesc != null) {
            return (createdAtDesc == YN.Y) ? accExp.createdAt.desc() : null;
        } else if(viewsDesc != null) {
            return (viewsDesc == YN.Y) ? accExp.views.desc() : null;
        } else {
            return accExp.createdAt.desc();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Long countAllByCondition(String keyword, String adminName, String phoneNo,
            LocalDateTime createdAtStart, LocalDateTime createdAtEnd) {

        try {
            QueryResults<AccidentExp> count =
                jpaQueryFactory
                .selectFrom(accExp)
                .leftJoin(admin).on(accExp.admin.id.eq(admin.id))
                .where
                (
                    isKeyword(keyword),
                    isAdminName(adminName),
                    isPhoneNo(phoneNo),
                    isCreatedAtStart(createdAtStart),
                    isCreatedAtEnd(createdAtEnd),
                    accExp.deleteYn.isFalse()
                )
                .groupBy(accExp.id)
                .fetchResults();

            return count.getTotal();

        } catch(Exception e) {
            e.getStackTrace();
            System.out.println(e.getCause());
            System.out.println(e.getMessage());

            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccidentExp> findAllByConditionAndOrderBy(String keyword, String adminName, String phoneNo,
        LocalDateTime createdAtStart, LocalDateTime createdAtEnd, YN createdAtDesc, YN viewsDesc,
        int pageNo, int pageSize) {

        try {
            List<AccidentExp> list =
                jpaQueryFactory
                .selectFrom(accExp)
                .leftJoin(admin).on(accExp.admin.id.eq(admin.id))
                .where
                (
                    isKeyword(keyword),
                    isAdminName(adminName),
                    isPhoneNo(phoneNo),
                    isCreatedAtStart(createdAtStart),
                    isCreatedAtEnd(createdAtEnd),
                    accExp.deleteYn.isFalse()
                )
                .orderBy(orderBy(createdAtDesc, viewsDesc))
                .groupBy(accExp.id)
                .limit(pageSize)
                .offset((pageNo - 1) * pageSize)
                .fetch();

            return list;

        } catch (Exception e) {
            e.getStackTrace();
            System.out.println(e.getCause());
            System.out.println(e.getMessage());

            throw e;
        }
    }

}