package com.safeapp.admin.web.service.impl;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.dto.request.RequestRiskCheckDetailDTO;
import com.safeapp.admin.web.model.entity.CheckListProject;
import com.safeapp.admin.web.model.entity.CheckListProjectDetail;
import com.safeapp.admin.web.model.entity.RiskCheck;
import com.safeapp.admin.web.repos.jpa.RiskCheckRepository;
import com.safeapp.admin.web.repos.jpa.UserRepos;
import com.safeapp.admin.utils.DateUtil;
import com.safeapp.admin.utils.PasswordUtil;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.RiskCheckDetail;
import com.safeapp.admin.web.repos.jpa.RiskCheckDetailRepos;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.safeapp.admin.web.service.RiskCheckDetailService;
import com.safeapp.admin.web.service.cmmn.JwtService;

@Service
@AllArgsConstructor
@Slf4j
public class RiskCheckDetailServiceImpl implements RiskCheckDetailService {

    private final RiskCheckRepository riskChkRepos;
    private final RiskCheckDetailRepos riskChkDetRepos;
    private final RiskCheckRepository riskCheckRepository;
    private final UserRepos userRepos;

    @Override
    public RiskCheckDetail toEntity(RequestRiskCheckDetailDTO dto) throws NotFoundException {

        RiskCheckDetail detail = new RiskCheckDetail();

        detail.setRiskCheck(riskCheckRepository.findById(dto.getRiskCheckId()).orElseThrow(() -> new NotFoundException("Input RiskCheck ID: " + dto.getRiskCheckId())));
        detail.setContents(dto.getContents());
        detail.setAddress(dto.getAddress());
        detail.setAddressDetail(dto.getAddressDetail());
        detail.setTools(dto.getTools());
        detail.setRiskFactorType(dto.getRiskFactorType());
        detail.setRelateLaw(dto.getRelatedLaw());
        detail.setRelateGuide(dto.getRelatedGuide());
        detail.setRiskType(dto.getRiskType());
        detail.setReduceResponse(dto.getReduceResponse());
        detail.setCheckMemo(dto.getCheckMemo());
        if(dto.getDueUserId() != null) {
            detail.setDueUser(userRepos.findById(dto.getDueUserId()).orElseThrow(() -> new NotFoundException("Input Due ID: " + dto.getDueUserId())));
        }
        if(dto.getCheckerUserId() != null) {
            detail.setCheckUser(userRepos.findById(dto.getCheckerUserId()).orElseThrow(() -> new NotFoundException("Input Checker ID: " + dto.getCheckerUserId())));
        }
        detail.setStatus(dto.getStatus());
        detail.setParentOrders(dto.getParentOrders());
        detail.setOrders(dto.getOrders());
        detail.setDepth(dto.getDepth());
        detail.setParentDepth(dto.getParentDepth());

        return detail;
    }

    @Transactional
    @Override
    public RiskCheckDetail add(RiskCheckDetail riskChkDet, HttpServletRequest request) throws Exception {
        if(Objects.isNull(riskChkDet)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 위험성 평가표 상세입니다.");
        }

        RiskCheckDetail addedRiskChkDet = riskChkDetRepos.save(riskChkDet);
        if(Objects.isNull(addedRiskChkDet)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "DB 저장 중 오류가 발생하였습니다.");
        }

        return addedRiskChkDet;
    }

    @Override
    public RiskCheckDetail find(long id, HttpServletRequest httpServletRequest) throws Exception {
        RiskCheckDetail riskChkDet =
            riskChkDetRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 위험성 평가표 상세입니다."));

        return riskChkDet;
    }

    @Override
    public RiskCheckDetail edit(RiskCheckDetail newRiskChkDet, HttpServletRequest request) throws Exception {
        RiskCheckDetail editedRiskChkDet =
            riskChkDetRepos.findById(newRiskChkDet.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 위험성 평가표 상세입니다."));
        if(Objects.isNull(editedRiskChkDet)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 위험성 평가표 상세입니다.");
        }

        editedRiskChkDet = riskChkDetRepos.save(newRiskChkDet);
        return editedRiskChkDet;
    }

    @Override
    public void remove(long id, HttpServletRequest httpServletRequest) {
        RiskCheckDetail riskChkDet =
            riskChkDetRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 위험성 평가표 상세입니다."));

        riskChkDet.setDeleteYn(true);
        riskChkDetRepos.save(riskChkDet);
    }

    @Override
    public void removeAll(long id, HttpServletRequest request) {
        RiskCheck riskChk =
            riskChkRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 위험성 평가표입니다."));

        List<RiskCheckDetail> riskChkDetList = riskChkDetRepos.findAllByRiskCheck(riskChk);
        riskChkDetList.forEach(each -> {
            riskChkDetRepos.delete(each);
        });
    }

    @Override
    public RiskCheckDetail generate(RiskCheckDetail newRiskChkDet) {

        return null;
    }

    @Override
    public ListResponse<RiskCheckDetail> findAll(RiskCheckDetail riskChkDet, Pages pages,
            HttpServletRequest request) throws Exception {

        return null;
    }

}