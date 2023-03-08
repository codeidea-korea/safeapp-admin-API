package com.safeapp.admin.web.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.dto.request.RequestCheckListProjectDTO;
import com.safeapp.admin.web.dto.request.RequestCheckListProjectEditDTO;
import com.safeapp.admin.web.dto.response.ResponseCheckListProjectDTO;
import com.safeapp.admin.web.model.entity.CheckListProject;
import com.safeapp.admin.web.model.entity.CheckListProjectDetail;
import com.safeapp.admin.web.repos.jpa.CheckListProjectRepository;
import com.safeapp.admin.web.repos.jpa.ProjectRepos;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.repos.jpa.UserRepos;
import lombok.AllArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;
import com.safeapp.admin.web.service.CheckListProjectService;

@Service
@AllArgsConstructor
public class CheckListProjectServiceImpl implements CheckListProjectService {

    private final CheckListProjectRepository chkPrjRepos;
    private final ProjectRepos prjRepos;
    private final UserRepos userRepos;

    @Override
    public CheckListProject toAddEntity(RequestCheckListProjectDTO addDto) throws NotFoundException {
        CheckListProject newChkPrj = new CheckListProject();

        newChkPrj.setUser(userRepos.findById(addDto.getUserId()).orElseThrow(() -> new NotFoundException("Input User ID: " + addDto.getUserId())));
        newChkPrj.setName(addDto.getName());
        newChkPrj.setVisibled(addDto.getVisibled());
        newChkPrj.setTag(addDto.getTag());
        newChkPrj.setRelatedAcidNo(addDto.getRelatedAcidNo());
        newChkPrj.setRecheckReason(addDto.getRecheckReason());

        if(addDto.getProjectId() != null) {
            newChkPrj.setProject(prjRepos.findById(addDto.getProjectId()).orElseThrow(() -> new NotFoundException("Input Project ID: " + addDto.getProjectId())));
        }
        if(addDto.getCheckerId() != null) {
            newChkPrj.setChecker(userRepos.findById(addDto.getCheckerId()).orElseThrow(() -> new NotFoundException("Input Checker ID: " + addDto.getCheckerId())));
            newChkPrj.setCheckAt(LocalDateTime.now());
        }
        if(addDto.getReviewerId() != null) {
            newChkPrj.setReviewer(userRepos.findById(addDto.getReviewerId()).orElseThrow(() -> new NotFoundException("Input Reviewer ID: " + addDto.getReviewerId())));
            newChkPrj.setReviewAt(LocalDateTime.now());
        }
        if(addDto.getApproverId() != null) {
            newChkPrj.setApprover(userRepos.findById(addDto.getApproverId()).orElseThrow(() -> new NotFoundException("Input User ID: " + addDto.getUserId())));
            newChkPrj.setApproveAt(LocalDateTime.now());
        }

        return newChkPrj;
    }

    @Transactional
    @Override
    public CheckListProject add(CheckListProject newChkPrj, HttpServletRequest request) throws Exception {
        if(Objects.isNull(newChkPrj)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 체크리스트입니다.");
        }

        CheckListProject addedChkPrj = chkPrjRepos.save(newChkPrj);
        if(Objects.isNull(addedChkPrj)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "DB 저장 중 오류가 발생하였습니다.");
        }

        return addedChkPrj;
    }

    @Override
    public CheckListProject find(long id, HttpServletRequest request) throws Exception {
        CheckListProject chkPrj =
            chkPrjRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 체크리스트입니다."));

        return chkPrj;
    }

    @Override
    public CheckListProject toEditEntity(RequestCheckListProjectEditDTO editDto) throws NotFoundException {
        CheckListProject newChkPrj = new CheckListProject();

        newChkPrj.setUser(userRepos.findById(editDto.getUserId()).orElseThrow(() -> new NotFoundException("Input User ID: " + editDto.getUserId())));
        newChkPrj.setName(editDto.getName());
        newChkPrj.setVisibled(editDto.getVisibled());
        newChkPrj.setTag(editDto.getTag());
        newChkPrj.setRelatedAcidNo(editDto.getRelatedAcidNo());
        newChkPrj.setRecheckReason(editDto.getRecheckReason());

        if(editDto.getProjectId() != null) {
            newChkPrj.setProject(prjRepos.findById(editDto.getProjectId()).orElseThrow(() -> new NotFoundException("Input Project ID: " + editDto.getProjectId())));
        }
        if(editDto.getCheckerId() != null) {
            newChkPrj.setChecker(userRepos.findById(editDto.getCheckerId()).orElseThrow(() -> new NotFoundException("Input Checker ID: " + editDto.getCheckerId())));
            newChkPrj.setCheckAt(LocalDateTime.now());
        }
        if(editDto.getReviewerId() != null) {
            newChkPrj.setReviewer(userRepos.findById(editDto.getReviewerId()).orElseThrow(() -> new NotFoundException("Input Reviewer ID: " + editDto.getReviewerId())));
            newChkPrj.setReviewAt(LocalDateTime.now());
        }
        if(editDto.getApproverId() != null) {
            newChkPrj.setApprover(userRepos.findById(editDto.getApproverId()).orElseThrow(() -> new NotFoundException("Input User ID: " + editDto.getUserId())));
            newChkPrj.setApproveAt(LocalDateTime.now());
        }

        List<CheckListProjectDetail> newChkPrjDets = new ArrayList<>();
        if(editDto.getDetails().isEmpty() == false) {
            for(RequestCheckListProjectEditDTO.DetailEditDTO detailEditDto : editDto.getDetails()) {
                CheckListProjectDetail newChkPrjDet = new CheckListProjectDetail();

                newChkPrjDet.setDepth(detailEditDto.getDepth());
                newChkPrjDet.setIsDepth(detailEditDto.getIsDepth());
                newChkPrjDet.setParentDepth(detailEditDto.getParentDepth());
                newChkPrjDet.setContents(detailEditDto.getContents());
                newChkPrjDet.setOrders(detailEditDto.getOrders());
                newChkPrjDet.setParentOrders(detailEditDto.getParentOrders());
                newChkPrjDet.setTypes(detailEditDto.getTypes());
                newChkPrjDet.setChecklistProject(newChkPrj);

                if(detailEditDto.getId() != null) {
                    newChkPrjDet.setId(detailEditDto.getId());
                }

                newChkPrjDets.add(newChkPrjDet);
            }
        }
        newChkPrj.setCheckListProjectDetailList(newChkPrjDets);

        return newChkPrj;
    }

    @Transactional
    @Override
    public CheckListProject edit(CheckListProject newChkPrj, HttpServletRequest request) throws Exception {
        CheckListProject chkPrj =
            chkPrjRepos.findById(newChkPrj.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 체크리스트입니다."));

        chkPrj.edit(newChkPrj);

        CheckListProject editedChkPrj = chkPrjRepos.save(chkPrj);
        return editedChkPrj;
    }

    @Override
    public void remove(long id, HttpServletRequest request) {
        CheckListProject chkPrj =
            chkPrjRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 체크리스트입니다."));

        chkPrj.setDeleteYn(true);
        chkPrjRepos.save(chkPrj);
    }

    @Override
    public Long countAllByCondition(String keyword, String userName, String phoneNo, YN visibled,
            LocalDateTime createdAtStart, LocalDateTime createdAtEnd) {

        return chkPrjRepos.countAllByCondition(keyword, userName, phoneNo, visibled, createdAtStart, createdAtEnd);
    }

    @Override
    public List<ResponseCheckListProjectDTO> findAllByConditionAndOrderBy(String keyword, String userName, String phoneNo,
            YN visibled, LocalDateTime createdAtStart, LocalDateTime createdAtEnd, YN createdAtDesc, YN likesDesc, YN viewsDesc,
            int pageNo, int pageSize, HttpServletRequest request) {

        List<CheckListProject> list =
                chkPrjRepos.findAllByConditionAndOrderBy(keyword, userName, phoneNo,
                visibled, createdAtStart, createdAtEnd, createdAtDesc, likesDesc, viewsDesc,
                pageNo, pageSize);

        List<ResponseCheckListProjectDTO> resultList = new ArrayList<>();
        for(CheckListProject chkPrj : list) {
            List<String> contents = chkPrjRepos.findContentsByCheckListId(chkPrj.getId());

            ResponseCheckListProjectDTO result =
                ResponseCheckListProjectDTO
                .builder()
                .checkListProject(chkPrj)
                .contents(contents)
                .build();

            resultList.add(result);
        }

        return resultList;
    }

    @Override
    public CheckListProject generate(CheckListProject newChkPrj) { return null; }

    @Override
    public ListResponse<CheckListProject> findAll(CheckListProject chkPrj, Pages pages, HttpServletRequest request) throws Exception {

        return null;
    }

}