package com.safeapp.admin.web.service;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.data.StatusType;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.request.RequestChecklistProjectDTO;
import com.safeapp.admin.web.dto.request.RequestChecklistProjectModifyDTO;
import com.safeapp.admin.web.dto.response.ResponseChecklistProjectDTO;
import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.ChecklistProject;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChecklistProjectService extends CRUDService<ChecklistProject> {
    ChecklistProject generate(ChecklistProject userSeq);
    
    boolean isLiked(long id, HttpServletRequest httpServletRequest);

    void addLike(long id, HttpServletRequest httpServletRequest);
    YN checkMyLike(long id, HttpServletRequest httpServletRequest);

    void removeLike(long id, HttpServletRequest httpServletRequest);

    ChecklistProject toEntity(RequestChecklistProjectDTO dto) throws NotFoundException;

    void updateStatus(Long id, StatusType type) throws NotFoundException;

    ChecklistProject toEntityModify(RequestChecklistProjectModifyDTO dto) throws NotFoundException;

    List<ResponseChecklistProjectDTO> findAllByCondition(
            Long userId,
            Long projectId,
            String name,
            String tag,
            YN visibled,
            YN created_at_descended,
            YN views_descended,
            YN likes_descended,
            String detail_contents,
            Pageable page,
            HttpServletRequest request
    );
}
