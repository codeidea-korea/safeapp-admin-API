package com.safeapp.admin.web.service.impl;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.dto.request.RequestRiskcheckDetailDTO;
import com.safeapp.admin.web.repos.jpa.RiskCheckRepository;
import com.safeapp.admin.web.repos.jpa.UserRepos;
import com.safeapp.admin.utils.DateUtil;
import com.safeapp.admin.utils.PasswordUtil;
import com.safeapp.admin.web.model.cmmn.BfListResponse;
import com.safeapp.admin.web.model.cmmn.BfPage;
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

    private final RiskCheckDetailRepos repos;

    private final JwtService jwtService;

    private final PasswordUtil passwordUtil;

    private final DateUtil dateUtil;
    private final UserRepos userRepos;

    private final RiskCheckRepository riskCheckRepository;

    @Transactional
    @Override
    public RiskCheckDetail add(RiskCheckDetail instance, HttpServletRequest httpServletRequest) throws Exception {
        if (Objects.isNull(instance)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }
        RiskCheckDetail savedInstance = repos.save(instance);
        if (Objects.isNull(savedInstance)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "저장중 오류가 발생했습니다.");
        }
        return savedInstance;
    }

    @Override
    public RiskCheckDetail edit(RiskCheckDetail instance, HttpServletRequest httpServletRequest)
        throws Exception {
        RiskCheckDetail savedInstance = repos.findById(instance.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "수정을 위해서는 키가 필요합니다."));
        if (Objects.isNull(savedInstance)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }
        savedInstance = repos.save(instance);
        return savedInstance;
    }

    @Override
    public void remove(long id, HttpServletRequest httpServletRequest) {
        RiskCheckDetail savedInstance = repos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "이미 삭제된 내용이거나 존재하지 않는 키입니다."));
        repos.delete(savedInstance);
    }

    @Override
    public RiskCheckDetail find(long id, HttpServletRequest httpServletRequest) throws Exception {
        RiskCheckDetail savedInstance = repos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "이미 삭제된 내용이거나 존재하지 않는 키입니다."));
        return savedInstance;
    }

    @Override
    public BfListResponse<RiskCheckDetail> findAll(RiskCheckDetail instance, BfPage bfPage,
                                                   HttpServletRequest httpServletRequest) throws Exception {
        return null;
    }

    @Override
    public RiskCheckDetail generate(RiskCheckDetail instance) {
        return RiskCheckDetail.builder()
            .id(instance.getId())
            .address(instance.getAddress())
            .addressDetail(instance.getAddressDetail())
            .checkMemo(instance.getCheckMemo())

            .contents(instance.getContents())

            .reduceResponse(instance.getReduceResponse())
            .relateGuide(instance.getRelateGuide())
            .relateLaw(instance.getRelateLaw())
            .riskFactorType(instance.getRiskFactorType())
            .riskType(instance.getRiskType())
            .status(instance.getStatus())
            .tools(instance.getTools())
            .build();
    }

    @Override
    public RiskCheckDetail toEntity(RequestRiskcheckDetailDTO dto) throws NotFoundException {
        RiskCheckDetail detail = new RiskCheckDetail();
        detail.setRiskCheck(riskCheckRepository.findById(dto.getRiskCheckId()).orElseThrow(() -> new NotFoundException("riskCheck does not exist. input riskCheck id: " + dto.getRiskCheckId())));
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
        detail.setDueUser(userRepos.findById(dto.getDueUserId()).orElseThrow(() -> new NotFoundException("dueuser does not exist. input checker id: " + dto.getDueUserId())));
        detail.setCheckUser(userRepos.findById(dto.getCheckerUserId()).orElseThrow(() -> new NotFoundException("checker does not exist. input checker id: " + dto.getDueUserId())));
        detail.setStatus(dto.getStatus());
        detail.setParentOrders(dto.getParentOrders());
        detail.setOrders(dto.getOrders());
        detail.setDepth(dto.getDepth());
        detail.setParentDepth(dto.getParentDepth());
        return detail;
    }
}
