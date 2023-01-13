
package com.safeapp.admin.web.service;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.request.RequestRiskTemplateDTO;
import com.safeapp.admin.web.dto.response.ResponseRiskTemplateDTO;
import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.RiskTemplate;
import org.apache.ibatis.javassist.NotFoundException;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RiskTemplateService extends CRUDService<RiskTemplate> {
    RiskTemplate generate(RiskTemplate userSeq);
    
    boolean isLiked(long id, HttpServletRequest httpServletRequest);

    void addLike(long id, HttpServletRequest httpServletRequest);

    void removeLike(long id, HttpServletRequest httpServletRequest);

    RiskTemplate toEntity(RequestRiskTemplateDTO dto) throws NotFoundException;

    List<ResponseRiskTemplateDTO> findAllByCondition(
            Long checkerId,
            Long projectId,
            Long userId,
            String name,
            YN visibled,
            String tags,
            Pageable page
            );
}
