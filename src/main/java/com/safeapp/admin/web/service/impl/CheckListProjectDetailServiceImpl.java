package com.safeapp.admin.web.service.impl;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.dto.request.RequestCheckListProjectDetailDTO;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.CheckListProjectDetail;
import com.safeapp.admin.web.repos.jpa.CheckListProjectDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.safeapp.admin.web.service.CheckListProjectDetailService;

@Service
@RequiredArgsConstructor
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

        CheckListProjectDetail chkPrjDetInfo = chkPrjDetRepos.save(chkPrjDet);
        if(Objects.isNull(chkPrjDetInfo)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "DB 저장 중 오류가 발생하였습니다.");
        }

        return chkPrjDetInfo;
    }

    @Override
    public CheckListProjectDetail find(long id, HttpServletRequest request) throws Exception {
        CheckListProjectDetail oldChkPrjDet =
            chkPrjDetRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 체크리스트 상세입니다."));

        return oldChkPrjDet;
    }

    @Override
    public CheckListProjectDetail edit(CheckListProjectDetail chkPrjDet, HttpServletRequest request) throws Exception {
        CheckListProjectDetail chkPrjDetInfo =
            chkPrjDetRepos.findById(chkPrjDet.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 체크리스트 상세입니다."));

        chkPrjDet = chkPrjDetRepos.save(generate(chkPrjDet));

        return chkPrjDet;
    }

    @Override
    public void remove(long id, HttpServletRequest httpServletRequest) {
        CheckListProjectDetail chkPrjDetInfo =
            chkPrjDetRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 체크리스트 상세입니다."));

        chkPrjDetRepos.delete(chkPrjDetInfo);
    }

    @Override
    public ListResponse<CheckListProjectDetail> findAll(CheckListProjectDetail chkPrjDet, Pages pages,
                                                        HttpServletRequest request) throws Exception {

        return null;
    }

    @Override
    public CheckListProjectDetail generate(CheckListProjectDetail chkPrjDet) {
        return
            CheckListProjectDetail.builder()
            .contents(chkPrjDet.getContents())
            .depth(chkPrjDet.getDepth())
            .id(chkPrjDet.getId())
            .isDepth(chkPrjDet.getIsDepth())
            .orders(chkPrjDet.getOrders())
            .parentDepth(chkPrjDet.getParentDepth())
            .types(chkPrjDet.getTypes())
            .build();
    }

}