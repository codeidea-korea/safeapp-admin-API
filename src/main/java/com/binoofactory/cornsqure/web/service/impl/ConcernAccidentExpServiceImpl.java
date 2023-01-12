package com.binoofactory.cornsqure.web.service.impl;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.binoofactory.cornsqure.web.data.YN;
import com.binoofactory.cornsqure.web.dto.response.ResponseChecklistTemplateDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.binoofactory.cornsqure.utils.DateUtil;
import com.binoofactory.cornsqure.utils.PasswordUtil;
import com.binoofactory.cornsqure.web.model.cmmn.BfListResponse;
import com.binoofactory.cornsqure.web.model.cmmn.BfPage;
import com.binoofactory.cornsqure.web.model.entity.ConcernAccidentExp;
import com.binoofactory.cornsqure.web.repos.jpa.ConcernAccidentExpRepos;
import com.binoofactory.cornsqure.web.repos.jpa.dsl.ConcernAccidentExpDslRepos;
import com.binoofactory.cornsqure.web.service.ConcernAccidentExpService;
import com.binoofactory.cornsqure.web.service.cmmn.UserJwtService;

@AllArgsConstructor
@Service
public class ConcernAccidentExpServiceImpl implements ConcernAccidentExpService {

    private final ConcernAccidentExpRepos repos;

    private final ConcernAccidentExpDslRepos dslRepos;

    private final UserJwtService userJwtService;

    private final PasswordUtil passwordUtil;

    private final DateUtil dateUtil;

    @Transactional
    @Override
    public ConcernAccidentExp add(ConcernAccidentExp instance, HttpServletRequest httpServletRequest) throws Exception {
        if (Objects.isNull(instance)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }
        ConcernAccidentExp savedInstance = repos.save(generate(instance));
        if (Objects.isNull(savedInstance)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "저장중 오류가 발생했습니다.");
        }
        return savedInstance;
    }

    @Override
    public ConcernAccidentExp edit(ConcernAccidentExp instance, HttpServletRequest httpServletRequest)
        throws Exception {
        ConcernAccidentExp savedInstance = repos.findById(instance.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "수정을 위해서는 키가 필요합니다."));
        if (Objects.isNull(savedInstance)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }
        savedInstance = repos.save(generate(instance));
        return savedInstance;
    }

    @Override
    public void remove(long id, HttpServletRequest httpServletRequest) {
        ConcernAccidentExp savedInstance = repos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "이미 삭제된 내용이거나 존재하지 않는 키입니다."));
        repos.delete(savedInstance);
    }

    @Override
    public ConcernAccidentExp find(long id, HttpServletRequest httpServletRequest) throws Exception {
        ConcernAccidentExp savedInstance = repos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "이미 삭제된 내용이거나 존재하지 않는 키입니다."));
        return savedInstance;
    }

    @Override
    public BfListResponse<ConcernAccidentExp> findAll(ConcernAccidentExp instance, BfPage bfPage,
        HttpServletRequest httpServletRequest) throws Exception {

        List<ConcernAccidentExp> list = dslRepos.findAll(instance, bfPage);
        long count = dslRepos.countAll(instance);

        return new BfListResponse<ConcernAccidentExp>(list, count, bfPage);
    }

    @Override
    public ConcernAccidentExp generate(ConcernAccidentExp instance) {
        return ConcernAccidentExp.builder()
            .id(instance.getId())
            .accidentAt(instance.getAccidentAt())
            .accidentCause(instance.getAccidentCause())
            .accidentReason(instance.getAccidentReason())
            .accidentType(instance.getAccidentType())
            .accidentUserName(instance.getAccidentUserName())
            .causeDetail(instance.getCauseDetail())
            .createdAt(instance.getCreatedAt() == null ? dateUtil.getThisTime() : instance.getCreatedAt())
            .image(instance.getImage())
            .name(instance.getName())
            .response(instance.getResponse())
            .tags(instance.getTags())
            .title(instance.getTitle())
            .userId(instance.getUserId())
            .views(instance.getViews())
            .build();
    }
}
