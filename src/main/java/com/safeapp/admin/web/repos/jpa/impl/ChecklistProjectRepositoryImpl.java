package com.safeapp.admin.web.repos.jpa.impl;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.response.ResponseChecklistProjectDTO;
import com.binoofactory.cornsqure.web.model.entity.*;
import com.safeapp.admin.web.repos.jpa.custom.ChecklistProjectRepositoryCustom;
import com.safeapp.admin.web.model.entity.ChecklistProject;
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
public class ChecklistProjectRepositoryImpl implements ChecklistProjectRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    final QChecklistProject checklistProject = QChecklistProject.checklistProject;
    final QUsers user = QUsers.users;
    final QProject project = QProject.project;
    final QChecklistProjectDetail checklistProjectDetail = QChecklistProjectDetail.checklistProjectDetail;

    @Transactional(readOnly = true)
    @Override
    public List<ChecklistProject> findAllByEntity(
            ){
        return jpaQueryFactory
                .selectFrom(checklistProject)
                .leftJoin(user).on(user.id.eq(checklistProject.user.id))
                .leftJoin(project).on(project.id.eq(checklistProject.project.id))
                .leftJoin(checklistProject.checklistProjectDetailList , checklistProjectDetail)
                .fetch();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ResponseChecklistProjectDTO> findAllByConditionAndOrderBy(
            Long userId,
            Long projectId,
            String name,
            String tag,
            YN visible,
            YN descendedCreatedDate,
            YN descendedLike,
            YN descendedView,
            String contents,
            Pageable page){
        try{
            List<ResponseChecklistProjectDTO> result = jpaQueryFactory
                    .from(checklistProject)
                    .leftJoin(user).on(user.id.eq(checklistProject.user.id))
                    .leftJoin(project).on(project.id.eq(checklistProject.project.id))
                    .innerJoin(checklistProjectDetail).on(checklistProject.id.eq(checklistProjectDetail.checklistProject.id))
                    .where(
                            isUserIdEq(userId),
                            isProjectIdEq(projectId),
                            isNameLike(name),
                            isTagLike(tag),
                            isContentsLike(contents),
                            isVisible(visible)
                    )
                    .orderBy(
                            isDescendView(descendedView),
                            isDescendDate(descendedCreatedDate),
                            isDescendLike(descendedLike)
                    )
                    .transform(
                            groupBy(checklistProject.id).list(
                                    Projections.fields(
                                            ResponseChecklistProjectDTO.class,
                                            checklistProject.id.as("id"),
                                            checklistProject.name.as("name"),
                                            checklistProject.project.id.as("projectId"),
                                            checklistProject.user.userName.as("userName"),
                                            checklistProject.createdAt.as("createdDate"),
                                            checklistProject.views.as("views"),
                                            checklistProject.likes.as("likeCount"),
                                            GroupBy.list(
                                                    checklistProjectDetail.contents
                                            ).as("content")
                                    )
                            )
                    );

            return result;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            System.out.println(e.getCause());
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> findContentsByProjectId(Long projectId){
        return jpaQueryFactory
                .select(
                        checklistProjectDetail.contents.as("")
                )
                .from(checklistProjectDetail)
                .where(checklistProjectDetail.checklistProject.id.eq(projectId))
                .fetch()
                .stream().collect(Collectors.toList());
    }

    private BooleanExpression isUserIdEq(Long userId) {
        return userId != null ? user.id.eq(userId) : null;
    }

    private BooleanExpression isProjectIdEq(Long projectId) {
        return projectId != null ? project.id.eq(projectId) : null;
    }

    private BooleanExpression isNameLike(String name) {
        return name != null ? checklistProject.name.contains(name) : null;
    }

    private BooleanExpression isTagLike(String tag) {
        return tag != null ? checklistProject.tag.contains(tag) : null;
    }

    private BooleanExpression isVisible(YN visibled) {
        return visibled != null ? checklistProject.visibled.eq(visibled) : null;
    }

    private OrderSpecifier<Integer> isDescendView(YN view) {
        return view == YN.Y ? checklistProject.views.desc() : checklistProject.views.asc();
    }

    private OrderSpecifier<LocalDateTime> isDescendDate(YN date) {
        return date == YN.Y ? checklistProject.createdAt.desc() : checklistProject.createdAt.asc();
    }

    private OrderSpecifier<Integer> isDescendLike(YN like) {
        return like == YN.Y ? checklistProject.likes.desc() : checklistProject.likes.asc();
    }


    private BooleanExpression isContentsLike(String contents) {
        return contents != null ? checklistProject.checklistProjectDetailList.any().contents.contains(contents) : null;
    }
}
