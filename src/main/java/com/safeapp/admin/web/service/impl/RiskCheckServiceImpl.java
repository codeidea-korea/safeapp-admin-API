package com.safeapp.admin.web.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.data.StatusType;
import com.safeapp.admin.web.dto.request.RequestRiskCheckDTO;
import com.safeapp.admin.web.dto.response.ResponseCheckListProjectDTO;
import com.safeapp.admin.web.dto.response.ResponseRiskCheckDTO;
import com.safeapp.admin.web.model.entity.Admins;
import com.safeapp.admin.web.model.entity.CheckListProject;
import com.safeapp.admin.web.repos.jpa.AccidentExpRepos;
import com.safeapp.admin.web.repos.jpa.ProjectRepos;
import com.safeapp.admin.web.repos.jpa.RiskCheckRepository;
import com.safeapp.admin.web.repos.jpa.UserRepos;
import com.safeapp.admin.utils.DateUtil;
import com.safeapp.admin.utils.PasswordUtil;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.docs.LikeHistory;
import com.safeapp.admin.web.model.entity.RiskCheck;
import lombok.AllArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.safeapp.admin.web.repos.jpa.dsl.RiskCheckDslRepos;
import com.safeapp.admin.web.repos.mongo.LikeHistoryRepos;
import com.safeapp.admin.web.service.RiskCheckService;
import com.safeapp.admin.web.service.cmmn.JwtService;

@Service
@AllArgsConstructor
public class RiskCheckServiceImpl implements RiskCheckService {

    private final RiskCheckRepository riskChkRepos;
    private final ProjectRepos prjRepos;
    private final UserRepos userRepos;

    @Override
    public RiskCheck toEntity(RequestRiskCheckDTO addDto) throws NotFoundException{
        RiskCheck riskCheck = new RiskCheck();

        riskCheck.setUser(userRepos.findById(addDto.getUserId()).orElseThrow(() -> new NotFoundException("Input User ID: " + addDto.getUserId())));
        riskCheck.setName(addDto.getName());
        riskCheck.setVisibled(addDto.getVisibled());
        riskCheck.setTag(addDto.getTag());
        riskCheck.setWorkStartAt(addDto.getWorkStartAt());
        riskCheck.setWorkEndAt(addDto.getWorkEndAt());
        riskCheck.setInstructWork(addDto.getInstructWork());
        riskCheck.setInstructDetail(addDto.getInstructDetail());
        riskCheck.setEtcRiskMemo(addDto.getEtcRiskMemo());
        riskCheck.setRecheckReason(addDto.getRecheckReason());
        riskCheck.setRelatedAcidNo(addDto.getRelatedAcidNo());

        if(addDto.getProjectId() != null) {
            riskCheck.setProject(prjRepos.findById(addDto.getProjectId()).orElseThrow(() -> new NotFoundException("Input Project ID: " + addDto.getProjectId())));
        }
        if(addDto.getCheckerId() != null) {
            riskCheck.setChecker(userRepos.findById(addDto.getCheckerId()).orElseThrow(() -> new NotFoundException("Input Checker ID: " + addDto.getCheckerId())));
        }
        if(addDto.getApproverId() != null) {
            riskCheck.setApprover(userRepos.findById(addDto.getApproverId()).orElseThrow(() -> new NotFoundException("Input User ID: " + addDto.getApproverId())));
        }
        if(addDto.getReviewer1Id() != null) {
            riskCheck.setReviewer1(userRepos.findById(addDto.getReviewer1Id()).orElseThrow(() -> new NotFoundException("Input User ID: " + addDto.getReviewer1Id())));
        }
        if(addDto.getReviewer2Id() != null) {
            riskCheck.setReviewer2(userRepos.findById(addDto.getReviewer2Id()).orElseThrow(() -> new NotFoundException("Input User ID: " + addDto.getReviewer2Id())));
        }
        if(addDto.getReviewer3Id() != null) {
            riskCheck.setReviewer3(userRepos.findById(addDto.getReviewer1Id()).orElseThrow(() -> new NotFoundException("Input User ID: " + addDto.getReviewer3Id())));
        }
        if(addDto.getCheckUserId() != null) {
            riskCheck.setCheckUser(userRepos.findById(addDto.getCheckUserId()).orElseThrow(() -> new NotFoundException("Input User ID: " + addDto.getCheckUserId())));
        }
        if(addDto.getDueUserId() != null) {
            riskCheck.setDueUser(userRepos.findById(addDto.getDueUserId()).orElseThrow(() -> new NotFoundException("Input User ID: " + addDto.getDueUserId())));
        }

        return riskCheck;
    }

    @Transactional
    @Override
    public RiskCheck add(RiskCheck riskChk, HttpServletRequest request) throws Exception {
        if(Objects.isNull(riskChk)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 위험성 평가표입니다.");
        }

        RiskCheck addedRiskChk = riskChkRepos.save(riskChk);
        if(Objects.isNull(addedRiskChk)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "DB 저장 중 오류가 발생하였습니다.");
        }

        return addedRiskChk;
    }

    @Override
    public RiskCheck find(long id, HttpServletRequest httpServletRequest) throws Exception {
        RiskCheck oldRiskChk = riskChkRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 위험성 평가표입니다."));

        return oldRiskChk;
    }

    @Transactional
    @Override
    public RiskCheck edit(RiskCheck riskChk, HttpServletRequest httpServletRequest) throws Exception {

        RiskCheck oldRiskChk = riskChkRepos.findById(riskChk.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 위험성 평가표입니다."));
        if(Objects.isNull(oldRiskChk)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 위험성 평가표입니다.");
        }

        oldRiskChk.edit(riskChk);

        RiskCheck editedRiskChk = riskChkRepos.save(oldRiskChk);
        return editedRiskChk;
    }

    @Override
    public void remove(long id, HttpServletRequest httpServletRequest) {
        RiskCheck riskChk = riskChkRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 위험성 평가표입니다."));

        riskChk.setDeleteYn(true);
        riskChkRepos.save(riskChk);
    }

    @Override
    public Long countAllByCondition(String keyword, String userName, String phoneNo, YN visibled,
                                    LocalDateTime createdAtStart, LocalDateTime createdAtEnd) {

        return Long.parseLong("0");
        //return riskChkRepos.countAllByCondition(keyword, userName, phoneNo, visibled, createdAtStart, createdAtEnd);
    }

    @Override
    public List<ResponseRiskCheckDTO> findAllByConditionAndOrderBy(String keyword, String userName, String phoneNo,
            YN visibled, LocalDateTime createdAtStart, LocalDateTime createdAtEnd, YN createdAtDesc, YN likesDesc, YN viewsDesc,
            int pageNo, int pageSize, HttpServletRequest request) {

        return null;
        /*
        List<RiskCheck> list =
            riskChkRepos.findAllByConditionAndOrderBy(keyword, userName, phoneNo,
            visibled, createdAtStart, createdAtEnd, createdAtDesc, likesDesc, viewsDesc,
            pageNo, pageSize);

        List<ResponseRiskCheckDTO> resultList = new ArrayList<>();
        for(RiskCheck riskChk : list) {
            List<String> contents = riskChkRepos.findContentsByRiskChecktId(riskChk.getId());
            ResponseRiskCheckDTO result =
                ResponseRiskCheckDTO
                .builder()
                .riskCheck(riskChk)
                .contents(contents)
                .build();

            resultList.add(result);
        }

        return resultList;
        */
    }

    @Override
    public RiskCheck generate(RiskCheck oldRiskChk) { return null; }

    @Override
    public ListResponse<RiskCheck> findAll(RiskCheck riskChk, Pages pages, HttpServletRequest request) throws Exception {

        return null;
    }

}