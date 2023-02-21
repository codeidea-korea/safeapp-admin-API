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

    // 마스터 관리자의 결제 기간이 유효해야
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
                        "AND pg.user = " + id
                );

            return (long)myProjectMap.get("cnt");

        } catch (Exception e) {
            e.getStackTrace();
            System.out.println(e.getCause());
            System.out.println(e.getMessage());

            return 0;
        }
    }
    
    // 마스터 관리자의 결제 기간이 유효해야
    public List<Map<String, Object>> findMyProjectList(long id, Pages pages) {
        try {
            List<Map<String, Object>> myProjectList =
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

            return myProjectList;

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

            Map<String, Object> userMap =
                jdbcTemplate.queryForMap(
                "SELECT COUNT(A.id) AS cnt FROM " +
                    "(" +
                        "SELECT u.*, ua.efective_start_at, ua.efective_end_at, ua.order_type FROM users u " +
                        "LEFT JOIN user_auths ua ON u.id = ua.user AND ua.created_at = (SELECT MAX(ua2.created_at) FROM user_auths ua2 WHERE ua2.user = u.id) " +
                        "WHERE u.delete_yn = false" +
                    ") A " +
                    "WHERE 1 = 1 " + whereOption
                );

            return (long)userMap.get("cnt");

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

            List<Map<String, Object>> userList =
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

            return userList;

        } catch (Exception e) {
            e.getStackTrace();
            System.out.println(e.getCause());
            System.out.println(e.getMessage());

            return null;
        }
    }

    public long countProjectList(long userId) {
        try {
            Map<String, Object> projectMap =
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

            return (long)projectMap.get("cnt");

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
            String whereOption2 = "";
            if(StringUtils.isNotEmpty(name)) {
                whereOption = whereOption + "AND p.name LIKE '%" + name + "%' ";
            }
            if(StringUtils.isNotEmpty(userName)) {
                whereOption = whereOption + "AND u.user_name LIKE '%" + userName + "%' ";
            }
            if(StringUtils.isNotEmpty(orderType)) {
                whereOption = whereOption + "AND ua.order_type LIKE '%" + orderType + "%' ";
            }
            if(StringUtils.isNotEmpty(status)) {
                if(status.equals("unsubscribe")) {
                    whereOption2 = "AND A.use_yn IS NULL ";
                } else {
                    whereOption = whereOption + "AND ua.status LIKE '%" + status + "%' ";
                }
            }
            if(StringUtils.isNotEmpty(createdAtStart)) {
                whereOption = whereOption + "AND p.created_at >= '" + createdAtStart + "' ";
            }
            if(StringUtils.isNotEmpty(createdAtEnd)) {
                whereOption = whereOption + "AND p.created_at <= '" + createdAtEnd + "' ";
            }

            Map<String, Object> projectMap =
                jdbcTemplate.queryForMap(
                "SELECT COUNT(A.id) AS cnt FROM " +
                    "(" +
                        "SELECT p.id, p.name, u.user_name, ua.order_type, ua.status, p.created_at FROM projects p " +
                        "LEFT JOIN project_groups pg ON p.id = pg.project AND pg.delete_yn = false AND pg.user_auth_type = 'TEAM_MASTER' " +
                        "LEFT JOIN user_auths ua ON pg.user = ua.user AND pg.user_auth_type = 'TEAM_MASTER' " +
                            "AND ua.created_at = (SELECT MAX(created_at) FROM user_auths WHERE user = pg.user) " +
                        "LEFT JOIN user_billing ub ON ua.user = ub.user_id AND ub.delete_yn = false AND ub.use_yn = 'Y' " +
                        "LEFT JOIN users u ON pg.user = u.id AND u.delete_yn = false " +
                        "WHERE p.delete_yn = false AND ua.status NOT IN ('first', 'temp') " + whereOption +
                    ") A " +
                    "WHERE 1 = 1 " + whereOption2
                );

            return (long)projectMap.get("cnt");

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
            String whereOption2 = "";
            if(StringUtils.isNotEmpty(name)) {
                whereOption = whereOption + "AND p.name LIKE '%" + name + "%' ";
            }
            if(StringUtils.isNotEmpty(userName)) {
                whereOption = whereOption + "AND u.user_name LIKE '%" + userName + "%' ";
            }
            if(StringUtils.isNotEmpty(orderType)) {
                whereOption = whereOption + "AND ua.order_type LIKE '%" + orderType + "%' ";
            }
            if(StringUtils.isNotEmpty(status)) {
                if(status.equals("unsubscribe")) {
                    whereOption2 = "AND A.use_yn IS NULL ";
                } else {
                    whereOption = whereOption + "AND ua.status LIKE '%" + status + "%' ";
                }
            }
            if(StringUtils.isNotEmpty(createdAtStart)) {
                whereOption = whereOption + "AND p.created_at >= '" + createdAtStart + "' ";
            }
            if(StringUtils.isNotEmpty(createdAtEnd)) {
                whereOption = whereOption + "AND p.created_at <= '" + createdAtEnd + "' ";
            }

            List<Map<String, Object>> projectList =
                jdbcTemplate.queryForList(
                "SELECT A.* FROM " +
                    "(" +
                        "SELECT " +
                            "p.id, p.name, p.created_at, u.user_name, ua.order_type, ua.status, " +
                            "(SELECT COUNT(pg2.id) FROM project_groups pg2 LEFT JOIN users u2 ON pg2.user = u2.id " +
                            "WHERE pg2.project = p.id AND pg2.delete_yn = false AND u2.delete_yn = false) AS group_cnt " +
                        "FROM projects p " +
                        "LEFT JOIN project_groups pg ON p.id = pg.project AND pg.delete_yn = false AND pg.user_auth_type = 'TEAM_MASTER' " +
                        "LEFT JOIN user_auths ua ON pg.user = ua.user AND ua.delete_yn = false " +
                            "AND ua.created_at = (SELECT MAX(created_at) FROM user_auths WHERE user = pg.user) " +
                        "LEFT JOIN user_billing ub ON ua.user = ub.user_id AND ub.delete_yn = false AND ub.use_yn = 'Y' " +
                        "LEFT JOIN users u ON pg.user = u.id AND u.delete_yn = false " +
                        "WHERE p.delete_yn = false AND ua.status NOT IN ('first', 'temp') " + whereOption +
                    ") A " +
                    "WHERE 1 = 1 " + whereOption2 +
                    "ORDER BY A.id DESC LIMIT " + (pageNo - 1) * pageSize + ", " + pageSize
                );

            return projectList;

        } catch (Exception e) {
            e.getStackTrace();
            System.out.println(e.getCause());
            System.out.println(e.getMessage());

            return null;
        }
    }

    public Map<String, Object> findMembership(long id) {
        try {
            Map<String, Object> membershipMap =
                jdbcTemplate.queryForMap(
                "SELECT " +
                        "auth.id, payment.merchant_uid, usr.user_id, usr.user_name, usr.phone_no, auth.order_type, " +
                        "auth.status AS auth_status, auth.efective_start_at, auth.efective_end_at, auth.created_at, payment.amount, " +
                        "payment.pay_method, payment.status AS pay_status, auth.memo, billing.use_yn " +
                    "FROM user_auths auth " +
                    "LEFT JOIN if_payments payment ON auth.payment_id = payment.id AND auth.delete_yn = false AND payment.delete_yn = false " +
                    "LEFT JOIN user_billing billing ON auth.user = billing.user_id AND billing.delete_yn = false AND billing.use_yn = 'Y' " +
                    "LEFT JOIN users usr ON auth.user = usr.id " +
                    "WHERE 1 = 1 AND auth.id = " + id
                );

            return membershipMap;

        } catch (Exception e) {
            e.getStackTrace();
            System.out.println(e.getCause());
            System.out.println(e.getMessage());

            return null;
        }
    }

    public void unsubscribe(long id) {
        try {
            jdbcTemplate.update(
            "UPDATE user_billing " +
                "SET use_yn = 'N' " +
                "WHERE user_id = (SELECT user FROM user_auths WHERE id = " + id + ")"
            );

        } catch (Exception e) {
            e.getStackTrace();
            System.out.println(e.getCause());
            System.out.println(e.getMessage());
        }
    }

    public long countMembershipList(String userName, String orderType, String status,
            String createdAtStart, String createdAtEnd) {

        try {
            String whereOption = "";
            String whereOption2 = "";
            if(StringUtils.isNotEmpty(userName)) {
                whereOption = whereOption + "AND usr.user_name LIKE '%" + userName + "%' ";
            }
            if(StringUtils.isNotEmpty(orderType)) {
                whereOption = whereOption + "AND auth.order_type LIKE '%" + orderType + "%' ";
            }
            if(StringUtils.isNotEmpty(status)) {
                if(status.equals("unsubscribe")) {
                    whereOption2 = "AND A.use_yn IS NULL ";
                } else {
                    whereOption = whereOption + "AND auth.status LIKE '%" + status + "%' ";
                }
            }
            if(!Objects.isNull(createdAtStart)) {
                whereOption = whereOption + "AND auth.created_at >= '" + createdAtStart + "' ";
            }
            if(!Objects.isNull(createdAtEnd)) {
                whereOption = whereOption + "AND auth.created_at <= '" + createdAtEnd + "' ";
            }

            Map<String, Object> membershipMap =
                jdbcTemplate.queryForMap(
                "SELECT COUNT(A.id) AS cnt, A.use_yn FROM " +
                    "(" +
                        "SELECT auth.id, usr.user_name, auth.order_type, auth.status, auth.created_at, billing.use_yn FROM user_auths auth " +
                        "LEFT JOIN user_billing billing ON auth.user = billing.user_id AND billing.delete_yn = false AND billing.use_yn = 'Y' " +
                        "LEFT JOIN users usr ON auth.user = usr.id " +
                        "WHERE 1 = 1 " +
                            "AND auth.status NOT IN ('first', 'temp') " + whereOption +
                    ") A " +
                    "WHERE 1 = 1 " + whereOption2
                );

            return (long)membershipMap.get("cnt");

        } catch (Exception e) {
            e.getStackTrace();
            System.out.println(e.getCause());
            System.out.println(e.getMessage());

            return 0;
        }

    }

    public List<Map<String, Object>> findMembershipList(String userName, String orderType, String status,
            String createdAtStart, String createdAtEnd, int pageNo, int pageSize, HttpServletRequest request) {

        try {
            String whereOption = "";
            String whereOption2 = "";
            if(StringUtils.isNotEmpty(userName)) {
                whereOption = whereOption + "AND usr.user_name LIKE '%" + userName + "%' ";
            }
            if(StringUtils.isNotEmpty(orderType)) {
                whereOption = whereOption + "AND auth.order_type LIKE '%" + orderType + "%' ";
            }
            if(StringUtils.isNotEmpty(status)) {
                if(status.equals("unsubscribe")) {
                    whereOption2 = "AND A.use_yn IS NULL ";
                } else {
                    whereOption = whereOption + "AND auth.status LIKE '%" + status + "%' ";
                }
            }
            if(!Objects.isNull(createdAtStart)) {
                whereOption = whereOption + "AND auth.created_at >= '" + createdAtStart + "' ";
            }
            if(!Objects.isNull(createdAtEnd)) {
                whereOption = whereOption + "AND auth.created_at <= '" + createdAtEnd + "' ";
            }

            List<Map<String, Object>> membershipList =
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
                        "LEFT JOIN user_billing billing ON auth.user = billing.user_id AND billing.delete_yn = false AND billing.use_yn = 'Y' " +
                        "LEFT JOIN users usr ON auth.user = usr.id " +
                        "WHERE 1 = 1 " +
                            "AND auth.status NOT IN ('first', 'temp') " + whereOption +
                    ") A " +
                    "WHERE 1 = 1 " + whereOption2 +
                    "ORDER BY A.id DESC LIMIT " + (pageNo - 1) * pageSize + ", " + pageSize
                );

            return membershipList;

        } catch (Exception e) {
            e.getStackTrace();
            System.out.println(e.getCause());
            System.out.println(e.getMessage());

            return null;
        }

    }

}