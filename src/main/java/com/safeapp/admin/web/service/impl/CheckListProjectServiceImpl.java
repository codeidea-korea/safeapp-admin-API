package com.safeapp.admin.web.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.dto.request.RequestCheckListProjectDTO;
import com.safeapp.admin.web.dto.request.RequestCheckListProjectModifyDTO;
import com.safeapp.admin.web.dto.response.ResponseCheckListProjectDTO;
import com.safeapp.admin.web.model.entity.CheckListProject;
import com.safeapp.admin.web.model.entity.CheckListProjectDetail;
import com.safeapp.admin.web.model.entity.CheckListTemplate;
import com.safeapp.admin.web.repos.jpa.CheckListProjectRepository;
import com.safeapp.admin.web.repos.jpa.ProjectRepos;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.repos.jpa.UserRepos;
import com.safeapp.admin.web.repos.jpa.dsl.CheckListProjectDslRepos;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;
import com.safeapp.admin.web.service.CheckListProjectService;

@Service
@AllArgsConstructor
public class CheckListProjectServiceImpl implements CheckListProjectService {

    private final CheckListProjectRepository chkPrjRepos;
    private final ProjectRepos projectRepos;
    private final UserRepos userRepos;

    @Override
    public CheckListProject toEntity(RequestCheckListProjectDTO addDto) throws NotFoundException {
        CheckListProject checkListProject = new CheckListProject();

        if(addDto.getProjectId() != null) {
            checkListProject.setProject(projectRepos.findById(addDto.getProjectId()).orElseThrow(() -> new NotFoundException("Input Project ID: " + addDto.getProjectId())));
        }
        checkListProject.setUser(userRepos.findById(addDto.getUserId()).orElseThrow(() -> new NotFoundException("Input User ID: " + addDto.getUserId())));
        if(addDto.getCheckerId() != null) {
            checkListProject.setChecker(userRepos.findById(addDto.getCheckerId()).orElseThrow(() -> new NotFoundException("Input Checker ID: " + addDto.getCheckerId())));
            checkListProject.setCheckAt(LocalDateTime.now());
        }
        if(addDto.getReviewerId() != null) {
            checkListProject.setReviewer(userRepos.findById(addDto.getReviewerId()).orElseThrow(() -> new NotFoundException("Input Reviewer ID: " + addDto.getReviewerId())));
            checkListProject.setReview_at(LocalDateTime.now());
        }
        if(addDto.getApproverId() != null) {
            checkListProject.setApprover(userRepos.findById(addDto.getApproverId()).orElseThrow(() -> new NotFoundException("Input User ID: " + addDto.getUserId())));
            checkListProject.setApprove_at(LocalDateTime.now());
        }

        checkListProject.setName(addDto.getName());
        checkListProject.setVisibled(addDto.getVisibled());
        checkListProject.setTag(addDto.getTag());
        checkListProject.setRelatedAcidNo(addDto.getRelatedAcidNo());
        checkListProject.setRecheckReason(addDto.getRecheckReason());

        return checkListProject;
    }

    @Transactional
    @Override
    public CheckListProject add(CheckListProject chkPrj, HttpServletRequest request) throws Exception {
        if(Objects.isNull(chkPrj)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 체크리스트입니다.");
        }

        CheckListProject addedChkPrj = chkPrjRepos.save(chkPrj);
        if(Objects.isNull(addedChkPrj)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "DB 저장 중 오류가 발생하였습니다.");
        }

        return addedChkPrj;
    }

    @Override
    public CheckListProject find(long id, HttpServletRequest request) throws Exception {
        CheckListProject oldChkPrj =
            chkPrjRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 체크리스트입니다."));

        return oldChkPrj;
    }

    @Override
    public CheckListProject toEntityModify(RequestCheckListProjectModifyDTO modifyDto) throws NotFoundException {
        CheckListProject checkListProject = new CheckListProject();

        if(modifyDto.getProjectId() != null) {
            checkListProject.setProject(projectRepos.findById(modifyDto.getProjectId()).orElseThrow(() -> new NotFoundException("Input Project ID: " + modifyDto.getProjectId())));
        }
        checkListProject.setUser(userRepos.findById(modifyDto.getUserId()).orElseThrow(() -> new NotFoundException("Input User ID: " + modifyDto.getUserId())));
        if(modifyDto.getCheckerId() != null) {
            checkListProject.setChecker(userRepos.findById(modifyDto.getCheckerId()).orElseThrow(() -> new NotFoundException("Input Checker ID: " + modifyDto.getCheckerId())));
            checkListProject.setCheckAt(LocalDateTime.now());
        }
        if(modifyDto.getReviewerId() != null) {
            checkListProject.setReviewer(userRepos.findById(modifyDto.getReviewerId()).orElseThrow(() -> new NotFoundException("Input Reviewer ID: " + modifyDto.getReviewerId())));
            checkListProject.setReview_at(LocalDateTime.now());
        }
        if(modifyDto.getApproverId() != null) {
            checkListProject.setApprover(userRepos.findById(modifyDto.getApproverId()).orElseThrow(() -> new NotFoundException("Input User ID: " + modifyDto.getUserId())));
            checkListProject.setApprove_at(LocalDateTime.now());
        }

        checkListProject.setName(modifyDto.getName());
        checkListProject.setVisibled(modifyDto.getVisibled());
        checkListProject.setTag(modifyDto.getTag());
        checkListProject.setRelatedAcidNo(modifyDto.getRelatedAcidNo());
        checkListProject.setRecheckReason(modifyDto.getRecheckReason());

        List<CheckListProjectDetail> chkPrjDets = new ArrayList<>();
        if(modifyDto.getDetails().isEmpty() == false) {
            for(RequestCheckListProjectModifyDTO.DetailModifyDTO detailModifyDto : modifyDto.getDetails()) {
                CheckListProjectDetail chkPrjDet = new CheckListProjectDetail();

                if(detailModifyDto.getId() != null) {
                    chkPrjDet.setId(detailModifyDto.getId());
                };
                chkPrjDet.setDepth(detailModifyDto.getDepth());
                chkPrjDet.setIsDepth(detailModifyDto.getIsDepth());
                chkPrjDet.setParentDepth(detailModifyDto.getParentDepth());
                chkPrjDet.setContents(detailModifyDto.getContents());
                chkPrjDet.setOrders(detailModifyDto.getOrders());
                chkPrjDet.setParentOrders(detailModifyDto.getParentOrders());
                chkPrjDet.setTypes(detailModifyDto.getTypes());

                chkPrjDet.setCheckListProject(checkListProject);

                chkPrjDets.add(chkPrjDet);
            }
        }
        checkListProject.setCheckListProjectDetailList(chkPrjDets);

        return checkListProject;
    }

    @Transactional
    @Override
    public CheckListProject edit(CheckListProject chkPrj, HttpServletRequest request) throws Exception {
        CheckListProject oldChkPrj =
            chkPrjRepos.findById(chkPrj.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 체크리스트입니다."));

        oldChkPrj.edit(chkPrj);

        CheckListProject editedChkPrj = chkPrjRepos.save(oldChkPrj);
        return editedChkPrj;
    }

    @Override
    public void remove(long id, HttpServletRequest request) {
        CheckListProject chkPrj =
            chkPrjRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 체크리스트입니다."));

        chkPrjRepos.delete(chkPrj);
    }

    @Override
    public List<ResponseCheckListProjectDTO> findAllByCondition(String tag, YN visibled, YN created_at_descended,
            YN likes_descended, YN views_descended, Pageable pageable, HttpServletRequest request) {

        return chkPrjRepos.findAllByConditionAndOrderBy(tag, visibled, created_at_descended, likes_descended, views_descended, pageable);
    }

    @Override
    public CheckListProject generate(CheckListProject chkPrj) { return null; }

    @Override
    public ListResponse<CheckListProject> findAll(CheckListProject chkPrj, Pages pages, HttpServletRequest request) throws Exception {

        return null;
    }

}