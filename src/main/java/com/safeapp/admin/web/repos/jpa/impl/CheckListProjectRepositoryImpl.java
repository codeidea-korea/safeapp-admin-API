package com.safeapp.admin.web.repos.jpa.impl;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.response.ResponseCheckListProjectDTO;
import com.safeapp.admin.web.model.entity.*;
import com.safeapp.admin.web.repos.jpa.custom.CheckListProjectRepositoryCustom;
import com.safeapp.admin.web.model.entity.CheckListProject;
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
import java.util.stream.Collectors;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.types.Projections.list;

@RequiredArgsConstructor
public class CheckListProjectRepositoryImpl implements CheckListProjectRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    final QChecklistProject checkListProject = QChecklistProject.checklistProject;
    final QUsers user = QUsers.users;
    final QProject project = QProject.project;
    final QChecklistProjectDetail checklistProjectDetail = QChecklistProjectDetail.checklistProjectDetail;

    @Transactional(readOnly = true)
    @Override
    public List<ResponseCheckListProjectDTO> findAllByConditionAndOrderBy(String tag, YN visible, YN descendedCreatedDate,
            YN descendedLike, YN descendedView, Pageable pageable) {

        try {
            List<ResponseCheckListProjectDTO> result =
                    jpaQueryFactory
                    .from(checkListProject)
                    .leftJoin(user).on(user.id.eq(checkListProject.user.id))
                    .leftJoin(project).on(project.id.eq(checkListProject.project.id))
                    .innerJoin(checklistProjectDetail).on(checkListProject.id.eq(checklistProjectDetail.checklistProject.id))
                    .where(
                        isTagLike(tag),
                        isVisible(visible)
                    )
                    .orderBy(
                        isDescendView(descendedView),
                        isDescendDate(descendedCreatedDate),
                        isDescendLike(descendedLike)
                    )
                    .transform(
                        groupBy(checkListProject.id).list(
                            Projections.fields(
                                ResponseCheckListProjectDTO.class,
                                checkListProject.id.as("id"),
                                checkListProject.name.as("name"),
                                checkListProject.project.id.as("projectId"),
                                checkListProject.user.userName.as("userName"),
                                checkListProject.createdAt.as("createdDate"),
                                checkListProject.views.as("views"),
                                checkListProject.likes.as("likeCount"),
                                GroupBy.list(checklistProjectDetail.contents).as("content")
                            )
                        )
                    );

            return result;

        } catch (Exception e) {
            System.out.println(e.getCause());
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());

            throw e;
        }
    }

    private BooleanExpression isTagLike(String tag) {
        return tag != null ? checkListProject.tag.contains(tag) : null;
    }

    private BooleanExpression isVisible(YN visibled) {
        return visibled != null ? checkListProject.visibled.eq(visibled) : null;
    }

    private OrderSpecifier<LocalDateTime> isDescendDate(YN date) {
        return date == YN.Y ? checkListProject.createdAt.desc() : checkListProject.createdAt.asc();
    }

    private OrderSpecifier<Integer> isDescendLike(YN like) {
        return like == YN.Y ? checkListProject.likes.desc() : checkListProject.likes.asc();
    }

    private OrderSpecifier<Integer> isDescendView(YN view) {
        return view == YN.Y ? checkListProject.views.desc() : checkListProject.views.asc();
    }

}