package com.safeapp.admin.web.repos.jpa.impl;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.response.ResponseProjectGroupDTO;
import com.safeapp.admin.web.model.entity.*;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.safeapp.admin.web.repos.jpa.custom.ProjectGroupReposCustom;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.types.Projections.list;

@AllArgsConstructor
public class ProjectGroupReposImpl implements ProjectGroupReposCustom {

    private final JPAQueryFactory jpaQueryFactory;

    final QProjectGroup projectGroup = QProjectGroup.projectGroup;
    final QUsers user = QUsers.users;

    @Transactional(readOnly = true)
    @Override
    public List<ResponseProjectGroupDTO> findAllById(long id, int pageNo, int pageSize, HttpServletRequest request) {
        try {
            List<ResponseProjectGroupDTO> result =
                jpaQueryFactory
                .select
                (
                    Projections.fields
                    (
                        ResponseProjectGroupDTO.class,
                        user.id.as("id"),
                        user.userName.as("userName"),
                        user.email.as("email"),
                        projectGroup.userAuthType.as("userAuthType")
                    )
                )
                .from(projectGroup)
                .leftJoin(user)
                .on(projectGroup.user.id.eq(user.id))
                .where(projectGroup.project.id.eq(id), projectGroup.deleteYn.eq(false), user.deleted.eq(YN.N))
                .offset((pageNo - 1) * pageSize)
                .limit(pageSize)
                .orderBy(user.userName.asc())
                .fetch();

            return result;

        } catch (Exception e) {
            e.getStackTrace();
            System.out.println(e.getCause());
            System.out.println(e.getMessage());

            throw e;
        }
    }

}