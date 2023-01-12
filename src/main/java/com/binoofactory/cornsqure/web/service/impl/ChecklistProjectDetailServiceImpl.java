package com.binoofactory.cornsqure.web.service.impl;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.binoofactory.cornsqure.web.dto.request.RequestChecklistProjectDTO;
import com.binoofactory.cornsqure.web.dto.request.RequestChecklistProjectDetailDTO;
import com.binoofactory.cornsqure.web.model.entity.ChecklistProject;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.binoofactory.cornsqure.utils.DateUtil;
import com.binoofactory.cornsqure.utils.PasswordUtil;
import com.binoofactory.cornsqure.web.model.cmmn.BfListResponse;
import com.binoofactory.cornsqure.web.model.cmmn.BfPage;
import com.binoofactory.cornsqure.web.model.entity.ChecklistProjectDetail;
import com.binoofactory.cornsqure.web.repos.jpa.ChecklistProjectDetailRepository;
import com.binoofactory.cornsqure.web.service.ChecklistProjectDetailService;
import com.binoofactory.cornsqure.web.service.cmmn.UserJwtService;

@Service
public class ChecklistProjectDetailServiceImpl implements ChecklistProjectDetailService {

    private final ChecklistProjectDetailRepository repos;

    private final UserJwtService userJwtService;

    private final PasswordUtil passwordUtil;

    private final DateUtil dateUtil;

    @Autowired
    public ChecklistProjectDetailServiceImpl(ChecklistProjectDetailRepository repos,
        UserJwtService userJwtService, PasswordUtil passwordUtil, DateUtil dateUtil) {

        this.repos = repos;
        this.userJwtService = userJwtService;
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
    public BfListResponse<ChecklistProjectDetail> findAll(ChecklistProjectDetail instance, BfPage bfPage,
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
