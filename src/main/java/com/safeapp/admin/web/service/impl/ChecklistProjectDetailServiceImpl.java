package com.safeapp.admin.web.service.impl;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.dto.request.RequestChecklistProjectDetailDTO;
import com.safeapp.admin.utils.DateUtil;
import com.safeapp.admin.utils.PasswordUtil;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.ChecklistProjectDetail;
import com.safeapp.admin.web.repos.jpa.ChecklistProjectDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.safeapp.admin.web.service.ChecklistProjectDetailService;
import com.safeapp.admin.web.service.cmmn.JwtService;

@Service
public class ChecklistProjectDetailServiceImpl implements ChecklistProjectDetailService {

    private final ChecklistProjectDetailRepository repos;

    private final JwtService jwtService;

    private final PasswordUtil passwordUtil;

    private final DateUtil dateUtil;

    @Autowired
    public ChecklistProjectDetailServiceImpl(ChecklistProjectDetailRepository repos,
                                             JwtService jwtService, PasswordUtil passwordUtil, DateUtil dateUtil) {

        this.repos = repos;
        this.jwtService = jwtService;
        this.passwordUtil = passwordUtil;
        this.dateUtil = dateUtil;
    }

    @Transactional
    @Override
    public ChecklistProjectDetail add(ChecklistProjectDetail instance, HttpServletRequest httpServletRequest)
        throws Exception {
        if (Objects.isNull(instance)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }
        ChecklistProjectDetail savedInstance = repos.save(instance);
        if (Objects.isNull(savedInstance)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "저장중 오류가 발생했습니다.");
        }
        return savedInstance;
    }

    @Override
    public ChecklistProjectDetail edit(ChecklistProjectDetail instance, HttpServletRequest httpServletRequest)
        throws Exception {
        ChecklistProjectDetail savedInstance = repos.findById(instance.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "수정을 위해서는 키가 필요합니다."));
        if (Objects.isNull(savedInstance)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }
        savedInstance = repos.save(generate(instance));
        return savedInstance;
    }

    @Override
    public void remove(long id, HttpServletRequest httpServletRequest) {
        ChecklistProjectDetail savedInstance = repos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "이미 삭제된 내용이거나 존재하지 않는 키입니다."));
        repos.delete(savedInstance);
    }

    @Override
    public ChecklistProjectDetail find(long id, HttpServletRequest httpServletRequest) throws Exception {
        ChecklistProjectDetail savedInstance = repos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "이미 삭제된 내용이거나 존재하지 않는 키입니다."));
        return savedInstance;
    }

    @Override
    public ListResponse<ChecklistProjectDetail> findAll(ChecklistProjectDetail instance, Pages bfPage,
                                                        HttpServletRequest httpServletRequest) throws Exception {
        return null;
    }

    @Override
    public ChecklistProjectDetail toEntity(RequestChecklistProjectDetailDTO dto) {
            ChecklistProjectDetail detail = new ChecklistProjectDetail();
            detail.setTypes(dto.getTypes());
            detail.setDepth(dto.getDepth());
            detail.setIzTitle(dto.getIzTitle());
            detail.setParentDepth(dto.getParentDepth());
            detail.setParentOrders(dto.getParentOrders());
            detail.setContents(dto.getContents());
            detail.setOrders(dto.getOrders());

        return detail;
    }

    @Override
    public ChecklistProjectDetail generate(ChecklistProjectDetail instance) {
        return ChecklistProjectDetail.builder()
            .contents(instance.getContents())
            .depth(instance.getDepth())
            .id(instance.getId())
            .izTitle(instance.getIzTitle())
            .orders(instance.getOrders())
            .parentDepth(instance.getParentDepth())
            .types(instance.getTypes())
            .build();
    }
}
