package com.safeapp.admin.web.service.impl;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.dto.request.RequestCheckListTemplateDTO;
import com.safeapp.admin.web.dto.response.ResponseCheckListTemplateDTO;
import com.safeapp.admin.web.model.entity.CheckListTemplate;
import com.safeapp.admin.web.repos.jpa.ProjectRepos;
import com.safeapp.admin.web.repos.jpa.UserRepos;
import com.safeapp.admin.utils.DateUtil;
import com.safeapp.admin.utils.PasswordUtil;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.docs.LikeHistory;
import com.safeapp.admin.web.model.entity.Users;
import com.safeapp.admin.web.repos.jpa.CheckListTemplateRepository;
import com.safeapp.admin.web.repos.jpa.dsl.CheckListTemplateDslRepos;
import com.safeapp.admin.web.service.CheckListTemplateService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

@Service
@AllArgsConstructor
public class CheckListTemplateServiceImpl implements CheckListTemplateService {

    private final CheckListTemplateRepository chkTmpRepos;
    private final ProjectRepos projectRepos;
    private final UserRepos userRepos;

    @Override
    public CheckListTemplate toEntity(RequestCheckListTemplateDTO addDto) throws NotFoundException {
        CheckListTemplate chkTmp = new CheckListTemplate();

        chkTmp.setProject(projectRepos.findById(addDto.getProjectId()).orElseThrow(() -> new NotFoundException("Input Project ID: " + addDto.getProjectId())));
        chkTmp.setUser(userRepos.findById(addDto.getUserId()).orElseThrow(() -> new NotFoundException("Input User ID: " + addDto.getUserId())));
        chkTmp.setName(addDto.getName());
        chkTmp.setTag(addDto.getTag());
        chkTmp.setRelatedAcidNo(addDto.getRelatedAcidNo());
        chkTmp.setChecker(userRepos.findById(addDto.getCheckerId()).orElseThrow(() -> new NotFoundException("Input Checker ID: " + addDto.getCheckerId())));
        chkTmp.setReviewer(userRepos.findById(addDto.getReviewerId()).orElseThrow(() -> new NotFoundException("Input Reviewer ID: " + addDto.getReviewerId())));
        chkTmp.setApprover(userRepos.findById(addDto.getApproverId()).orElseThrow(() -> new NotFoundException("Input User ID: " + addDto.getUserId())));

        return chkTmp;
    }

    @Transactional
    @Override
    public CheckListTemplate add(CheckListTemplate chkTmp, HttpServletRequest request) throws Exception {
        if(Objects.isNull(chkTmp)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 체크리스트 템플릿입니다.");
        }

        CheckListTemplate addedChkTmp = chkTmpRepos.save(chkTmp);
        if(Objects.isNull(addedChkTmp)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "DB 저장 중 오류가 발생하였습니다.");
        }

        return addedChkTmp;
    }

    @Override
    public CheckListTemplate find(long id, HttpServletRequest httpServletRequest) throws Exception {
        CheckListTemplate oldChkTmp =
            chkTmpRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 체크리스트 템플릿입니다."));

        return oldChkTmp;
    }

    @Override
    public CheckListTemplate generate(CheckListTemplate oldChkTmp) {

        return
            CheckListTemplate.builder()
            .id(oldChkTmp.getId())
            .name(oldChkTmp.getName())
            .tag(oldChkTmp.getTag())
            .relatedAcidNo(oldChkTmp.getRelatedAcidNo())
            .details(oldChkTmp.getDetails())
            .build();
    }

    @Transactional
    @Override
    public CheckListTemplate edit(CheckListTemplate chkTmp, HttpServletRequest request) throws Exception {
        CheckListTemplate oldChkTmp =
            chkTmpRepos.findById(chkTmp.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 체크리스트 템플릿입니다."));

        CheckListTemplate editedChkTmp = chkTmpRepos.save(generate(oldChkTmp));
        return editedChkTmp;
    }

    @Override
    public void remove(long id, HttpServletRequest httpServletRequest) {
        CheckListTemplate chkTmp =
            chkTmpRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 체크리스트 템플릿입니다."));

        chkTmpRepos.delete(chkTmp);
    }

    @Override
    public List<ResponseCheckListTemplateDTO> findAllByCondition(Long projectId, Long userId, String name, String tag,
            YN created_at_descended, YN views_descended, YN likes_descended, String detail_contents, Pageable pageable, HttpServletRequest request) {

        return chkTmpRepos.findAllByCondition(projectId, userId, name, tag, created_at_descended, views_descended, likes_descended, detail_contents, pageable, request);
    }

    @Override
    public ListResponse<CheckListTemplate> findAll(CheckListTemplate chkTmp, Pages pages, HttpServletRequest request) throws Exception {

        return null;
    }

}