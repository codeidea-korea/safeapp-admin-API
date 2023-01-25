package com.safeapp.admin.web.repos.jpa.custom;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.response.ResponseCheckListTemplateDTO;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CheckListTemplateRepositoryCustom {

    List<ResponseCheckListTemplateDTO> findAllByCondition(Long userId, Long projectId, String name, String tag,
            YN created_at_descended, YN views_descended, YN likes_descended, String detail_contents, Pageable page, HttpServletRequest request);

}