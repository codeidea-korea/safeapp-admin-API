package com.safeapp.admin.web.repos.jpa.impl;

import com.querydsl.core.QueryResults;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.response.ResponseRiskCheckDTO;
import com.safeapp.admin.web.model.entity.*;
import com.safeapp.admin.web.repos.jpa.custom.RIskCheckRepositoryCustom;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.querydsl.core.group.GroupBy.groupBy;

@AllArgsConstructor
public class RiskCheckRepositoryImpl implements RIskCheckRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    final QRiskCheck riskCheck = QRiskCheck.riskCheck;

    final QUsers user = QUsers.users;
    final QProject project = QProject.project;
    final QRiskCheckDetail riskCheckDetail = QRiskCheckDetail.riskCheckDetail;

    private BooleanExpression isKeyword(String keyword) {
        return
            (keyword != null) ?
                riskCheck.name.contains(keyword).or(riskCheck.tag.contains(keyword))
                .or(riskCheckDetail.contents.contains(keyword)).or(riskCheckDetail.checkMemo.contains(keyword)) : null;
    }
    private BooleanExpression isUserName(String userName) {

        return (userName != null) ? riskCheck.user.userName.contains(userName) : null;
    }
    private BooleanExpression isPhoneNo(String phoneNo) {

        return (phoneNo != null) ? riskCheck.user.phoneNo.contains(phoneNo) : null;
    }
    private BooleanExpression isVisibled(YN visibled) {

        return (visibled != null) ? riskCheck.visibled.eq(visibled) : null;
    }
    private BooleanExpression isCreatedAtStart(LocalDateTime createdAtStart) {

        return (createdAtStart != null) ? riskCheck.createdAt.after(createdAtStart) : null;
    }
    private BooleanExpression isCreatedAtEnd(LocalDateTime createdAtEnd) {

        return (createdAtEnd != null) ? riskCheck.createdAt.before(createdAtEnd) : null;
    }

    private OrderSpecifier<?> orderBy(YN createdAtDesc, YN likesDesc, YN viewsDesc) {
        if(createdAtDesc != null) {
            return (createdAtDesc == YN.Y) ? riskCheck.createdAt.desc() : null;
        } else if(likesDesc != null) {
            return (likesDesc == YN.Y) ? riskCheck.likes.desc() : null;
        } else if(viewsDesc != null) {
            return (viewsDesc == YN.Y) ? riskCheck.views.desc() : null;
        } else {
            return riskCheck.createdAt.desc();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> findContentsByRiskChecktId(Long riskCheckId) {
        return
            jpaQueryFactory
            .select(riskCheckDetail.relateGuide)
            .from(riskCheckDetail)
            .where(riskCheckDetail.riskCheck.id.eq(riskCheckId))
            .limit(3)
            .fetch()
            .stream().collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Long countAllByCondition(String keyword, String userName, String phoneNo, YN visibled,
            LocalDateTime createdAtStart, LocalDateTime createdAtEnd) {

        try {
            QueryResults<RiskCheck> result =
                jpaQueryFactory
                .selectFrom(riskCheck)
                .leftJoin(user).on(user.id.eq(riskCheck.user.id))
                .leftJoin(project).on(project.id.eq(riskCheck.project.id))
                .innerJoin(riskCheckDetail).on(riskCheckDetail.riskCheck.id.eq(riskCheck.id))
                .where
                (
                    isKeyword(keyword),
                    isUserName(userName),
                    isPhoneNo(phoneNo),
                    isVisibled(visibled),
                    isCreatedAtStart(createdAtStart),
                    isCreatedAtEnd(createdAtEnd),
                    riskCheck.deleteYn.isFalse()
                )
                .groupBy(riskCheck.id)
                .fetchResults();

            return result.getTotal();

        } catch(Exception e) {
            e.getStackTrace();
            System.out.println(e.getCause());
            System.out.println(e.getMessage());

            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<RiskCheck> findAllByConditionAndOrderBy(String keyword, String userName, String phoneNo, YN visibled,
           LocalDateTime createdAtStart, LocalDateTime createdAtEnd, YN createdAtDesc, YN likesDesc, YN viewsDesc,
           int pageNo, int pageSize) {

        try {
            List<RiskCheck> list =
                jpaQueryFactory
                .selectFrom(riskCheck)
                .leftJoin(user).on(user.id.eq(riskCheck.user.id))
                .leftJoin(project).on(project.id.eq(riskCheck.project.id))
                .innerJoin(riskCheckDetail).on(riskCheckDetail.riskCheck.id.eq(riskCheck.id))
                .where
                (
                    isKeyword(keyword),
                    isUserName(userName),
                    isPhoneNo(phoneNo),
                    isVisibled(visibled),
                    isCreatedAtStart(createdAtStart),
                    isCreatedAtEnd(createdAtEnd),
                    riskCheck.deleteYn.isFalse()
                )
                .orderBy(orderBy(createdAtDesc, likesDesc, viewsDesc))
                .groupBy(riskCheck.id)
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