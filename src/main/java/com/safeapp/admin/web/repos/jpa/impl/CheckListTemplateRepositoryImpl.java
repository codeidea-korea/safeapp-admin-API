package com.safeapp.admin.web.repos.jpa.impl;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.response.ResponseCheckListTemplateDTO;
import com.safeapp.admin.web.model.entity.QCheckListTemplate;
import com.safeapp.admin.web.model.entity.QCheckListTemplateDetail;
import com.safeapp.admin.web.model.entity.QProject;
import com.safeapp.admin.web.model.entity.QUsers;
import com.safeapp.admin.web.repos.jpa.custom.CheckListTemplateRepositoryCustom;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

import static com.querydsl.core.group.GroupBy.groupBy;

@AllArgsConstructor
public class CheckListTemplateRepositoryImpl implements CheckListTemplateRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    final QProject project = QProject.project;
    final QUsers user = QUsers.users;
    final QCheckListTemplate checkListTemplate = QCheckListTemplate.checkListTemplate;
    final QCheckListTemplateDetail checkListTemplateDetail = QCheckListTemplateDetail.checkListTemplateDetail;

    @Transactional(readOnly = true)
    @Override
    public List<ResponseCheckListTemplateDTO> findAllByCondition(Long userId, Long projectId, String name, String tag,
            YN created_at_descended, YN views_descended, YN likes_descended, String detail_contents, Pageable page, HttpServletRequest request) {

        try {
            List<ResponseCheckListTemplateDTO> result =
                jpaQueryFactory
                .from(checkListTemplate)
                .leftJoin(user).on(user.id.eq(checkListTemplate.user.id))
                .leftJoin(project).on(project.id.eq(checkListTemplate.project.id))
                .innerJoin(checkListTemplateDetail).on(checkListTemplate.id.eq(checkListTemplateDetail.checkListTemplate.id))
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
                    groupBy(checkListTemplate.id).list(
                        Projections.fields(
                            ResponseCheckListTemplateDTO.class,
                            checkListTemplate.id.as("id"),
                            checkListTemplate.name.as("title"),
                            checkListTemplate.user.userName.as("userName"),
                            checkListTemplate.createdAt.as("createdDate"),
                            checkListTemplate.views.as("views"),
                            checkListTemplate.likes.as("likeCount"),
                            GroupBy.list(checkListTemplate.project.contents).as("content")
                        )
                    )
                );

            return result;

        } catch(Exception e) {
            System.out.println(e.getCause());
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());

            throw e;
        }
    }

    private BooleanExpression isUserIdEq(Long userId) {
        return userId != null ? user.id.eq(userId) : null;
    }

    private BooleanExpression isProjectIdEq(Long projectId) {
        return projectId != null ? project.id.eq(projectId) : null;
    }

    private BooleanExpression isNameLike(String name) {
        return name != null ? checkListTemplate.name.contains(name) : null;
    }

    private BooleanExpression isTagLike(String tag) {
        return tag != null ? checkListTemplate.tag.contains(tag) : null;
    }

    private OrderSpecifier<Integer> isDescendView(YN view) {
        return view == YN.Y ? checkListTemplate.views.desc() : checkListTemplate.views.asc();
    }

    private OrderSpecifier<LocalDateTime> isDescendDate(YN date) {
        return date == YN.Y ? checkListTemplate.createdAt.desc() : checkListTemplate.createdAt.asc();
    }

    private OrderSpecifier<Integer> isDescendLike(YN like) {
        return like == YN.Y ? checkListTemplate.likes.desc() : checkListTemplate.likes.asc();
    }

    private BooleanExpression isContentsLike(String contents) {
        return contents != null ? checkListTemplate.details.any().contents.contains(contents) : null;
    }

}