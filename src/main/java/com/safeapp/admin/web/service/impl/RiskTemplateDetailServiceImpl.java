package com.safeapp.admin.web.service.impl;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.dto.request.RequestRiskTemplateDetailDTO;
import com.safeapp.admin.web.repos.jpa.RiskTemplateRepository;
import com.safeapp.admin.web.repos.jpa.UserRepos;
import com.safeapp.admin.utils.DateUtil;
import com.safeapp.admin.utils.PasswordUtil;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.BfPage;
import com.safeapp.admin.web.model.entity.RiskTemplateDetail;
import com.safeapp.admin.web.repos.jpa.RiskTemplateDetailRepos;
import lombok.AllArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.safeapp.admin.web.service.RiskTemplateDetailService;
import com.safeapp.admin.web.service.cmmn.JwtService;

@AllArgsConstructor
@Service
public class RiskTemplateDetailServiceImpl implements RiskTemplateDetailService {

    private final RiskTemplateDetailRepos repos;

    private final RiskTemplateRepository templateRepository;
    private final UserRepos userRepos;

    private final JwtService jwtService;

    private final PasswordUtil passwordUtil;

    private final DateUtil dateUtil;


    @Transactional
    @Override
    public RiskTemplateDetail add(RiskTemplateDetail instance, HttpServletRequest httpServletRequest) throws Exception {
        if (Objects.isNull(instance)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }
        RiskTemplateDetail savedInstance = repos.save(instance);
        if (Objects.isNull(savedInstance)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "저장중 오류가 발생했습니다.");
        }
        return savedInstance;
    }

    @Override
    public RiskTemplateDetail edit(RiskTemplateDetail instance, HttpServletRequest httpServletRequest)
        throws Exception {
        RiskTemplateDetail savedInstance = repos.findById(instance.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "수정을 위해서는 키가 필요합니다."));
        if (Objects.isNull(savedInstance)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }
        savedInstance = repos.save(generate(instance));
        return savedInstance;
    }

    @Override
    public void remove(long id, HttpServletRequest httpServletRequest) {
        RiskTemplateDetail savedInstance = repos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "이미 삭제된 내용이거나 존재하지 않는 키입니다."));
        repos.delete(savedInstance);
    }

    @Override
    public RiskTemplateDetail find(long id, HttpServletRequest httpServletRequest) throws Exception {
        RiskTemplateDetail savedInstance = repos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "이미 삭제된 내용이거나 존재하지 않는 키입니다."));
        return savedInstance;
    }

    @Override
    public ListResponse<RiskTemplateDetail> findAll(RiskTemplateDetail instance, BfPage bfPage,
                                                    HttpServletRequest httpServletRequest) throws Exception {
        return null;
    }

    @Override
    public RiskTemplateDetail generate(RiskTemplateDetail instance) {
        return RiskTemplateDetail.builder()
            .id(instance.getId())
            .address(instance.getAddress())
            .addressDetail(instance.getAddressDetail())
            .checkMemo(instance.getCheckMemo())
            .contents(instance.getContents())
            .dueUserId(instance.getDueUserId())
            .reduceResponse(instance.getReduceResponse())
            .relateGuide(instance.getRelateGuide())
            .relateLaw(instance.getRelateLaw())
            .riskFactorType(instance.getRiskFactorType())
            .riskType(instance.getRiskType())
            .tools(instance.getTools())
            .build();
    }

    @Override
    public RiskTemplateDetail toEntity(RequestRiskTemplateDetailDTO dto) throws NotFoundException{
        RiskTemplateDetail detail = new RiskTemplateDetail();
        detail.setRiskTemplate(templateRepository.findById(dto.getRiskTemplateId()).orElseThrow(() -> new NotFoundException("riskTemplate does not exist. input riskCheck id: " + dto.getRiskTemplateId())));
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
        detail.setParentOrders(dto.getParentOrders());
        detail.setOrders(dto.getOrders());
        detail.setDepth(dto.getDepth());
        detail.setParentDepth(dto.getParentDepth());
        return detail;
    }
}
