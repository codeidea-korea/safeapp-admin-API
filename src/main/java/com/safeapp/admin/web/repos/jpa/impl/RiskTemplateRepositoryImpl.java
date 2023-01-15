package com.safeapp.admin.web.repos.jpa.impl;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.response.ResponseRiskTemplateDTO;
import com.safeapp.admin.web.model.entity.*;
import com.safeapp.admin.web.repos.jpa.custom.RIskTemplateRepositoryCustom;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;


import java.util.List;

import static com.querydsl.core.group.GroupBy.groupBy;

@RequiredArgsConstructor
public class RiskTemplateRepositoryImpl implements RIskTemplateRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    final QRiskTemplate template = QRiskTemplate.riskTemplate;

    final QUsers user = QUsers.users;
    final QProject project = QProject.project;
    final QRiskTemplateDetail detail = QRiskTemplateDetail.riskTemplateDetail;

    @Transactional(readOnly = true)
    @Override
    public List<ResponseRiskTemplateDTO> findAllByCondition(
            Long checkerId,
            Long projectId,
            Long userId,
            String name,
            YN visibled,
            String tags,
            Pageable page
    ){
        List<ResponseRiskTemplateDTO> result = jpaQueryFactory
                .from(template)
                .leftJoin(user).on(user.id.eq(template.user.id))
                .leftJoin(project).on(project.id.eq(template.project.id))
                .innerJoin(detail).on(template.id.eq(detail.riskTemplate.id))
                .where(
                        isUserIdEq(userId),
                        isProjectIdEq(projectId),
                        isNameLike(name),
                        isTagLike(tags),
                        isVisible(visibled)
                )
                .offset(page.getOffset())
                .limit(page.getPageSize())
                .transform(
                        groupBy(template.id).list(
                                Projections.fields(
                                        ResponseRiskTemplateDTO.class,
                                        template.id.as("id"),
                                        template.name.as("title"),
                                        template.user.userName.as("userName"),
                                        template.createdAt.as("createdDate"),
                                        template.views.as("views"),
                                        template.likes.as("likeCount"),
                                        GroupBy.list(
                                                detail.contents
                                        ).as("content")
                                )
                        )
                );
        return result;
    }

    private BooleanExpression isUserIdEq(Long userId) {
        return userId != null ? user.id.eq(userId) : null;
    }

    private BooleanExpression isProjectIdEq(Long projectId) {
        return projectId != null ? project.id.eq(projectId) : null;
    }

    private BooleanExpression isNameLike(String name) {
        return name != null ? template.name.contains(name) : null;
    }

    private BooleanExpression isTagLike(String tag) {
        return tag != null ? template.tag.contains(tag) : null;
    }

    private BooleanExpression isVisible(YN visibled) {
        return visibled != null ? template.visibled.eq(visibled) : null;
    }

    private OrderSpecifier<Integer> isDescendView(YN view) {
        return view == YN.Y ? template.views.desc() : template.views.asc();
    }
}
