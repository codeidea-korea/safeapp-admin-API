
package com.binoofactory.cornsqure.web.service;

import javax.servlet.http.HttpServletRequest;

import com.binoofactory.cornsqure.web.data.YN;
import com.binoofactory.cornsqure.web.dto.request.RequestRiskTemplateDTO;
import com.binoofactory.cornsqure.web.dto.response.ResponseRiskTemplateDTO;
import com.binoofactory.cornsqure.web.model.cmmn.service.BfCRUDService;
import com.binoofactory.cornsqure.web.model.entity.RiskTemplate;
import org.apache.ibatis.javassist.NotFoundException;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RiskTemplateService extends BfCRUDService<RiskTemplate> {
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
