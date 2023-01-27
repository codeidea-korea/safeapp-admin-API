package com.safeapp.admin.web.repos.jpa.impl;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.response.ResponseCheckListProjectDTO;
import com.safeapp.admin.web.dto.response.ResponseProjectGroupDTO;
import com.safeapp.admin.web.model.entity.*;
import com.safeapp.admin.web.repos.jpa.custom.CheckListProjectRepositoryCustom;
import com.safeapp.admin.web.model.entity.CheckListProject;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.safeapp.admin.web.repos.jpa.custom.ProjectReposCustom;
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
public class ProjectRepositoryImpl implements ProjectReposCustom {

    private final JPAQueryFactory jpaQueryFactory;

    final QProjectGroup projectGroup = QProjectGroup.projectGroup;
    final QUsers user = QUsers.users;

    @Transactional(readOnly = true)
    @Override
    public List<ResponseProjectGroupDTO> findAllGroupByCondition(long id, Pageable pageable, HttpServletRequest request) {
        try {
            List<ResponseProjectGroupDTO> result =
                jpaQueryFactory
                .from(projectGroup)
                .leftJoin(user).on(projectGroup.user.id.eq(user.id))
                .where(
                    projectGroup.project.id.eq(id),
                    user.deleted.eq(YN.Y)
                )
                .orderBy(user.userName.asc())
                .transform(
                    groupBy(projectGroup.id).list(Projections.fields(ResponseProjectGroupDTO.class, projectGroup.id.as("id")))
                );

            return result;

        } catch (Exception e) {
            e.getStackTrace();
            System.out.println(e.getCause());
            System.out.println(e.getMessage());

            throw e;
        }
    }

}