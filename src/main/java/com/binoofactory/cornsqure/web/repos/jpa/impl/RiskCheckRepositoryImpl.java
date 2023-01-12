package com.binoofactory.cornsqure.web.repos.jpa.impl;

import com.binoofactory.cornsqure.web.data.YN;
import com.binoofactory.cornsqure.web.dto.response.ResponseChecklistProjectDTO;
import com.binoofactory.cornsqure.web.dto.response.ResponseRiskcheckDTO;
import com.binoofactory.cornsqure.web.model.entity.QProject;
import com.binoofactory.cornsqure.web.model.entity.QRiskCheck;
import com.binoofactory.cornsqure.web.model.entity.QRiskCheckDetail;
import com.binoofactory.cornsqure.web.model.entity.QUsers;
import com.binoofactory.cornsqure.web.repos.jpa.custom.RIskCheckRepositoryCustom;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.binoofactory.cornsqure.web.data.YN.Y;
import static com.querydsl.core.group.GroupBy.groupBy;

@RequiredArgsConstructor
public class RiskCheckRepositoryImpl implements RIskCheckRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    final QRiskCheck riskCheck = QRiskCheck.riskCheck;

    final QUsers user = QUsers.users;
    final QProject project = QProject.project;
    final QRiskCheckDetail riskCheckDetail = QRiskCheckDetail.riskCheckDetail;

    @Transactional(readOnly = true)
    @Override
    public List<ResponseRiskcheckDTO> findAllByCondition(
            Long userId,
            Long projectId,
            String name,
            String tag,
            YN visibled,
            String status,
            YN created_at_descended,
            YN views_descended,
            YN likes_descended,
            String detail_contents,
            Pageable page
    ){
        List<ResponseRiskcheckDTO> result = jpaQueryFactory
                .from(riskCheck)
                .leftJoin(user).on(user.id.eq(riskCheck.user.id))
                .leftJoin(project).on(project.id.eq(riskCheck.project.id))
                .innerJoin(riskCheckDetail).on(riskCheck.id.eq(riskCheckDetail.riskCheck.id))
                .where(
                        isUserIdEq(userId),
                        isProjectIdEq(projectId),
                        isNameLike(name),
                        isTagLike(tag),
                        isContentsLike(detail_contents),
                        isVisible(visibled)
                )
                .orderBy(
                        isDescendView(views_descended),
                        isDescendDate(created_at_descended),
                        isDescendLike(likes_descended)
                )
                .offset(page.getOffset())
                .limit(page.getPageSize())
                .transform(
                        groupBy(riskCheck.id).list(
                                Projections.fields(
                                        ResponseRiskcheckDTO.class,
                                        riskCheck.id.as("id"),
                                        riskCheck.name.as("name"),
                                        riskCheck.project.id.as("projectId"),
                                        riskCheck.user.userName.as("userName"),
                                        riskCheck.createdAt.as("createdDate"),
                                        riskCheck.views.as("views"),
                                        riskCheck.likes.as("likeCount"),
                                        GroupBy.list(
                                                riskCheckDetail.contents
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
        return name != null ? riskCheck.name.contains(name) : null;
    }

    private BooleanExpression isTagLike(String tag) {
        return tag != null ? riskCheck.tag.contains(tag) : null;
    }

    private BooleanExpression isVisible(YN visibled) {
        return visibled != null ? riskCheck.visibled.eq(visibled) : null;
    }

    private OrderSpecifier<Integer> isDescendView(YN view) {
        return view == Y ? riskCheck.views.desc() : riskCheck.views.asc();
    }

    private OrderSpecifier<LocalDateTime> isDescendDate(YN date) {
        return date == Y ? riskCheck.createdAt.desc() : riskCheck.createdAt.asc();
    }

    private OrderSpecifier<Integer> isDescendLike(YN like) {
        return like == Y ? riskCheck.likes.desc() : riskCheck.likes.asc();
    }


    private BooleanExpression isContentsLike(String contents) {
        return contents != null ? riskCheck.riskCheckDetailList.any().contents.contains(contents) : null;
    }
}
