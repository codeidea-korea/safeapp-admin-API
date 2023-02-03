package com.safeapp.admin.web.repos.direct;

import java.util.List;
import java.util.Map;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.Users;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
@Slf4j
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

    public long countUserList(Users user) {
        try {
            String whereOption = "";
            if(StringUtils.isNotEmpty(user.getUserId())) {
                whereOption = whereOption + "AND A.user_id LIKE '%" + user.getUserId() + "%' ";
            }
            if(StringUtils.isNotEmpty(user.getUserName())) {
                whereOption = whereOption + "AND A.user_name LIKE '%" + user.getUserName() + "%' ";
            }
            if(StringUtils.isNotEmpty(user.getPhoneNo())) {
                whereOption = whereOption + "AND A.phone_no LIKE '%" + user.getPhoneNo() + "%' ";
            }

            Map<String, Object> result =
                jdbcTemplate.queryForMap(
                "SELECT COUNT(A.id) AS cnt FROM " +
                    "(" +
                    "SELECT u.*, ua.efective_start_at, ua.efective_end_at, ua.order_type FROM users u LEFT JOIN user_auths ua ON u.id = ua.user " +
                    "UNION " +
                    "SELECT u.*, ua.efective_start_at, ua.efective_end_at, ua.order_type FROM users u RIGHT JOIN user_auths ua ON u.id = ua.user WHERE 1 = 1 AND u.delete_yn = false AND ua.status = 'ing'" +
                    ") A " +
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

    public List<Map<String, Object>> findUserList(Users user, Pages pages) {
        try {
            String whereOption = "";
            if(StringUtils.isNotEmpty(user.getUserId())) {
                whereOption = whereOption + "AND user_id LIKE '%" + user.getUserId() + "%' ";
            }
            if(StringUtils.isNotEmpty(user.getUserName())) {
                whereOption = whereOption + "AND user_name LIKE '%" + user.getUserName() + "%' ";
            }
            if(StringUtils.isNotEmpty(user.getPhoneNo())) {
                whereOption = whereOption + "AND phone_no LIKE '%" + user.getPhoneNo() + "%' ";
            }

            List<Map<String, Object>> resultList =
                jdbcTemplate.queryForList(
                "SELECT A.* FROM " +
                    "(" +
                        "SELECT u.*, ua.efective_start_at, ua.efective_end_at, ua.order_type FROM users u LEFT JOIN user_auths ua ON u.id = ua.user " +
                        "UNION " +
                        "SELECT u.*, ua.efective_start_at, ua.efective_end_at, ua.order_type FROM users u RIGHT JOIN user_auths ua ON u.id = ua.user WHERE 1 = 1 AND u.delete_yn = false AND ua.status = 'ing'" +
                     ") A " +
                    "WHERE 1 = 1 " + whereOption +
                    "ORDER BY A.id DESC LIMIT " + (pages.getPageNo() - 1) * pages.getPageSize() + ", " + pages.getPageSize()
                );

            return resultList;

        } catch (Exception e) {
            e.getStackTrace();
            System.out.println(e.getCause());
            System.out.println(e.getMessage());

            return null;
        }
    }

}