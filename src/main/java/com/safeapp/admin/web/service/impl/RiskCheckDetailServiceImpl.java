package com.safeapp.admin.web.service.impl;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.dto.request.RequestRiskCheckDetailDTO;
import com.safeapp.admin.web.repos.jpa.RiskCheckRepository;
import com.safeapp.admin.web.repos.jpa.UserRepos;
import com.safeapp.admin.utils.DateUtil;
import com.safeapp.admin.utils.PasswordUtil;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.RiskCheckDetail;
import com.safeapp.admin.web.repos.jpa.RiskCheckDetailRepos;
import lombok.AllArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.safeapp.admin.web.service.RiskCheckDetailService;
import com.safeapp.admin.web.service.cmmn.JwtService;

@Service
@AllArgsConstructor
public class RiskCheckDetailServiceImpl implements RiskCheckDetailService {

    private final RiskCheckDetailRepos riskChkDetRepos;

    private final UserRepos userRepos;

    private final RiskCheckRepository riskCheckRepository;

    @Override
    public RiskCheckDetail toEntity(RequestRiskCheckDetailDTO addDto) throws NotFoundException {

        RiskCheckDetail detail = new RiskCheckDetail();

        detail.setRiskCheck(riskCheckRepository.findById(addDto.getRiskCheckId()).orElseThrow(() -> new NotFoundException("Input RiskCheck ID: " + addDto.getRiskCheckId())));
        detail.setContents(addDto.getContents());
        detail.setAddress(addDto.getAddress());
        detail.setAddressDetail(addDto.getAddressDetail());
        detail.setTools(addDto.getTools());
        detail.setRiskFactorType(addDto.getRiskFactorType());
        detail.setRelateLaw(addDto.getRelatedLaw());
        detail.setRelateGuide(addDto.getRelatedGuide());
        detail.setRiskType(addDto.getRiskType());
        detail.setReduceResponse(addDto.getReduceResponse());
        detail.setCheckMemo(addDto.getCheckMemo());
        detail.setDueUser(userRepos.findById(addDto.getDueUserId()).orElseThrow(() -> new NotFoundException("Input Checker ID: " + addDto.getDueUserId())));
        detail.setCheckUser(userRepos.findById(addDto.getCheckerUserId()).orElseThrow(() -> new NotFoundException("Input Checker ID: " + addDto.getDueUserId())));
        detail.setStatus(addDto.getStatus());
        detail.setParentOrders(addDto.getParentOrders());
        detail.setOrders(addDto.getOrders());
        detail.setDepth(addDto.getDepth());
        detail.setParentDepth(addDto.getParentDepth());

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
        RiskCheckDetail oldRiskChkDet =
            riskChkDetRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 위험성 평가표 상세입니다."));

        return oldRiskChkDet;
    }

    @Override
    public RiskCheckDetail edit(RiskCheckDetail oldRiskChkDet, HttpServletRequest request) throws Exception {
        RiskCheckDetail editedRiskChkDet = riskChkDetRepos.findById(oldRiskChkDet.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 위험성 평가표 상세입니다."));
        if(Objects.isNull(editedRiskChkDet)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 위험성 평가표 상세입니다.");
        }

        editedRiskChkDet = riskChkDetRepos.save(oldRiskChkDet);
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
    public RiskCheckDetail generate(RiskCheckDetail oldRiskChkDet) {

        return null;
    }

    @Override
    public ListResponse<RiskCheckDetail> findAll(RiskCheckDetail riskChkDet, Pages pages,
            HttpServletRequest request) throws Exception {

        return null;
    }

}