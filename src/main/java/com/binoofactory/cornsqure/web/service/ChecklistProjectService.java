package com.binoofactory.cornsqure.web.service;

import javax.servlet.http.HttpServletRequest;

import com.binoofactory.cornsqure.web.data.StatusType;
import com.binoofactory.cornsqure.web.data.YN;
import com.binoofactory.cornsqure.web.dto.request.RequestChecklistProjectDTO;
import com.binoofactory.cornsqure.web.dto.request.RequestChecklistProjectModifyDTO;
import com.binoofactory.cornsqure.web.dto.response.ResponseChecklistProjectDTO;
import com.binoofactory.cornsqure.web.model.cmmn.service.BfCRUDService;
import com.binoofactory.cornsqure.web.model.entity.ChecklistProject;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChecklistProjectService extends BfCRUDService<ChecklistProject> {
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
