package com.safeapp.admin.web.service.impl;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.dto.request.RequestCheckListTemplateDetailDTO;
import com.safeapp.admin.utils.DateUtil;
import com.safeapp.admin.utils.PasswordUtil;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.CheckListTemplateDetail;
import com.safeapp.admin.web.repos.jpa.CheckListTemplateDetailRepos;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.safeapp.admin.web.service.CheckListTemplateDetailService;
import com.safeapp.admin.web.service.cmmn.JwtService;

@Service
@AllArgsConstructor
public class CheckListTemplateDetailServiceImpl implements CheckListTemplateDetailService {

    private final CheckListTemplateDetailRepos chkTmpDetRepos;

    @Override
    public CheckListTemplateDetail toEntity(RequestCheckListTemplateDetailDTO addDto) {
        CheckListTemplateDetail detail = new CheckListTemplateDetail();

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
    public CheckListTemplateDetail add(CheckListTemplateDetail chkTmpDet, HttpServletRequest request) throws Exception {
        if(Objects.isNull(chkTmpDet)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 체크리스트 템플릿 상세입니다.");
        }

        CheckListTemplateDetail addedChkTmpDet = chkTmpDetRepos.save(chkTmpDet);
        if (Objects.isNull(addedChkTmpDet)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "DB 저장 중 오류가 발생하였습니다.");
        }

        return addedChkTmpDet;
    }

    @Override
    public CheckListTemplateDetail find(long id, HttpServletRequest request) throws Exception {
        CheckListTemplateDetail oldChkPrjDet =
            chkTmpDetRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 체크리스트 템플릿 상세입니다."));

        return oldChkPrjDet;
    }

    @Override
    public CheckListTemplateDetail generate(CheckListTemplateDetail oldChkTmpDet) {
        return
            CheckListTemplateDetail.builder()
            .id(oldChkTmpDet.getId())
            .contents(oldChkTmpDet.getContents())
            .depth(oldChkTmpDet.getDepth())
            .isDepth(oldChkTmpDet.getIsDepth())
            .orders(oldChkTmpDet.getOrders())
            .parentDepth(oldChkTmpDet.getParentDepth())
            .types(oldChkTmpDet.getTypes())
            .build();
    }

    @Override
    public CheckListTemplateDetail edit(CheckListTemplateDetail oldChkTmpDet, HttpServletRequest request)
            throws Exception {

        CheckListTemplateDetail editedChkTmpDet =
            chkTmpDetRepos.findById(oldChkTmpDet.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 체크리스트 템플릿 상세입니다."));
        if(Objects.isNull(editedChkTmpDet)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 체크리스트 템플릿 상세입니다.");
        }

        editedChkTmpDet = chkTmpDetRepos.save(generate(oldChkTmpDet));
        return editedChkTmpDet;
    }

    @Override
    public void remove(long id, HttpServletRequest httpServletRequest) {
        CheckListTemplateDetail chkTmpDet =
            chkTmpDetRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 체크리스트 템플릿 상세입니다."));

        chkTmpDetRepos.delete(chkTmpDet);
    }

    @Override
    public ListResponse<CheckListTemplateDetail> findAll(CheckListTemplateDetail chkTmpDet, Pages pages,
            HttpServletRequest request) throws Exception {

        return null;
    }

}