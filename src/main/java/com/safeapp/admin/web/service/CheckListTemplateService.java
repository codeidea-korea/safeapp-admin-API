package com.safeapp.admin.web.service;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.request.RequestCheckListTemplateDTO;
import com.safeapp.admin.web.dto.response.ResponseCheckListTemplateDTO;
import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.CheckListTemplate;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CheckListTemplateService extends CRUDService<CheckListTemplate> {

    CheckListTemplate toEntity(RequestCheckListTemplateDTO addDto) throws NotFoundException;

    CheckListTemplate generate(CheckListTemplate oldChkTmp);

    List<ResponseCheckListTemplateDTO> findAllByCondition(Long projectId, Long userId, String name, String tag,
            YN created_at_descended, YN views_descended, YN likes_descended, String detail_contents, Pageable pageable, HttpServletRequest request);

}