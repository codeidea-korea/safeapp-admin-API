package com.safeapp.admin.web.repos.direct;

import java.util.List;
import java.util.Map;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.cmmn.Pages;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DirectQuery {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DirectQuery(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> findAllUnionChecklistAndRisk(String title, String type, YN createdAtDescended,
        YN nameDescended, YN userIdDescended, Long projectId, Pages bfPage) {

        try {
            String whereOption = "";
            if (StringUtils.isNotEmpty(title)) {
                whereOption = whereOption + " and rf.name like '%" + title + "%' ";
            }
            if (StringUtils.isNotEmpty(type)) {
                whereOption = whereOption + " and rf.type = '" + type + "' ";
            }
            if (projectId != null) {
                whereOption = whereOption + " and rf.project = " + projectId + " ";
            }

            String orderOption = "";
            if (createdAtDescended != null) {
                orderOption = orderOption + (!"".equals(orderOption) ? ", " : "") + " rf.created_at "
                    + (createdAtDescended == YN.Y ? "DESC" : "ASC");
            }
            if (nameDescended != null) {
                orderOption = orderOption + (!"".equals(orderOption) ? ", " : "") + " rf.name "
                    + (nameDescended == YN.Y ? "DESC" : "ASC");
            }
            if (userIdDescended != null) {
                orderOption = orderOption + (!"".equals(orderOption) ? ", " : "") + " rf.user_email_id "
                    + (userIdDescended == YN.Y ? "DESC" : "ASC");
            }
            if ("".equals(orderOption)) {
                orderOption = " order by rf.created_at DESC ";
            } else {
                orderOption = " order by " + orderOption;
            }

            List<Map<String, Object>> result = jdbcTemplate.queryForList(
                "SELECT "
                    + " rf.id, rf.name, rf.created_at, rf.type, rf.user_id, rf.user_email_id, rf.user_name, rf.project from "
                    +
                    "( " +
                    "     select p.id, p.project, p.name, p.created_at, u1.id as user_id, u1.user_id as user_email_id, u1.user_name, 'checklist' as type from checklist_projects p join users u1 on p.user = u1.id "
                    +
                    "union all " +
                    "     select r.id, r.project, r.name, r.created_at, u2.id as user_id, u2.user_id as user_email_id, u2.user_name, 'risk_assessment' as type from risk_checks r join users u2 on r.user = u2.id "
                    +
                    ") rf " +
                    "WHERE 1=1 " + whereOption
                    + orderOption
                    + " limit " + bfPage.getOffset() + " ," + bfPage.getPageSize());

            return result;
        } catch (Exception e) {
            e.getStackTrace();
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            return null;
        }
    }

    public long countUnionChecklistAndRisk(String title, String type, Long projectId) {

        try {
            String whereOption = "";
            if (StringUtils.isNotEmpty(title)) {
                whereOption = whereOption + " and rf.name like '%" + title + "%' ";
            }
            if (StringUtils.isNotEmpty(type)) {
                whereOption = whereOption + " and rf.type = '" + type + "' ";
            }
            if (projectId != null) {
                whereOption = whereOption + " and rf.project = " + projectId + " ";
            }

            Map<String, Object> result = jdbcTemplate.queryForMap(
                "SELECT count(rf.id) cnt from " +
                    "( " +
                    "     select p.id, p.project, p.name, p.created_at, u1.id as user_id, u1.user_id as user_email_id, u1.user_name, 'checklist' as type from checklist_projects p join users u1 on p.user = u1.id "
                    +
                    "union all " +
                    "     select r.id, r.project, r.name, r.created_at, u2.id as user_id, u2.user_id as user_email_id, u2.user_name, 'risk_assessment' as type from risk_checks r join users u2 on r.user = u2.id "
                    +
                    ") rf " +
                    "WHERE 1=1 " + whereOption);

            return (long)result.get("cnt");
        } catch (Exception e) {
            e.getStackTrace();
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            return 0;
        }
    }

    public List<Map<String, Object>> findAllUnionChecklistTemplateAndRiskTemplate(String title, String type,
        YN createdAtDescended, YN nameDescended, YN userIdDescended, Long projectId, Pages bfPage) {

        try {
            String whereOption = "";
            if (StringUtils.isNotEmpty(title)) {
                whereOption = whereOption + " and rf.name like '%" + title + "%' ";
            }
            if (StringUtils.isNotEmpty(type)) {
                whereOption = whereOption + " and rf.type = '" + type + "' ";
            }
            if (projectId != null) {
                whereOption = whereOption + " and rf.project = " + projectId + " ";
            }
            String orderOption = "";
            if (createdAtDescended != null) {
                orderOption = orderOption + (!"".equals(orderOption) ? ", " : "") + " rf.created_at "
                    + (createdAtDescended == YN.Y ? "DESC" : "ASC");
            }
            if (nameDescended != null) {
                orderOption = orderOption + (!"".equals(orderOption) ? ", " : "") + " rf.name "
                    + (nameDescended == YN.Y ? "DESC" : "ASC");
            }
            if (userIdDescended != null) {
                orderOption = orderOption + (!"".equals(orderOption) ? ", " : "") + " rf.user_email_id "
                    + (userIdDescended == YN.Y ? "DESC" : "ASC");
            }
            if ("".equals(orderOption)) {
                orderOption = " order by rf.created_at DESC ";
            } else {
                orderOption = " order by " + orderOption;
            }

            List<Map<String, Object>> result = jdbcTemplate.queryForList(
                "SELECT "
                    + " rf.id, rf.name, rf.created_at, rf.type, rf.user_id, rf.user_email_id, rf.user_name, rf.project from "
                    +
                    "( " +
                    "     select p.id, p.project, p.name, p.created_at, u1.id as user_id, u1.user_id as user_email_id, u1.user_name, 'checklist' as type from checklist_templates p join users u1 on p.user = u1.id "
                    +
                    "union all " +
                    "     select r.id, r.project, r.name, r.created_at, u2.id as user_id, u2.user_id as user_email_id, u2.user_name, 'risk_assessment' as type from risk_templates r join users u2 on r.user = u2.id "
                    +
                    ") rf " +
                    "WHERE 1=1 " + whereOption
                    + " limit " + bfPage.getOffset() + " ," + bfPage.getPageSize());

            return result;
        } catch (Exception e) {
            e.getStackTrace();
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            return null;
        }
    }

    public long countAllUnionChecklistTemplateAndRiskTemplate(String title, String type, Long projectId) {

        try {
            String whereOption = "";
            if (StringUtils.isNotEmpty(title)) {
                whereOption = whereOption + " and rf.name like '%" + title + "%' ";
            }
            if (StringUtils.isNotEmpty(type)) {
                whereOption = whereOption + " and rf.type = '" + type + "' ";
            }
            if (projectId != null) {
                whereOption = whereOption + " and rf.project = " + projectId + " ";
            }

            Map<String, Object> result = jdbcTemplate.queryForMap(
                "SELECT count(rf.id) cnt from " +
                    "( " +
                    "     select p.id, p.project, p.name, p.created_at, u1.id as user_id, u1.user_id as user_email_id, u1.user_name, 'checklist' as type from checklist_templates p join users u1 on p.user = u1.id "
                    +
                    "union all " +
                    "     select r.id, r.project, r.name, r.created_at, u2.id as user_id, u2.user_id as user_email_id, u2.user_name, 'risk_assessment' as type from risk_templates r join users u2 on r.user = u2.id "
                    +
                    ") rf " +
                    "WHERE 1=1 " + whereOption);

            return (long)result.get("cnt");
        } catch (Exception e) {
            e.getStackTrace();
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            return 0;
        }
    }
}
