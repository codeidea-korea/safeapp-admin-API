package com.safeapp.admin.web.service.impl;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.dto.request.RequestCheckListProjectDetailDTO;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.CheckListProjectDetail;
import com.safeapp.admin.web.repos.jpa.CheckListProjectDetailRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.safeapp.admin.web.service.CheckListProjectDetailService;

@Service
@AllArgsConstructor
public class CheckListProjectDetailServiceImpl implements CheckListProjectDetailService {

    private final CheckListProjectDetailRepository chkPrjDetRepos;

    @Override
    public CheckListProjectDetail toEntity(RequestCheckListProjectDetailDTO addDto) {
        CheckListProjectDetail detail = new CheckListProjectDetail();

        detail.setTypes(addDto.getTypes());
        detail.setDepth(addDto.getDepth());
        detail.setIsDepth(addDto.getIsDepth());
        detail.setParentDepth(addDto.getParentDepth());
        detail.setParentOrders(addDto.getParentOrders());
        detail.setContents(addDto.getContents());
        detail.setOrders(addDto.getOrders());

        return detail;
    }

    @Transactional
    @Override
    public CheckListProjectDetail add(CheckListProjectDetail chkPrjDet, HttpServletRequest request) throws Exception {
        if(Objects.isNull(chkPrjDet)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 체크리스트 상세입니다.");
        }

        CheckListProjectDetail addedChkPrjDet = chkPrjDetRepos.save(chkPrjDet);
        if(Objects.isNull(addedChkPrjDet)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "DB 저장 중 오류가 발생하였습니다.");
        }

        return addedChkPrjDet;
    }

    @Override
    public CheckListProjectDetail find(long id, HttpServletRequest request) throws Exception {
        CheckListProjectDetail oldChkPrjDet =
            chkPrjDetRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 체크리스트 상세입니다."));

        return oldChkPrjDet;
    }

    @Override
    public CheckListProjectDetail generate(CheckListProjectDetail oldChkPrjDet) {
        return
            CheckListProjectDetail.builder()
            .contents(oldChkPrjDet.getContents())
            .depth(oldChkPrjDet.getDepth())
            .id(oldChkPrjDet.getId())
            .isDepth(oldChkPrjDet.getIsDepth())
            .orders(oldChkPrjDet.getOrders())
            .parentDepth(oldChkPrjDet.getParentDepth())
            .types(oldChkPrjDet.getTypes())
            .build();
    }

    @Override
    public CheckListProjectDetail edit(CheckListProjectDetail oldChkPrjDet, HttpServletRequest request) throws Exception {
        CheckListProjectDetail editedChkPrjDet =
            chkPrjDetRepos.findById(oldChkPrjDet.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 체크리스트 상세입니다."));
        if(Objects.isNull(editedChkPrjDet)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 체크리스트 상세입니다.");
        }

        editedChkPrjDet = chkPrjDetRepos.save(generate(oldChkPrjDet));
        return editedChkPrjDet;
    }

    @Override
    public void remove(long id, HttpServletRequest httpServletRequest) {
        CheckListProjectDetail chkPrjDet =
            chkPrjDetRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 체크리스트 상세입니다."));

        chkPrjDetRepos.delete(chkPrjDet);
    }

    @Override
    public ListResponse<CheckListProjectDetail> findAll(CheckListProjectDetail chkPrjDet, Pages pages,
            HttpServletRequest request) throws Exception {

        return null;
    }

}