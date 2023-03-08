package com.safeapp.admin.web.repos.jpa.impl;

import com.querydsl.core.QueryResults;
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
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.types.Projections.list;

@AllArgsConstructor
@Slf4j
public class CheckListProjectRepositoryImpl implements CheckListProjectRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    final QProject project = QProject.project;
    final QUsers user = QUsers.users;
    final QCheckListProject checkListProject = QCheckListProject.checkListProject;
    final QCheckListProjectDetail checkListProjectDetail = QCheckListProjectDetail.checkListProjectDetail;

    private BooleanExpression isKeyword(String keyword) {
        return
            (keyword != null) ?
                checkListProject.name.contains(keyword).or(checkListProject.tag.contains(keyword))
                .or(checkListProjectDetail.contents.contains(keyword)).or(checkListProjectDetail.memo.contains(keyword)) : null;
    }
    private BooleanExpression isUserName(String userName) {

        return (userName != null) ? checkListProject.user.userName.contains(userName) : null;
    }
    private BooleanExpression isPhoneNo(String phoneNo) {

        return (phoneNo != null) ? checkListProject.user.phoneNo.contains(phoneNo) : null;
    }
    private BooleanExpression isVisibled(YN visibled) {

        return (visibled != null) ? checkListProject.visibled.eq(visibled) : null;
    }
    private BooleanExpression isCreatedAtStart(LocalDateTime createdAtStart) {

        return (createdAtStart != null) ? checkListProject.createdAt.after(createdAtStart) : null;
    }
    private BooleanExpression isCreatedAtEnd(LocalDateTime createdAtEnd) {

        return (createdAtEnd != null) ? checkListProject.createdAt.before(createdAtEnd) : null;
    }

    private OrderSpecifier<?> orderBy(YN createdAtDesc, YN likesDesc, YN viewsDesc) {
        if(createdAtDesc != null) {
            return (createdAtDesc == YN.Y) ? checkListProject.createdAt.desc() : null;
        } else if(likesDesc != null) {
            return (likesDesc == YN.Y) ? checkListProject.likes.desc() : null;
        } else if(viewsDesc != null) {
            return (viewsDesc == YN.Y) ? checkListProject.views.desc() : null;
        } else {
            return checkListProject.createdAt.desc();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> findContentsByCheckListId(Long checkListId) {
        return
            jpaQueryFactory
            .select(checkListProjectDetail.contents)
            .from(checkListProjectDetail)
            .where
            (
                checkListProjectDetail.checklistProject.id.eq(checkListId),
                checkListProjectDetail.depth.eq(3),
                checkListProjectDetail.checklistProject.deleteYn.isFalse()
            )
            .limit(3)
            .fetch()
            .stream().collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Long countAllByCondition(String keyword, String userName, String phoneNo, YN visibled,
            LocalDateTime createdAtStart, LocalDateTime createdAtEnd) {

        try {
            QueryResults<CheckListProject> result =
                jpaQueryFactory
                .selectFrom(checkListProject)
                .leftJoin(user).on(user.id.eq(checkListProject.user.id))
                .leftJoin(project).on(project.id.eq(checkListProject.project.id))
                .innerJoin(checkListProjectDetail).on(checkListProjectDetail.checklistProject.id.eq(checkListProject.id))
                .where
                (
                    isKeyword(keyword),
                    isUserName(userName),
                    isPhoneNo(phoneNo),
                    isVisibled(visibled),
                    isCreatedAtStart(createdAtStart),
                    isCreatedAtEnd(createdAtEnd),
                    checkListProject.deleteYn.isFalse()
                )
                .groupBy(checkListProject.id)
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
    public List<CheckListProject> findAllByConditionAndOrderBy(String keyword, String userName, String phoneNo, YN visibled,
            LocalDateTime createdAtStart, LocalDateTime createdAtEnd, YN createdAtDesc, YN likesDesc, YN viewsDesc,
            int pageNo, int pageSize) {

        try {
            List<CheckListProject> list =
                jpaQueryFactory
                .selectFrom(checkListProject)
                .leftJoin(user).on(user.id.eq(checkListProject.user.id))
                .leftJoin(project).on(project.id.eq(checkListProject.project.id))
                .innerJoin(checkListProjectDetail).on(checkListProjectDetail.checklistProject.id.eq(checkListProject.id))
                .where
                (
                    isKeyword(keyword),
                    isUserName(userName),
                    isPhoneNo(phoneNo),
                    isVisibled(visibled),
                    isCreatedAtStart(createdAtStart),
                    isCreatedAtEnd(createdAtEnd),
                    checkListProject.deleteYn.isFalse()
                )
                .orderBy(orderBy(createdAtDesc, likesDesc, viewsDesc))
                .groupBy(checkListProject.id)
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