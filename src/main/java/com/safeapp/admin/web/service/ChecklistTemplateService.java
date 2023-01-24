package com.safeapp.admin.web.service;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.request.RequestCheckListTemplateDTO;
import com.safeapp.admin.web.dto.response.ResponseCheckListTemplateDTO;
import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.ChecklistTemplate;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CheckListTemplateService extends CRUDService<CheckListTemplate> {

    ChecklistTemplate generate(ChecklistTemplate userSeq);
    
    boolean isLiked(long id, HttpServletRequest httpServletRequest);

    void addLike(long id, HttpServletRequest httpServletRequest);

    void removeLike(long id, HttpServletRequest httpServletRequest);

    ChecklistTemplate toEntity(RequestCheckListTemplateDTO dto) throws NotFoundException;

    List<ResponseCheckListTemplateDTO> findAllByCondition(
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
