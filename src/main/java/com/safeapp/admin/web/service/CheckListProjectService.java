package com.safeapp.admin.web.service;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.data.StatusType;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.request.RequestCheckListProjectDTO;
import com.safeapp.admin.web.dto.request.RequestCheckListProjectModifyDTO;
import com.safeapp.admin.web.dto.response.ResponseCheckListProjectDTO;
import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.CheckListProject;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface CheckListProjectService extends CRUDService<CheckListProject> {

    CheckListProject toEntity(RequestCheckListProjectDTO addDto) throws NotFoundException;

    CheckListProject toEntityModify(RequestCheckListProjectModifyDTO modifyDto) throws NotFoundException;

    Long countAllByCondition(String keyword, String userName, String phoneNo, YN visibled,
            LocalDateTime createdAtStart, LocalDateTime createdAtEnd);

    List<ResponseCheckListProjectDTO> findAllByConditionAndOrderBy(String keyword, String userName, String phoneNo,
        YN visibled, LocalDateTime createdAtStart, LocalDateTime createdAtEnd, YN createdAtDesc, YN likesDesc, YN viewsDesc,
        int PageNo, int pageSize, HttpServletRequest request);


}