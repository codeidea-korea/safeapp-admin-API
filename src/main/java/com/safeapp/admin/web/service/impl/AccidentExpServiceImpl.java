package com.safeapp.admin.web.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.dto.request.RequestAccidentCaseDTO;
import com.safeapp.admin.web.model.entity.Admins;
import com.safeapp.admin.web.repos.jpa.AdminRepos;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.AccidentExp;
import com.safeapp.admin.web.repos.jpa.AccidentExpRepos;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.safeapp.admin.web.repos.jpa.dsl.AccidentExpDslRepos;
import com.safeapp.admin.web.service.AccidentExpService;

@Service
@AllArgsConstructor
@Slf4j
public class AccidentExpServiceImpl implements AccidentExpService {

    private final AccidentExpRepos accExpRepos;
    private final AccidentExpDslRepos accExpDslRepos;
    private final AdminRepos adminRepos;

    @Override
    public AccidentExp toEntity(RequestAccidentCaseDTO addDto) throws NotFoundException {
        AccidentExp accidentExp = new AccidentExp();

        accidentExp.setTitle(addDto.getTitle());
        //accidentExp.setAdmin(adminRepos.findById(addDto.getAdminId()).orElseThrow(() -> new NotFoundException("Input Admin ID: " + addDto.getAdminId())));
        accidentExp.setTags(addDto.getTags());
        accidentExp.setName(addDto.getName());
        accidentExp.setAccidentAt(addDto.getAccidentAt());
        accidentExp.setAccidentUid(addDto.getAccidentUid());
        accidentExp.setAccidentReason(addDto.getAccidentReason());

        return accidentExp;
    }

    @Transactional
    @Override
    public AccidentExp add(AccidentExp accExp, HttpServletRequest request) throws Exception {
        if(Objects.isNull(accExp)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 사고사례입니다.");
        }

        AccidentExp addedAccExp = accExpRepos.save(accExp);
        if(Objects.isNull(addedAccExp)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "DB 저장 중 오류가 발생하였습니다.");
        }

        return addedAccExp;
    }

    @Override
    public AccidentExp find(long id, HttpServletRequest httpServletRequest) throws Exception {
        AccidentExp oldAccExp =
            accExpRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 사고사례입니다."));

        Optional<Admins> oldAdmin = adminRepos.findById(oldAccExp.getAdmin());
        if(oldAdmin.isPresent()) {
            oldAccExp.setAdminName(oldAdmin.get().getAdminName());
        } else {
            oldAccExp.setAdminName("삭제됨");
        }

        return oldAccExp;
    }

    @Override
    public AccidentExp generate(AccidentExp newAccExp) {
        return
            AccidentExp.builder()
            .accidentAt(newAccExp.getAccidentAt())
            .accidentCause(newAccExp.getAccidentCause())
            .accidentReason(newAccExp.getAccidentReason())
            .accidentUid(newAccExp.getAccidentUid())
            .causeDetail(newAccExp.getCauseDetail())
            .id(newAccExp.getId())
            .image(newAccExp.getImage())
            .name(newAccExp.getName())
            .response(newAccExp.getResponse())
            .tags(newAccExp.getTags())
            .title(newAccExp.getTitle())
            .admin(newAccExp.getAdmin())
            .views(newAccExp.getViews())
            .build();
    }

    @Override
    public AccidentExp edit(AccidentExp newAccExp, HttpServletRequest httpServletRequest) throws Exception {
        log.error("newAccExp: {}", newAccExp);
        AccidentExp accExp =
            accExpRepos.findById(newAccExp.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 사고사례입니다."));
        if(accExp.getDeleteYn() == true) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 사고사례입니다.");
        }
        if(Objects.isNull(accExp)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 사고사례입니다.");
        }

        accExp = accExpRepos.save(generate(newAccExp));
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
    public ListResponse<AccidentExp> findAll(AccidentExp accExp, Pages pages, HttpServletRequest request) throws Exception {

        return null;
    }

}