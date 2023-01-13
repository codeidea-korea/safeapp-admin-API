package com.safeapp.admin.web.service;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.data.StatusType;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.request.RequestRiskCheckDTO;
import com.safeapp.admin.web.dto.response.ResponseRiskcheckDTO;
import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.RiskCheck;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RiskCheckService extends CRUDService<RiskCheck> {
    RiskCheck generate(RiskCheck userSeq);
    
    boolean isLiked(long id, HttpServletRequest httpServletRequest);

    void addLike(long id, HttpServletRequest httpServletRequest);

    YN checkMyLike(long id, HttpServletRequest httpServletRequest);

    void removeLike(long id, HttpServletRequest httpServletRequest);

    RiskCheck toEntity(RequestRiskCheckDTO dto) throws NotFoundException;

    void updateStatus(Long id, StatusType type) throws NotFoundException;

    List<ResponseRiskcheckDTO> findAllByCondition(
            Long userId,
            Long projectId,
            String name,
            String tag,
            YN visibled,
            String status,
            YN created_at_descended,
            YN views_descended,
            YN likes_descended,
            String detail_contents,
            Pageable page,
            HttpServletRequest httpServletRequest
    );
}
