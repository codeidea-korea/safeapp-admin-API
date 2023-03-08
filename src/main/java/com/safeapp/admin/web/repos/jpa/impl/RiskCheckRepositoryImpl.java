package com.safeapp.admin.web.repos.jpa.impl;

import com.safeapp.admin.web.model.entity.*;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.safeapp.admin.web.repos.jpa.custom.RiskCheckRepositoryCustom;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.querydsl.core.group.GroupBy.groupBy;

@AllArgsConstructor
public class RiskCheckRepositoryImpl implements RiskCheckRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    final QRiskCheckDetail riskCheckDetail = QRiskCheckDetail.riskCheckDetail;

    @Override
    @Transactional(readOnly = true)
    public List<String> findContentsByRiskCheckId(long riskCheckId) {
        return
            jpaQueryFactory
            .select(riskCheckDetail.relateGuide)
            .from(riskCheckDetail)
            .where(riskCheckDetail.riskCheck.id.eq(riskCheckId))
            .limit(3)
            .fetch()
            .stream().collect(Collectors.toList());
    }

}