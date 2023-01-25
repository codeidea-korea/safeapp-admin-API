package com.safeapp.admin.web.repos.direct;

import java.util.List;
import java.util.Map;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.cmmn.Pages;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class DirectQuery {

    private final JdbcTemplate jdbcTemplate;

    public long countUnionCheckListAndRisk(String title, String type, Long projectId) {
        try {
            String whereOption = "";
            if(StringUtils.isNotEmpty(title)) {
                whereOption = whereOption + " AND rf.name LIKE '%" + title + "%' ";
            }
            if(StringUtils.isNotEmpty(type)) {
                whereOption = whereOption + " AND rf.type = '" + type + "' ";
            }
            if(projectId != null) {
                whereOption = whereOption + " AND rf.project = " + projectId + " ";
            }

            Map<String, Object> result =
                jdbcTemplate.queryForMap(
                "SELECT COUNT(rf.id) cnt " +
                    "FROM ( " +
                        "   SELECT p.id, p.project, p.name, p.created_at, u1.id AS user_id, u1.user_id AS user_email_id, u1.user_name, 'checkList' AS type FROM checklist_projects p JOIN users u1 ON p.user = u1.id " +
                        "   UNION ALL " +
                        "   SELECT r.id, r.project, r.name, r.created_at, u2.id as user_id, u2.user_id as user_email_id, u2.user_name, 'risk_assessment' as type from risk_checks r join users u2 on r.user = u2.id " +
                    ") rf " +
                    "WHERE 1 = 1 " + whereOption
                );

            return (long)result.get("cnt");

        } catch (Exception e) {
            e.getStackTrace();
            System.out.println(e.getCause());
            System.out.println(e.getMessage());

            return 0;
        }
    }

    public List<Map<String, Object>> findAllUnionCheckListAndRisk(String title, String type, YN createdAtDescended, YN nameDescended,
            YN userIdDescended, Long projectId, Pages pages) {

        try {
            String whereOption = "";
            if(StringUtils.isNotEmpty(title)) {
                whereOption = whereOption + " AND rf.name LIKE '%" + title + "%' ";
            }
            if(StringUtils.isNotEmpty(type)) {
                whereOption = whereOption + " AND rf.type = '" + type + "' ";
            }
            if(projectId != null) {
                whereOption = whereOption + " AND rf.project = " + projectId + " ";
            }

            String orderOption = "";
            if(createdAtDescended != null) {
                orderOption =
                    orderOption + (!orderOption.equals("") ? ", " : "") + " rf.created_at "
                    + (createdAtDescended == YN.Y ? "DESC" : "ASC");
            }
            if(nameDescended != null) {
                orderOption =
                    orderOption + (!orderOption.equals("") ? ", " : "") + " rf.name "
                    + (nameDescended == YN.Y ? "DESC" : "ASC");
            }
            if(userIdDescended != null) {
                orderOption =
                    orderOption + (!orderOption.equals("") ? ", " : "") + " rf.user_email_id "
                    + (userIdDescended == YN.Y ? "DESC" : "ASC");
            }
            if(orderOption.equals("")) {
                orderOption = " ORDER BY rf.created_at DESC ";
            } else {
                orderOption = " ORDER BY " + orderOption + " ";
            }

            List<Map<String, Object>> result =
                jdbcTemplate.queryForList(
                "SELECT rf.id, rf.name, rf.created_at, rf.type, rf.user_id, rf.user_email_id, rf.user_name, rf.project " +
                    "FROM (" +
                    "       SELECT p.id, p.project, p.name, p.created_at, u1.id AS user_id, u1.user_id AS user_email_id, u1.user_name, 'checkList' AS type FROM checklist_projects p JOIN users u1 ON p.user = u1.id " +
                    "       UNION ALL " +
                    "       SELECT r.id, r.project, r.name, r.created_at, u2.id AS user_id, u2.user_id AS user_email_id, u2.user_name, 'risk' AS type FROM risk_checks r JOIN users u2 ON r.user = u2.id " +
                    ") rf " +
                    "WHERE 1 = 1 " + whereOption +
                    orderOption +
                    "LIMIT " + pages.getOffset() + ", " + pages.getPageSize()
                );

            return result;

        } catch (Exception e) {
            e.getStackTrace();
            System.out.println(e.getCause());
            System.out.println(e.getMessage());

            return null;
        }
    }

    public long countAllUnionCheckListTemplateAndRiskTemplate(String title, String type, Long projectId) {
        try {
            String whereOption = "";
            if(StringUtils.isNotEmpty(title)) {
                whereOption = whereOption + " AND rf.name LIKE '%" + title + "%' ";
            }
            if(StringUtils.isNotEmpty(type)) {
                whereOption = whereOption + " AND rf.type = '" + type + "' ";
            }
            if(projectId != null) {
                whereOption = whereOption + " AND rf.project = " + projectId + " ";
            }

            Map<String, Object> result =
                jdbcTemplate.queryForMap(
                "SELECT COUNT(rf.id) cnt " +
                    "FROM ( " +
                    "       SELECT p.id, p.project, p.name, p.created_at, u1.id AS user_id, u1.user_id AS user_email_id, u1.user_name, 'checkList' AS type FROM checklist_templates p JOIN users u1 ON p.user = u1.id " +
                    "       UNION ALL " +
                    "       SELECT r.id, r.project, r.name, r.created_at, u2.id AS user_id, u2.user_id AS user_email_id, u2.user_name, 'risk_assessment' AS type FROM risk_templates r JOIN users u2 ON r.user = u2.id " +
                    ") rf " +
                    "WHERE 1 = 1 " + whereOption
                );

            return (long)result.get("cnt");

        } catch (Exception e) {
            e.getStackTrace();
            System.out.println(e.getCause());
            System.out.println(e.getMessage());

            return 0;
        }
    }

    public List<Map<String, Object>> findAllUnionCheckListTemplateAndRiskTemplate(String title, String type, YN createdAtDescended,
            YN nameDescended, YN userIdDescended, Long projectId, Pages pages) {

        try {
            String whereOption = "";
            if(StringUtils.isNotEmpty(title)) {
                whereOption = whereOption + " AND rf.name LIKE '%" + title + "%' ";
            }
            if(StringUtils.isNotEmpty(type)) {
                whereOption = whereOption + " AND rf.type = '" + type + "' ";
            }
            if(projectId != null) {
                whereOption = whereOption + " AND rf.project = " + projectId + " ";
            }

            String orderOption = "";
            if(createdAtDescended != null) {
                orderOption =
                    orderOption + (!orderOption.equals("") ? ", " : "") + " rf.created_at "
                    + (createdAtDescended == YN.Y ? "DESC" : "ASC");
            }
            if(nameDescended != null) {
                orderOption =
                    orderOption + (!orderOption.equals("") ? ", " : "") + " rf.name "
                    + (nameDescended == YN.Y ? "DESC" : "ASC");
            }
            if(userIdDescended != null) {
                orderOption =
                    orderOption + (!orderOption.equals("") ? ", " : "") + " rf.user_email_id "
                    + (userIdDescended == YN.Y ? "DESC" : "ASC");
            }
            if(orderOption.equals("")) {
                orderOption = " ORDER BY rf.created_at DESC ";
            } else {
                orderOption = " ORDER BY " + orderOption + " ";;
            }

            List<Map<String, Object>> result =
                jdbcTemplate.queryForList(
                "SELECT rf.id, rf.name, rf.created_at, rf.type, rf.user_id, rf.user_email_id, rf.user_name, rf.project " +
                    "FROM ( " +
                    "       SELECT p.id, p.project, p.name, p.created_at, u1.id AS user_id, u1.user_id AS user_email_id, u1.user_name, 'checkList' AS type FROM checklist_templates p JOIN users u1 ON p.user = u1.id " +
                    "       UNION ALL " +
                    "       SELECT r.id, r.project, r.name, r.created_at, u2.id AS user_id, u2.user_id AS user_email_id, u2.user_name, 'risk_assessment' AS type FROM risk_templates r JOIN users u2 ON r.user = u2.id " +
                    ") rf " +
                    "WHERE 1 = 1 " + whereOption +
                    orderOption +
                    "LIMIT " + pages.getOffset() + ", " + pages.getPageSize()
                );

            return result;

        } catch (Exception e) {
            e.getStackTrace();
            System.out.println(e.getCause());
            System.out.println(e.getMessage());

            return null;
        }
    }

}