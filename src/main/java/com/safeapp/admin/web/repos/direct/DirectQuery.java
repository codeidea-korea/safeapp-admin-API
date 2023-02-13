package com.safeapp.admin.web.repos.direct;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

import javax.servlet.http.HttpServletRequest;

@Repository
@AllArgsConstructor
@Slf4j
public class DirectQuery {

    private final JdbcTemplate jdbcTemplate;

    public Map<String, Object> findMyAuth(long id) {
        try {
            Map<String, Object> myAuthMap =
                jdbcTemplate.queryForMap(
                    "SELECT u.id, u.user_id, u.user_name, ua.order_type FROM users u " +
                    "LEFT JOIN user_auths ua ON u.id = ua.user AND ua.created_at = (SELECT MAX(ua2.created_at) FROM user_auths ua2 WHERE ua2.user = u.id) " +
                    "WHERE 1 = 1 AND u.id = " + id
                );

            return myAuthMap;

        } catch (Exception e) {
            e.getStackTrace();
            System.out.println(e.getCause());
            System.out.println(e.getMessage());

            return null;
        }
    }

    // 마스터 관리자의 결제기간이 유효해야함
    public long countMyProjectList(long id) {
        try {
            Map<String, Object> myProjectMap =
                jdbcTemplate.queryForMap(
                "SELECT COUNT(p.id) AS cnt FROM projects p " +
                    "LEFT JOIN project_groups pg ON p.id = pg.project " +
                    "WHERE 1 = 1 " +
                        "AND p.delete_yn = false " +
                        "AND p.id IN " +
                        "(" +
                            "SELECT DISTINCT(pg.project) FROM project_groups pg " +
                            "LEFT JOIN user_auths ua ON pg.user = ua.user " +
                            "WHERE 1 = 1 " +
                                "AND pg.project IN (SELECT project FROM project_groups WHERE 1 = 1 AND user = " + id + " AND delete_yn = false) " +
                                "AND pg.user_auth_type = 'TEAM_MASTER' " +
                                "AND ua.status = 'ing' " +
                        ") " +
                        "AND pg.user = 13 "
                );

            return (long)myProjectMap.get("cnt");

        } catch (Exception e) {
            e.getStackTrace();
            System.out.println(e.getCause());
            System.out.println(e.getMessage());

            return 0;
        }
    }
    
    // 마스터 관리자의 결제기간이 유효해야함
    public List<Map<String, Object>> findMyProjectList(long id, Pages pages) {
        try {
            List<Map<String, Object>> myProject =
                jdbcTemplate.queryForList(
                    "SELECT p.id, p.name, p.updated_at, pg.user_auth_type FROM projects p " +
                    "LEFT JOIN project_groups pg ON p.id = pg.project " +
                    "WHERE 1 = 1 " +
                        "AND p.delete_yn = false " +
                        "AND p.id IN " +
                        "(" +
                            "SELECT DISTINCT(pg.project) FROM project_groups pg " +
                            "LEFT JOIN user_auths ua ON pg.user = ua.user " +
                            "WHERE 1 = 1 " +
                                "AND pg.project IN (SELECT project FROM project_groups WHERE 1 = 1 AND user = " + id + " AND delete_yn = false) " +
                                "AND pg.user_auth_type = 'TEAM_MASTER' " +
                                "AND ua.status = 'ing' " +
                        ") " +
                        "AND pg.user = " + id + " " +
                    "ORDER BY pg.project ASC " +
                    "LIMIT " + (pages.getPageNo() - 1) * pages.getPageSize() + ", " + pages.getPageSize()
                );

            return myProject;

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
                        "SELECT u.*, ua.efective_start_at, ua.efective_end_at, ua.order_type FROM users u " +
                        "LEFT JOIN user_auths ua ON u.id = ua.user AND ua.created_at = (SELECT MAX(ua2.created_at) FROM user_auths ua2 WHERE ua2.user = u.id) " +
                        "WHERE u.delete_yn = false" +
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
                        "SELECT u.*, ua.efective_start_at, ua.efective_end_at, ua.order_type FROM users u " +
                        "LEFT JOIN user_auths ua ON u.id = ua.user AND ua.created_at = (SELECT MAX(ua2.created_at) FROM user_auths ua2 WHERE ua2.user = u.id) " +
                        "WHERE u.delete_yn = false" +
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

    public long countProjectList(long userId) {
        try {
            Map<String, Object> myProject =
                jdbcTemplate.queryForMap(
                "SELECT COUNT(pg.id) AS cnt FROM project_groups pg " +
                    "LEFT JOIN user_auths ua ON pg.user = ua.user " +
                    "LEFT JOIN projects p ON pg.project = p.id " +
                    "WHERE 1 = 1 " +
                        "AND pg.project IN (SELECT DISTINCT(project) FROM project_groups WHERE 1 = 1 AND user = " + userId + " AND delete_yn = false) " +
                        "AND pg.user_auth_type = 'TEAM_MASTER' " +
                        "AND ua.status = 'ing' " +
                        "AND p.delete_yn = false " +
                    "ORDER BY pg.project ASC"
                );

            return (long)myProject.get("cnt");

        } catch (Exception e) {
            e.getStackTrace();
            System.out.println(e.getCause());
            System.out.println(e.getMessage());

            return 0;
        }
    }

    public long countProjectList(String name, String userName, String orderType, String status,
            String createdAtStart, String createdAtEnd) {

        try {
            String whereOption = "";
            if(StringUtils.isNotEmpty(name)) {
                whereOption = whereOption + "AND A.name LIKE '%" + name + "%' ";
            }
            if(StringUtils.isNotEmpty(userName)) {
                whereOption = whereOption + "AND A.user_name LIKE '%" + userName + "%' ";
            }
            if(StringUtils.isNotEmpty(orderType)) {
                whereOption = whereOption + "AND A.order_type LIKE '%" + orderType + "%' ";
            }
            if(StringUtils.isNotEmpty(status)) {
                whereOption = whereOption + "AND A.status LIKE '%" + status + "%' ";
            }
            if(StringUtils.isNotEmpty(createdAtStart)) {
                whereOption = whereOption + "AND A.created_at >= '" + createdAtStart + "' ";
            }
            if(StringUtils.isNotEmpty(createdAtEnd)) {
                whereOption = whereOption + "AND A.created_at <= '" + createdAtEnd + "' ";
            }

            Map<String, Object> resultMap =
                jdbcTemplate.queryForMap(
                "SELECT COUNT(A.id) AS cnt FROM " +
                    "(" +
                        "SELECT DISTINCT(p.id) FROM projects p " +
                        "LEFT JOIN project_groups pg ON p.id = pg.project AND pg.delete_yn = false AND pg.user_auth_type = 'TEAM_MASTER' " +
                        "LEFT JOIN user_auths ua ON pg.user = ua.user AND pg.user_auth_type = 'TEAM_MASTER' " +
                        "LEFT JOIN users u ON pg.user = u.id AND u.delete_yn = false " +
                        "WHERE p.delete_yn = false" +
                    ") A " +
                    "WHERE 1 = 1 " + whereOption
                );

            return (long)resultMap.get("cnt");

        } catch (Exception e) {
            e.getStackTrace();
            System.out.println(e.getCause());
            System.out.println(e.getMessage());

            return 0;
        }

    }

    public List<Map<String, Object>> findProjectList(String name, String userName, String orderType, String status,
            String createdAtStart, String createdAtEnd, int pageNo, int pageSize) {

        try {
            String whereOption = "";
            if(StringUtils.isNotEmpty(name)) {
                whereOption = whereOption + "AND A.name LIKE '%" + name + "%' ";
            }
            if(StringUtils.isNotEmpty(userName)) {
                whereOption = whereOption + "AND A.user_name LIKE '%" + userName + "%' ";
            }
            if(StringUtils.isNotEmpty(orderType)) {
                whereOption = whereOption + "AND A.order_type LIKE '%" + orderType + "%' ";
            }
            if(StringUtils.isNotEmpty(status)) {
                whereOption = whereOption + "AND A.status LIKE '%" + status + "%' ";
            }
            if(StringUtils.isNotEmpty(createdAtStart)) {
                whereOption = whereOption + "AND A.created_at >= '" + createdAtStart + "' ";
            }
            if(StringUtils.isNotEmpty(createdAtEnd)) {
                whereOption = whereOption + "AND A.created_at <= '" + createdAtEnd + "' ";
            }

            List<Map<String, Object>> resultList =
                jdbcTemplate.queryForList(
                "SELECT A.* FROM " +
                    "(" +
                        "SELECT p.id, p.name, p.created_at, u.user_name, ua.order_type, ua.status, " +
                        "(SELECT COUNT(pg2.id) FROM project_groups pg2 LEFT JOIN users u2 ON pg2.user = u2.id " +
                        "WHERE pg2.project = p.id AND pg2.delete_yn = false AND u2.delete_yn = false) AS group_cnt " +
                        "FROM projects p " +
                        "LEFT JOIN project_groups pg ON p.id = pg.project AND pg.delete_yn = false " +
                        "AND pg.user_auth_type = 'TEAM_MASTER' " +
                        "LEFT JOIN user_auths ua ON pg.user = ua.user AND pg.user_auth_type = 'TEAM_MASTER' " +
                        "LEFT JOIN users u ON pg.user = u.id AND u.delete_yn = false " +
                        "WHERE p.delete_yn = false" +
                    ") A " +
                    "WHERE 1 = 1 " + whereOption +
                    "ORDER BY A.id DESC LIMIT " + (pageNo - 1) * pageSize + ", " + pageSize
                );

            return resultList;

        } catch (Exception e) {
            e.getStackTrace();
            System.out.println(e.getCause());
            System.out.println(e.getMessage());

            return null;
        }
    }

    public Map<String, Object> findMembership(long id) {
        try {
            Map<String, Object> oldMembership =
                jdbcTemplate.queryForMap(
                "SELECT " +
                        "auth.id, payment.merchant_uid, usr.user_id, usr.user_name, usr.phone_no, auth.order_type, " +
                        "auth.status AS auth_status, auth.efective_start_at, auth.efective_end_at, auth.created_at, payment.amount, " +
                        "payment.pay_method, payment.status AS pay_status, auth.memo, billing.use_yn " +
                    "FROM user_auths auth " +
                    "LEFT JOIN if_payments payment ON auth.payment_id = payment.id AND auth.delete_yn = false AND payment.delete_yn = false " +
                    "LEFT JOIN user_billing billing ON payment.id = billing.payment_id AND billing.delete_yn = false " +
                    "LEFT JOIN users usr ON auth.user = usr.id " +
                    "WHERE 1 = 1 AND auth.id = " + id
                );

            return oldMembership;

        } catch (Exception e) {
            e.getStackTrace();
            System.out.println(e.getCause());
            System.out.println(e.getMessage());

            return null;
        }
    }

    public long countMembershipList(String userName, String orderType, String status,
            LocalDateTime createdAtStart, LocalDateTime createdAtEnd) {

        try {
            String whereOption = "";
            if(StringUtils.isNotEmpty(userName)) {
                whereOption = whereOption + "AND usr.user_name LIKE '%" + userName + "%' ";
            }
            if(StringUtils.isNotEmpty(orderType)) {
                whereOption = whereOption + "AND auth.order_type LIKE '%" + orderType + "%' ";
            }
            if(StringUtils.isNotEmpty(status)) {
                whereOption = whereOption + "AND auth.status LIKE '%" + status + "%' ";
            }
            if(!Objects.isNull(createdAtStart)) {
                whereOption = whereOption + "AND auth.created_at >= '" + createdAtStart + "' ";
            }
            if(!Objects.isNull(createdAtEnd)) {
                whereOption = whereOption + "AND auth.created_at <= '" + createdAtEnd + "' ";
            }

            Map<String, Object> resultMap =
                jdbcTemplate.queryForMap(
                    "SELECT COUNT(auth.id) AS cnt FROM user_auths auth " +
                    "LEFT JOIN users usr ON auth.user = usr.id " +
                    "WHERE 1 = 1 AND auth.status NOT IN ('first', 'temp') " + whereOption
                );

            return (long)resultMap.get("cnt");

        } catch (Exception e) {
            e.getStackTrace();
            System.out.println(e.getCause());
            System.out.println(e.getMessage());

            return 0;
        }

    }

    public List<Map<String, Object>> findMembershipList(String userName, String orderType, String status,
            LocalDateTime createdAtStart, LocalDateTime createdAtEnd, int pageNo, int pageSize, HttpServletRequest request) {

        try {
            String whereOption = "";
            if(StringUtils.isNotEmpty(userName)) {
                whereOption = whereOption + "AND usr.user_name LIKE '%" + userName + "%' ";
            }
            if(StringUtils.isNotEmpty(orderType)) {
                whereOption = whereOption + "AND auth.order_type LIKE '%" + orderType + "%' ";
            }
            if(StringUtils.isNotEmpty(status)) {
                whereOption = whereOption + "AND auth.status LIKE '%" + status + "%' ";
            }
            if(!Objects.isNull(createdAtStart)) {
                whereOption = whereOption + "AND auth.created_at >= '" + createdAtStart + "' ";
            }
            if(!Objects.isNull(createdAtEnd)) {
                whereOption = whereOption + "AND auth.created_at <= '" + createdAtEnd + "' ";
            }

            List<Map<String, Object>> resultList =
                jdbcTemplate.queryForList(
                "SELECT A.* FROM " +
                    "(" +
                        "SELECT " +
                            "auth.id, payment.merchant_uid, usr.user_id, usr.user_name, usr.phone_no, auth.order_type, " +
                            "auth.status AS auth_status, auth.efective_start_at, auth.efective_end_at, auth.created_at, payment.amount, " +
                            "payment.pay_method, payment.status AS pay_status, auth.memo, billing.use_yn " +
                        "FROM user_auths auth " +
                        "LEFT JOIN if_payments payment ON auth.payment_id = payment.id AND auth.delete_yn = false AND payment.delete_yn = false " +
                        /*"AND auth.created_at = (SELECT MAX(created_at) FROM if_payments WHERE payment_id = auth.payment_id) " +*/
                        "LEFT JOIN user_billing billing ON payment.id = billing.payment_id AND billing.delete_yn = false " +
                        "LEFT JOIN users usr ON auth.user = usr.id " +
                        "WHERE 1 = 1 " +
                            "AND auth.status NOT IN ('first', 'temp') " +
                            whereOption +
                    ") A " +
                    "ORDER BY A.id DESC LIMIT " + (pageNo - 1) * pageSize + ", " + pageSize
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