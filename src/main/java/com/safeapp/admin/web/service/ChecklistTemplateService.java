package com.safeapp.admin.web.service;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.request.RequestChecklistTemplateDTO;
import com.safeapp.admin.web.dto.response.ResponseChecklistTemplateDTO;
import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.ChecklistTemplate;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChecklistTemplateService extends CRUDService<ChecklistTemplate> {
    ChecklistTemplate generate(ChecklistTemplate userSeq);
    
    boolean isLiked(long id, HttpServletRequest httpServletRequest);

    void addLike(long id, HttpServletRequest httpServletRequest);

    void removeLike(long id, HttpServletRequest httpServletRequest);

    ChecklistTemplate toEntity(RequestChecklistTemplateDTO dto) throws NotFoundException;

    List<ResponseChecklistTemplateDTO> findAllByCondition(
            Long userId,
            Long projectId,
            String name,
            String tag,
            YN created_at_descended,
            YN views_descended,
            YN likes_descended,
            String detail_contents,
            Pageable page
    );
}
