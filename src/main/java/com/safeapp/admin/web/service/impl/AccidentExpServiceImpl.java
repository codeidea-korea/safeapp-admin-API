package com.safeapp.admin.web.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.request.RequestAccidentCaseDTO;
import com.safeapp.admin.web.dto.response.ResponseAccidentCaseDTO;
import com.safeapp.admin.web.dto.response.ResponseRiskCheckDTO;
import com.safeapp.admin.web.model.entity.Admins;
import com.safeapp.admin.web.model.entity.RiskCheck;
import com.safeapp.admin.web.repos.jpa.AccidentExpRepository;
import com.safeapp.admin.web.repos.jpa.AdminRepos;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.AccidentExp;
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
    private final AdminRepos adminRepos;

    @Override
    public AccidentExp toAddEntity(RequestAccidentCaseDTO addDto) {
        AccidentExp accidentExp = new AccidentExp();

        accidentExp.setCreatedAt(LocalDateTime.now());
        accidentExp.setTitle(addDto.getTitle());
        accidentExp.setAdmin(adminRepos.findById(addDto.getAdminId()).orElse(null));
        accidentExp.setTags(addDto.getTags());
        accidentExp.setName(addDto.getName());
        accidentExp.setAccidentAt(addDto.getAccidentAt());
        accidentExp.setAccidentReason(addDto.getAccidentReason());
        accidentExp.setAccidentCause(addDto.getAccidentCause());
        accidentExp.setCauseDetail(addDto.getCauseDetail());
        accidentExp.setResponse(addDto.getResponse());
        accidentExp.setImage(addDto.getImage());
        accidentExp.setViews(0);
        accidentExp.setAccidentUid(addDto.getAccidentUid());

        return accidentExp;
    }

    @Transactional
    @Override
    public AccidentExp add(AccidentExp newAccExp, HttpServletRequest request) throws Exception {
        if(Objects.isNull(newAccExp)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 사고사례입니다.");
        }

        AccidentExp accExp = accExpRepos.save(newAccExp);
        if(Objects.isNull(accExp)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "DB 저장 중 오류가 발생하였습니다.");
        }

        return accExp;
    }

    @Override
    public AccidentExp find(long id, HttpServletRequest httpServletRequest) throws Exception {
        AccidentExp accExp =
            accExpRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 사고사례입니다."));

        return accExp;
    }

    @Override
    public AccidentExp toEditEntity(RequestAccidentCaseDTO editDto) {
        AccidentExp newAccExp = new AccidentExp();

        newAccExp.setCreatedAt(editDto.getCreatedAt());
        newAccExp.setTitle(editDto.getTitle());
        newAccExp.setAdmin(adminRepos.findById(editDto.getAdminId()).orElse(null));
        newAccExp.setTags(editDto.getTags());
        newAccExp.setName(editDto.getName());
        newAccExp.setAccidentAt(editDto.getAccidentAt());
        newAccExp.setAccidentReason(editDto.getAccidentReason());
        newAccExp.setAccidentCause(editDto.getAccidentCause());
        newAccExp.setCauseDetail(editDto.getCauseDetail());
        newAccExp.setResponse(editDto.getResponse());
        newAccExp.setImage(editDto.getImage());
        newAccExp.setViews(editDto.getViews());
        newAccExp.setAccidentUid(editDto.getAccidentUid());

        return newAccExp;
    }

    @Override
    public AccidentExp edit(AccidentExp newAccExp, HttpServletRequest httpServletRequest) throws Exception {
        AccidentExp accExp =
            accExpRepos.findById(newAccExp.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 사고사례입니다."));

        accExp.edit(newAccExp);

        accExp = accExpRepos.save(newAccExp);
        log.error("accExp: {}", accExp);
        return accExp;
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
    public Long countAllByCondition(String keyword, String adminName, String phoneNo,
        LocalDateTime createdAtStart, LocalDateTime createdAtEnd) {

        return accExpRepos.countAllByCondition(keyword, adminName, phoneNo, createdAtStart, createdAtEnd);
    }

    @Override
    public List<ResponseAccidentCaseDTO> findAllByConditionAndOrderBy(String keyword, String adminName, String phoneNo,
            LocalDateTime createdAtStart, LocalDateTime createdAtEnd, YN createdAtDesc, YN viewsDesc,
            int pageNo, int pageSize, HttpServletRequest request) {

        List<AccidentExp> list =
            accExpRepos.findAllByConditionAndOrderBy(keyword, adminName, phoneNo,
            createdAtStart, createdAtEnd, createdAtDesc, viewsDesc, pageNo, pageSize);

        List<ResponseAccidentCaseDTO> resultList = new ArrayList<>();
        for(AccidentExp accExp : list) {
            ResponseAccidentCaseDTO result =
                ResponseAccidentCaseDTO
                .builder()
                .accExp(accExp)
                .build();

            resultList.add(result);
        }

        return resultList;
    }

    @Override
    public AccidentExp generate(AccidentExp newAccExp) { return null; }

    @Override
    public ListResponse<AccidentExp> findAll(AccidentExp accExp, Pages pages, HttpServletRequest request) throws Exception {

        return null;
    }

}