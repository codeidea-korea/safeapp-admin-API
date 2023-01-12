package com.binoofactory.cornsqure.web.repos.jpa.impl;

import com.binoofactory.cornsqure.web.data.YN;
import com.binoofactory.cornsqure.web.dto.response.ResponseChecklistProjectDTO;
import com.binoofactory.cornsqure.web.dto.response.ResponseChecklistTemplateDTO;
import com.binoofactory.cornsqure.web.model.entity.QChecklistTemplate;
import com.binoofactory.cornsqure.web.model.entity.QChecklistTemplateDetail;
import com.binoofactory.cornsqure.web.model.entity.QProject;
import com.binoofactory.cornsqure.web.model.entity.QUsers;
import com.binoofactory.cornsqure.web.repos.jpa.custom.ChecklistTemplateRepositoryCustom;
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
public class ChecklistTemplateRepositoryImpl implements ChecklistTemplateRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    final QChecklistTemplate checklistTemplate = QChecklistTemplate.checklistTemplate;
    final QUsers user = QUsers.users;
    final QProject project = QProject.project;
    final QChecklistTemplateDetail checklistTemplateDetail = QChecklistTemplateDetail.checklistTemplateDetail;

    @Transactional(readOnly = true)
    @Override
    public List<ResponseChecklistTemplateDTO> findAllByCondition(
            Long userId,
            Long projectId,
            String name,
            String tag,
            YN created_at_descended,
            YN views_descended,
            YN likes_descended,
            String detail_contents,
            Pageable page
    ){
        return jpaQueryFactory
                .from(checklistTemplate)
                .leftJoin(user).on(user.id.eq(checklistTemplate.user.id))
                .leftJoin(project).on(project.id.eq(checklistTemplate.project.id))
                .innerJoin(checklistTemplateDetail).on(checklistTemplate.id.eq(checklistTemplateDetail.checklistTemplate.id))
                .where(
                        isUserIdEq(userId),
                        isProjectIdEq(projectId),
                        isNameLike(name),
                        isTagLike(tag),
                        isContentsLike(detail_contents)
                )
                .orderBy(
                        isDescendView(views_descended),
                        isDescendDate(created_at_descended),
                        isDescendLike(likes_descended)
                )
                .offset(page.getOffset())
                .limit(page.getPageSize())
                .transform(
                        groupBy(checklistTemplate.id).list(
                                Projections.fields(
                                        ResponseChecklistTemplateDTO.class,
                                        checklistTemplate.id.as("id"),
                                        checklistTemplate.name.as("title"),
                                        checklistTemplate.user.userName.as("userName"),
                                        checklistTemplate.createdAt.as("createdDate"),
                                        checklistTemplate.views.as("views"),
                                        checklistTemplate.likes.as("likeCount"),
                                        GroupBy.list(
                                                checklistTemplateDetail.contents
                                        ).as("content")
                                )
                        )
                );
    }

    private BooleanExpression isUserIdEq(Long userId) {
        return userId != null ? user.id.eq(userId) : null;
    }

    private BooleanExpression isProjectIdEq(Long projectId) {
        return projectId != null ? project.id.eq(projectId) : null;
    }

    private BooleanExpression isNameLike(String name) {
        return name != null ? checklistTemplate.name.contains(name) : null;
    }

    private BooleanExpression isTagLike(String tag) {
        return tag != null ? checklistTemplate.tag.contains(tag) : null;
    }

    private OrderSpecifier<Integer> isDescendView(YN view) {
        return view == Y ? checklistTemplate.views.desc() : checklistTemplate.views.asc();
    }

    private OrderSpecifier<LocalDateTime> isDescendDate(YN date) {
        return date == Y ? checklistTemplate.createdAt.desc() : checklistTemplate.createdAt.asc();
    }

    private OrderSpecifier<Integer> isDescendLike(YN like) {
        return like == Y ? checklistTemplate.likes.desc() : checklistTemplate.likes.asc();
    }


    private BooleanExpression isContentsLike(String contents) {
        return contents != null ? checklistTemplate.details.any().contents.contains(contents) : null;
    }
}
