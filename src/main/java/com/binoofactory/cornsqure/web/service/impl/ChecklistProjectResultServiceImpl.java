package com.binoofactory.cornsqure.web.service.impl;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.binoofactory.cornsqure.web.dto.request.RequestChecklistProjectResultDTO;
import com.binoofactory.cornsqure.web.model.entity.ChecklistProjectDetail;
import com.binoofactory.cornsqure.web.repos.jpa.ChecklistProjectDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.binoofactory.cornsqure.utils.DateUtil;
import com.binoofactory.cornsqure.utils.PasswordUtil;
import com.binoofactory.cornsqure.web.model.cmmn.BfListResponse;
import com.binoofactory.cornsqure.web.model.cmmn.BfPage;
import com.binoofactory.cornsqure.web.model.entity.ChecklistProjectResult;
import com.binoofactory.cornsqure.web.repos.jpa.ChecklistProjectResultRepos;
import com.binoofactory.cornsqure.web.service.ChecklistProjectResultService;
import com.binoofactory.cornsqure.web.service.cmmn.UserJwtService;

@Service
public class ChecklistProjectResultServiceImpl implements ChecklistProjectResultService {

    private final ChecklistProjectResultRepos repos;

    private final UserJwtService userJwtService;

    private final PasswordUtil passwordUtil;

    private final DateUtil dateUtil;

    private final ChecklistProjectDetailRepository checklistProjectDetailRepository;

    @Autowired
    public ChecklistProjectResultServiceImpl(ChecklistProjectResultRepos repos,
                                             UserJwtService userJwtService, PasswordUtil passwordUtil, DateUtil dateUtil, ChecklistProjectDetailRepository checklistProjectDetailRepository) {

        this.repos = repos;
        this.userJwtService = userJwtService;
        this.passwordUtil = passwordUtil;
        this.dateUtil = dateUtil;
        this.checklistProjectDetailRepository = checklistProjectDetailRepository;
    }

    @Transactional
    @Override
    public ChecklistProjectResult add(ChecklistProjectResult instance, HttpServletRequest httpServletRequest)
        throws Exception {
        if (Objects.isNull(instance)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }
        ChecklistProjectResult savedInstance = repos.save(instance);
        if (Objects.isNull(savedInstance)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "저장중 오류가 발생했습니다.");
        }
        return savedInstance;
    }

    @Override
    public ChecklistProjectResult edit(ChecklistProjectResult instance, HttpServletRequest httpServletRequest)
        throws Exception {
        ChecklistProjectResult savedInstance = repos.findById(instance.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "수정을 위해서는 키가 필요합니다."));
        if (Objects.isNull(savedInstance)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }
        savedInstance = repos.save(instance);
        return savedInstance;
    }

    @Override
    public void remove(long id, HttpServletRequest httpServletRequest) {
        ChecklistProjectResult savedInstance = repos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "이미 삭제된 내용이거나 존재하지 않는 키입니다."));
        repos.delete(savedInstance);
    }

    @Override
    public ChecklistProjectResult find(long id, HttpServletRequest httpServletRequest) throws Exception {
        ChecklistProjectResult savedInstance = repos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "이미 삭제된 내용이거나 존재하지 않는 키입니다."));
        return savedInstance;
    }

    @Override
    public BfListResponse<ChecklistProjectResult> findAll(ChecklistProjectResult instance, BfPage bfPage,
        HttpServletRequest httpServletRequest) throws Exception {
        return null;
    }

    @Override
    public ChecklistProjectResult generate(ChecklistProjectResult instance) {
        return ChecklistProjectResult.builder()
            .id(instance.getId())
            .check(instance.getCheckYn())
            .detailId(instance.getChecklistProjectDetail().getId())
            .memo(instance.getMemo())
            .status(instance.getStatus())
            .build();
    }

    @Override
    public ChecklistProjectResult toEntity(RequestChecklistProjectResultDTO dto) {

        ChecklistProjectResult checklistProjectResult = new ChecklistProjectResult();
        ChecklistProjectDetail detail = checklistProjectDetailRepository.findById(dto.getDetailId()).orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "detailid에 맞는 값이 존재하지 않습니다.."));

        checklistProjectResult.setChecklistProjectDetail(detail);
        checklistProjectResult.setStatus(dto.getStatus());
        checklistProjectResult.setMemo(dto.getMemo());
        checklistProjectResult.setCheckYn(dto.getCheckYn());
        return checklistProjectResult;
    }
}
