package com.binoofactory.cornsqure.web.service.impl;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.binoofactory.cornsqure.web.dto.request.RequestChecklistTemplateDetailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.binoofactory.cornsqure.utils.DateUtil;
import com.binoofactory.cornsqure.utils.PasswordUtil;
import com.binoofactory.cornsqure.web.model.cmmn.BfListResponse;
import com.binoofactory.cornsqure.web.model.cmmn.BfPage;
import com.binoofactory.cornsqure.web.model.entity.ChecklistTemplateDetail;
import com.binoofactory.cornsqure.web.repos.jpa.ChecklistTemplateDetailRepos;
import com.binoofactory.cornsqure.web.service.ChecklistTemplateDetailService;
import com.binoofactory.cornsqure.web.service.cmmn.UserJwtService;

@Service
public class ChecklistTemplateDetailServiceImpl implements ChecklistTemplateDetailService {

    private final ChecklistTemplateDetailRepos repos;

    private final UserJwtService userJwtService;

    private final PasswordUtil passwordUtil;

    private final DateUtil dateUtil;

    @Autowired
    public ChecklistTemplateDetailServiceImpl(ChecklistTemplateDetailRepos repos,
        UserJwtService userJwtService, PasswordUtil passwordUtil, DateUtil dateUtil) {

        this.repos = repos;
        this.userJwtService = userJwtService;
        this.passwordUtil = passwordUtil;
        this.dateUtil = dateUtil;
    }

    @Transactional
    @Override
    public ChecklistTemplateDetail add(ChecklistTemplateDetail instance, HttpServletRequest httpServletRequest)
        throws Exception {
        if (Objects.isNull(instance)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }
        ChecklistTemplateDetail savedInstance = repos.save(instance);
        if (Objects.isNull(savedInstance)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "저장중 오류가 발생했습니다.");
        }
        return savedInstance;
    }

    @Override
    public ChecklistTemplateDetail edit(ChecklistTemplateDetail instance, HttpServletRequest httpServletRequest)
        throws Exception {
        ChecklistTemplateDetail savedInstance = repos.findById(instance.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "수정을 위해서는 키가 필요합니다."));
        if (Objects.isNull(savedInstance)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }
        savedInstance = repos.save(generate(instance));
        return savedInstance;
    }

    @Override
    public void remove(long id, HttpServletRequest httpServletRequest) {
        ChecklistTemplateDetail savedInstance = repos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "이미 삭제된 내용이거나 존재하지 않는 키입니다."));
        repos.delete(savedInstance);
    }

    @Override
    public ChecklistTemplateDetail find(long id, HttpServletRequest httpServletRequest) throws Exception {
        ChecklistTemplateDetail savedInstance = repos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "이미 삭제된 내용이거나 존재하지 않는 키입니다."));
        return savedInstance;
    }

    @Override
    public BfListResponse<ChecklistTemplateDetail> findAll(ChecklistTemplateDetail instance, BfPage bfPage,
        HttpServletRequest httpServletRequest) throws Exception {

        return null;
    }

    @Override
    public ChecklistTemplateDetail generate(ChecklistTemplateDetail instance) {
        return ChecklistTemplateDetail.builder()
            .id(instance.getId())
            .contents(instance.getContents())
            .depth(instance.getDepth())
            .izTitle(instance.getIzTitle())
            .orders(instance.getOrders())
            .parentDepth(instance.getParentDepth())
            .types(instance.getTypes())
            .build();
    }

    @Override
    public ChecklistTemplateDetail toEntity(RequestChecklistTemplateDetailDTO dto){
        ChecklistTemplateDetail detail = new ChecklistTemplateDetail();
        detail.setTypes(dto.getTypes());
        detail.setDepth(dto.getDepth());
        detail.setIzTitle(dto.getIzTitle());
        detail.setParentDepth(dto.getParentDepth());
        detail.setParentOrders(dto.getParentOrders());
        detail.setContents(dto.getContents());
        detail.setOrders(dto.getOrders());

        return detail;
    }
}
