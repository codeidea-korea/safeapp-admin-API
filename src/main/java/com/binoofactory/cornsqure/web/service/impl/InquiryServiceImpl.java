package com.binoofactory.cornsqure.web.service.impl;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.binoofactory.cornsqure.utils.DateUtil;
import com.binoofactory.cornsqure.utils.PasswordUtil;
import com.binoofactory.cornsqure.web.data.YN;
import com.binoofactory.cornsqure.web.model.cmmn.BfListResponse;
import com.binoofactory.cornsqure.web.model.cmmn.BfPage;
import com.binoofactory.cornsqure.web.model.entity.Inquiry;
import com.binoofactory.cornsqure.web.repos.jpa.InquiryRepos;
import com.binoofactory.cornsqure.web.repos.jpa.dsl.InquiryDslRepos;
import com.binoofactory.cornsqure.web.service.InquiryService;
import com.binoofactory.cornsqure.web.service.cmmn.UserJwtService;

@Service
public class InquiryServiceImpl implements InquiryService {

    private final InquiryRepos repos;

    private final InquiryDslRepos dslRepos;

    private final UserJwtService userJwtService;

    private final PasswordUtil passwordUtil;

    private final DateUtil dateUtil;

    @Autowired
    public InquiryServiceImpl(InquiryRepos repos, InquiryDslRepos dslRepos,
        UserJwtService userJwtService, PasswordUtil passwordUtil, DateUtil dateUtil) {

        this.repos = repos;
        this.dslRepos = dslRepos;
        this.userJwtService = userJwtService;
        this.passwordUtil = passwordUtil;
        this.dateUtil = dateUtil;
    }

    @Transactional
    @Override
    public Inquiry add(Inquiry instance, HttpServletRequest httpServletRequest) throws Exception {
        if (Objects.isNull(instance)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }
        Inquiry savedInstance = repos.save(generate(instance));
        if (Objects.isNull(savedInstance)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "저장중 오류가 발생했습니다.");
        }
        return savedInstance;
    }

    @Override
    public Inquiry edit(Inquiry instance, HttpServletRequest httpServletRequest)
        throws Exception {
        Inquiry savedInstance = repos.findById(instance.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "수정을 위해서는 키가 필요합니다."));
        if (Objects.isNull(savedInstance)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }
        savedInstance = repos.save(generate(instance));
        return savedInstance;
    }

    @Override
    public void remove(long id, HttpServletRequest httpServletRequest) {
        Inquiry savedInstance = repos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "이미 삭제된 내용이거나 존재하지 않는 키입니다."));
        repos.delete(savedInstance);
    }

    @Override
    public Inquiry find(long id, HttpServletRequest httpServletRequest) throws Exception {
        Inquiry savedInstance = repos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "이미 삭제된 내용이거나 존재하지 않는 키입니다."));
        return savedInstance;
    }

    @Override
    public BfListResponse<Inquiry> findAll(Inquiry instance, BfPage bfPage,
        HttpServletRequest httpServletRequest) throws Exception {

        List<Inquiry> list = dslRepos.findAll(instance, bfPage);
        long count = dslRepos.countAll(instance);

        return new BfListResponse<Inquiry>(list, count, bfPage);
    }

    @Override
    public Inquiry generate(Inquiry instance) {
        return Inquiry.builder()
            .id(instance.getId())
            .answer(instance.getAnswer())

            .attachment(instance.getAttachment())
            .contents(instance.getContents())
            .createdAt(instance.getCreatedAt() == null ? dateUtil.getThisTime() : instance.getCreatedAt())
            .isAnswer(instance.getIsAnswer() == null ? YN.N : instance.getIsAnswer())
            .serviceName(instance.getServiceName())
            .title(instance.getTitle())
            .build();
    }
}
