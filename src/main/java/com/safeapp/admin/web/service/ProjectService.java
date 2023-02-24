package com.safeapp.admin.web.service;

import com.safeapp.admin.web.dto.request.RequestProjectGroupEditDTO;
import com.safeapp.admin.web.dto.response.ResponseProjectGroupDTO;
import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.docs.InviteHistory;
import com.safeapp.admin.web.model.entity.*;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface ProjectService extends CRUDService<Project> {

    InviteHistory addAllGroup(InviteHistory newIvtHst, HttpServletRequest request) throws Exception;

    void editAllGroup(List<RequestProjectGroupEditDTO> prjGrEditList, HttpServletRequest request) throws Exception;

    void removeGroup(long id, HttpServletRequest request) throws Exception;

    long countDocList(long id, String userName, String name, HttpServletRequest request) throws Exception;

    List<Map<String, Object>> findDocList(long id, String userName, String name, int pageNo, int pageSize, HttpServletRequest request) throws Exception;

    void removeDoc(String docType, long id, HttpServletRequest request) throws Exception;

    List<ResponseProjectGroupDTO> findAllGroupByCondition(long id, int pageNo, int pageSize, HttpServletRequest request) throws Exception;

    /*
    long countProjectList(String name, String userName, String orderType, String status,
        String createdAtStart, String createdAtEnd, HttpServletRequest request) throws Exception;

    List<Map<String, Object>> findProjectList(String name, String userName, String orderType, String status,
        String createdAtStart, String createdAtEnd, int pageNo, int pageSize, HttpServletRequest request) throws Exception;
    */

    long countProjectList(String name, String userName, String createdAtStart, String createdAtEnd, HttpServletRequest request) throws Exception;

    List<Map<String, Object>> findProjectList(String name, String userName, String createdAtStart, String createdAtEnd,
        int pageNo, int pageSize, HttpServletRequest request) throws Exception;

}