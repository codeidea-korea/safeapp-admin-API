package com.safeapp.admin.web.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.data.StatusType;
import com.safeapp.admin.web.dto.request.RequestRiskCheckDTO;
import com.safeapp.admin.web.dto.response.ResponseAccidentCaseDTO;
import com.safeapp.admin.web.dto.response.ResponseCheckListProjectDTO;
import com.safeapp.admin.web.dto.response.ResponseRiskCheckDTO;
import com.safeapp.admin.web.model.entity.*;
import com.safeapp.admin.web.repos.jpa.ProjectRepos;
import com.safeapp.admin.web.repos.jpa.RiskCheckRepository;
import com.safeapp.admin.web.repos.jpa.UserRepos;
import com.safeapp.admin.utils.DateUtil;
import com.safeapp.admin.utils.PasswordUtil;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.repos.jpa.dsl.RiskChkDslRepos;
import lombok.AllArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;
import com.safeapp.admin.web.service.RiskCheckService;
import com.safeapp.admin.web.service.cmmn.JwtService;

@Service
@AllArgsConstructor
public class RiskCheckServiceImpl implements RiskCheckService {

    private final RiskCheckRepository riskChkRepos;
    private final RiskChkDslRepos riskChkDslRepos;
    private final ProjectRepos prjRepos;
    private final UserRepos userRepos;

    @Override
    public RiskCheck toEntity(RequestRiskCheckDTO dto) throws NotFoundException{
        RiskCheck newRiskChk = new RiskCheck();

        newRiskChk.setUser(userRepos.findById(dto.getUserId()).orElseThrow(() -> new NotFoundException("Input User ID: " + dto.getUserId())));
        newRiskChk.setName(dto.getName());
        newRiskChk.setVisibled(dto.getVisibled());
        newRiskChk.setTag(dto.getTag());
        newRiskChk.setWorkStartAt(dto.getWorkStartAt());
        newRiskChk.setWorkEndAt(dto.getWorkEndAt());
        newRiskChk.setInstructWork(dto.getInstructWork());
        newRiskChk.setInstructDetail(dto.getInstructDetail());
        newRiskChk.setEtcRiskMemo(dto.getEtcRiskMemo());
        newRiskChk.setRecheckReason(dto.getRecheckReason());
        newRiskChk.setRelatedAcidNo(dto.getRelatedAcidNo());

        if(dto.getProjectId() != null) {
            newRiskChk.setProject(prjRepos.findById(dto.getProjectId()).orElseThrow(() -> new NotFoundException("Input Project ID: " + dto.getProjectId())));
        }
        if(dto.getCheckerId() != null) {
            newRiskChk.setChecker(userRepos.findById(dto.getCheckerId()).orElseThrow(() -> new NotFoundException("Input Checker ID: " + dto.getCheckerId())));
        }
        if(dto.getApproverId() != null) {
            newRiskChk.setApprover(userRepos.findById(dto.getApproverId()).orElseThrow(() -> new NotFoundException("Input User ID: " + dto.getApproverId())));
        }
        if(dto.getReviewer1Id() != null) {
            newRiskChk.setReviewer1(userRepos.findById(dto.getReviewer1Id()).orElseThrow(() -> new NotFoundException("Input User ID: " + dto.getReviewer1Id())));
        }
        if(dto.getReviewer2Id() != null) {
            newRiskChk.setReviewer2(userRepos.findById(dto.getReviewer2Id()).orElseThrow(() -> new NotFoundException("Input User ID: " + dto.getReviewer2Id())));
        }
        if(dto.getReviewer3Id() != null) {
            newRiskChk.setReviewer3(userRepos.findById(dto.getReviewer1Id()).orElseThrow(() -> new NotFoundException("Input User ID: " + dto.getReviewer3Id())));
        }
        if(dto.getCheckUserId() != null) {
            newRiskChk.setCheckUser(userRepos.findById(dto.getCheckUserId()).orElseThrow(() -> new NotFoundException("Input User ID: " + dto.getCheckUserId())));
        }
        if(dto.getDueUserId() != null) {
            newRiskChk.setDueUser(userRepos.findById(dto.getDueUserId()).orElseThrow(() -> new NotFoundException("Input User ID: " + dto.getDueUserId())));
        }

        return newRiskChk;
    }

    @Transactional
    @Override
    public RiskCheck add(RiskCheck newRiskChk, HttpServletRequest request) throws Exception {
        if(Objects.isNull(newRiskChk)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 위험성 평가표입니다.");
        }

        newRiskChk.setViews(0);
        newRiskChk.setLikes(0);

        RiskCheck addedRiskChk = riskChkRepos.save(newRiskChk);
        if(Objects.isNull(addedRiskChk)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "DB 저장 중 오류가 발생하였습니다.");
        }

        return addedRiskChk;
    }

    @Override
    public RiskCheck find(long id, HttpServletRequest request) throws Exception {
        RiskCheck riskChk = riskChkRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 위험성 평가표입니다."));

        if(riskChk.getViews() != null) {
            riskChk.setViews(riskChk.getViews() + 1);
        } else {
            riskChk.setViews(1);
        }

        riskChk = riskChkRepos.save(riskChk);

        return riskChk;
    }

    @Transactional
    @Override
    public RiskCheck edit(RiskCheck newRiskChk, HttpServletRequest request) throws Exception {
        RiskCheck riskChk = riskChkRepos.findById(newRiskChk.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 위험성 평가표입니다."));
        if(Objects.isNull(riskChk)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 위험성 평가표입니다.");
        }

        riskChk.edit(newRiskChk);

        RiskCheck editedRiskChk = riskChkRepos.save(riskChk);
        return editedRiskChk;
    }

    @Override
    public void remove(long id, HttpServletRequest httpServletRequest) {
        RiskCheck riskChk =
            riskChkRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 위험성 평가표입니다."));

        riskChk.setDeleteYn(true);
        riskChkRepos.save(riskChk);
    }

    @Override
    public ListResponse<ResponseRiskCheckDTO> findAllByCondition(RiskCheck riskChk, Pages pages, HttpServletRequest request) {
        long count = riskChkDslRepos.countAll(riskChk);
        List<RiskCheck> list = riskChkDslRepos.findAll(riskChk, pages);

        List<ResponseRiskCheckDTO> resultList = new ArrayList<>();
        for(RiskCheck each : list) {
            List<String> contents = riskChkRepos.findContentsByRiskCheckId(each.getId());

            ResponseRiskCheckDTO resRiskChkDTO =
                ResponseRiskCheckDTO
                .builder()
                .riskCheck(each)
                .contents(contents)
                .build();

            resultList.add(resRiskChkDTO);
        }

        return new ListResponse<>(count, resultList, pages);
    }

    @Override
    public RiskCheck generate(RiskCheck newRiskChk) { return null; }

    @Override
    public ListResponse<RiskCheck> findAll(RiskCheck riskChk, Pages pages, HttpServletRequest request) throws Exception {

        return null;
    }

}