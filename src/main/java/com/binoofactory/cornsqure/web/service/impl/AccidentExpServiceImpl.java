package com.binoofactory.cornsqure.web.service.impl;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.binoofactory.cornsqure.web.dto.request.RequestAccidentCaseDTO;
import com.binoofactory.cornsqure.web.repos.jpa.UserRepos;
import lombok.AllArgsConstructor;
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
import com.binoofactory.cornsqure.web.model.entity.AccidentExp;
import com.binoofactory.cornsqure.web.repos.jpa.AccidentExpRepos;
import com.binoofactory.cornsqure.web.repos.jpa.dsl.AccidentExpDslRepos;
import com.binoofactory.cornsqure.web.service.AccidentExpService;
import com.binoofactory.cornsqure.web.service.cmmn.UserJwtService;

@Service
@AllArgsConstructor
public class AccidentExpServiceImpl implements AccidentExpService {

    private final AccidentExpRepos repos;

    private final AccidentExpDslRepos dslRepos;

    private final UserRepos userRepos;

    private final UserJwtService userJwtService;

    private final PasswordUtil passwordUtil;

    private final DateUtil dateUtil;

    @Transactional
    @Override
    public AccidentExp add(AccidentExp instance, HttpServletRequest httpServletRequest) throws Exception {
        if (Objects.isNull(instance)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }
        AccidentExp savedInstance = repos.save(instance);
        if (Objects.isNull(savedInstance)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "저장중 오류가 발생했습니다.");
        }
        return savedInstance;
    }

    @Override
    public AccidentExp edit(AccidentExp instance, HttpServletRequest httpServletRequest) throws Exception {
        AccidentExp savedInstance = repos.findById(instance.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "수정을 위해서는 키가 필요합니다."));
        if (Objects.isNull(savedInstance)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }
        savedInstance = repos.save(generate(instance));
        return savedInstance;
    }

    @Override
    public void remove(long id, HttpServletRequest httpServletRequest) {
        AccidentExp savedInstance = repos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "이미 삭제된 내용이거나 존재하지 않는 키입니다."));
        repos.delete(savedInstance);
    }

    @Override
    public AccidentExp find(long id, HttpServletRequest httpServletRequest) throws Exception {
        AccidentExp savedInstance = repos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "이미 삭제된 내용이거나 존재하지 않는 키입니다."));
        return savedInstance;
    }

    @Override
    public BfListResponse<AccidentExp> findAll(AccidentExp instance, BfPage bfPage,
        HttpServletRequest httpServletRequest) throws Exception {

        List<AccidentExp> list = dslRepos.findAll(instance, bfPage);
        long count = dslRepos.countAll(instance);

        return new BfListResponse<AccidentExp>(list, count, bfPage);
    }

    @Override
    public AccidentExp generate(AccidentExp instance) {
        return AccidentExp.builder()
            .accidentAt(instance.getAccidentAt())
            .accidentCause(instance.getAccidentCause())
            .accidentReason(instance.getAccidentReason())
            .accidentUid(instance.getAccidentUid())
            .causeDetail(instance.getCauseDetail())
            .id(instance.getId())
            .image(instance.getImage())
            .name(instance.getName())
            .response(instance.getResponse())
            .tags(instance.getTags())
            .title(instance.getTitle())
            .userId(instance.getUser().getId())
            .views(instance.getViews())
            .build();
    }

    @Override
    public AccidentExp toEntity(RequestAccidentCaseDTO dto) throws NotFoundException {
        AccidentExp accidentExp = new AccidentExp();
        accidentExp.setTitle(dto.getTitle());
        accidentExp.setUser(userRepos.findById(dto.getUserId()).orElseThrow(() -> new NotFoundException("dueuser does not exist. input checker id: " + dto.getUserId())));
        accidentExp.setTags(dto.getTags());
        accidentExp.setName(dto.getName());
        accidentExp.setAccidentAt(dto.getAccidentAt());
        accidentExp.setAccidentUid(dto.getAccidentUid());
        accidentExp.setAccidentReason(dto.getAccidentReason());

        return accidentExp;
    }
}
