package com.safeapp.admin.web.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.request.RequestAccidentCaseDTO;
import com.safeapp.admin.web.dto.request.RequestAccidentCaseEditDTO;
import com.safeapp.admin.web.dto.response.ResponseAccidentCaseDTO;
import com.safeapp.admin.web.dto.response.ResponseRiskCheckDTO;
import com.safeapp.admin.web.model.entity.Admins;
import com.safeapp.admin.web.model.entity.RiskCheck;
import com.safeapp.admin.web.repos.jpa.AccidentExpRepository;
import com.safeapp.admin.web.repos.jpa.AdminRepos;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.AccidentExp;
import com.safeapp.admin.web.repos.jpa.dsl.AccidentExpDslRepos;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.safeapp.admin.web.service.AccidentExpService;

@Service
@AllArgsConstructor
@Slf4j
public class AccidentExpServiceImpl implements AccidentExpService {

    private final AccidentExpRepository accExpRepos;
    private final AccidentExpDslRepos accExpDslRepos;
    private final AdminRepos adminRepos;

    @Override
    public AccidentExp toAddEntity(RequestAccidentCaseDTO addDto) {
        AccidentExp newAccExp = new AccidentExp();

        newAccExp.setCreatedAt(LocalDateTime.now());
        newAccExp.setTitle(addDto.getTitle());
        newAccExp.setAdmin(adminRepos.findById(addDto.getAdminId()).orElse(null));
        newAccExp.setTags(addDto.getTags());
        newAccExp.setName(addDto.getName());
        newAccExp.setAccidentAt(addDto.getAccidentAt());
        newAccExp.setAccidentReason(addDto.getAccidentReason());
        newAccExp.setAccidentCause(addDto.getAccidentCause());
        newAccExp.setCauseDetail(addDto.getCauseDetail());
        newAccExp.setResponse(addDto.getResponse());
        newAccExp.setImage(addDto.getImage());
        newAccExp.setViews(0);
        newAccExp.setAccidentUid(addDto.getAccidentUid());

        return newAccExp;
    }

    @Override
    @Transactional
    public AccidentExp add(AccidentExp newAccExp, HttpServletRequest request) throws Exception {
        if(Objects.isNull(newAccExp)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 사고사례입니다.");
        }

        AccidentExp addedAccExp = accExpRepos.save(newAccExp);
        if(Objects.isNull(addedAccExp)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "DB 저장 중 오류가 발생하였습니다.");
        }

        return addedAccExp;
    }

    @Override
    public AccidentExp find(long id, HttpServletRequest request) throws Exception {
        AccidentExp accExp =
            accExpRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 사고사례입니다."));

        return accExp;
    }

    @Override
    public AccidentExp toEditEntity(RequestAccidentCaseEditDTO editDto) {
        AccidentExp newAccExp = new AccidentExp();

        newAccExp.setUpdatedAt(LocalDateTime.now());
        newAccExp.setTitle(editDto.getTitle());
        newAccExp.setTags(editDto.getTags());
        newAccExp.setName(editDto.getName());
        newAccExp.setAccidentAt(editDto.getAccidentAt());
        newAccExp.setAccidentReason(editDto.getAccidentReason());
        newAccExp.setAccidentCause(editDto.getAccidentCause());
        newAccExp.setCauseDetail(editDto.getCauseDetail());
        newAccExp.setResponse(editDto.getResponse());
        newAccExp.setImage(editDto.getImage());
        newAccExp.setAccidentUid(editDto.getAccidentUid());

        return newAccExp;
    }

    @Override
    public AccidentExp edit(AccidentExp newAccExp, HttpServletRequest request) throws Exception {
        AccidentExp accExp =
            accExpRepos.findById(newAccExp.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 사고사례입니다."));

        accExp.edit(newAccExp);

        AccidentExp editedAccExp = accExpRepos.save(accExp);
        return editedAccExp;
    }

    @Override
    public void remove(long id, HttpServletRequest httpServletRequest) {
        AccidentExp accExp =
            accExpRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 사고사례입니다."));

        accExp.setDeleteYn(true);
        accExpRepos.save(accExp);
    }

    @Override
    public ListResponse<AccidentExp> findAll(AccidentExp accExp, Pages pages, HttpServletRequest request) throws Exception {
        long count = accExpDslRepos.countAll(accExp);
        List<AccidentExp> list = accExpDslRepos.findAll(accExp, pages);

        return new ListResponse<>(count, list, pages);
    }

    @Override
    public AccidentExp generate(AccidentExp newAccExp) { return null; }

}