package com.safeapp.admin.web.service.impl;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.utils.DateUtil;
import com.safeapp.admin.utils.PasswordUtil;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.BfPage;
import com.safeapp.admin.web.model.docs.InviteHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.safeapp.admin.web.repos.mongo.InviteHistoryRepos;
import com.safeapp.admin.web.service.InviteHistoryService;
import com.safeapp.admin.web.service.cmmn.DirectSendAPIService;
import com.safeapp.admin.web.service.cmmn.JwtService;

@Service
public class InviteHistoryServiceImpl implements InviteHistoryService {
    
    private final DirectSendAPIService directSendAPIService;

    private final InviteHistoryRepos repos;

    private final JwtService jwtService;

    private final PasswordUtil passwordUtil;

    private final DateUtil dateUtil;

    @Autowired
    public InviteHistoryServiceImpl(InviteHistoryRepos repos,
                                    JwtService jwtService, PasswordUtil passwordUtil, DateUtil dateUtil, DirectSendAPIService directSendAPIService) {

        this.repos = repos;
        this.jwtService = jwtService;
        this.passwordUtil = passwordUtil;
        this.dateUtil = dateUtil;
        this.directSendAPIService = directSendAPIService;
    }

    @Transactional
    @Override
    public InviteHistory add(InviteHistory instance, HttpServletRequest httpServletRequest) throws Exception {
        if (Objects.isNull(instance)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }
        InviteHistory savedInstance = repos.save(generate(instance));
        if (Objects.isNull(savedInstance)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "저장중 오류가 발생했습니다.");
        }
        
        Map<String, String> bodyMap = new HashMap<String, String>();
        bodyMap.put("subject", "인증번호입니다.");
        bodyMap.put("body", URLEncoder.encode("안녕하세요, 컨스퀘어에 초대합니다. <a href=\"https://safeapp.codeidea.io/login?code=" + savedInstance.getUrlData() + "\">가입하기</a>", "UTF-8"));
        bodyMap.put("name", "인증자");
        bodyMap.put("mailAddress", savedInstance.getUserMail());
        
        directSendAPIService.sendMail(instance.getUserMail(), bodyMap);
        
        return savedInstance;
    }

    @Override
    public InviteHistory edit(InviteHistory instance, HttpServletRequest httpServletRequest)
        throws Exception {
        InviteHistory savedInstance = repos.findById(instance.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "수정을 위해서는 키가 필요합니다."));
        if (Objects.isNull(savedInstance)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }
        savedInstance = repos.save(generate(instance));
        return savedInstance;
    }

    @Override
    public void remove(String id, HttpServletRequest httpServletRequest) {
        InviteHistory savedInstance = repos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "이미 삭제된 내용이거나 존재하지 않는 키입니다."));
        repos.delete(savedInstance);
    }

    @Override
    public InviteHistory find(String id, HttpServletRequest httpServletRequest) throws Exception {
        InviteHistory savedInstance = repos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "이미 삭제된 내용이거나 존재하지 않는 키입니다."));
        return savedInstance;
    }

    @Override
    public ListResponse<InviteHistory> findAll(InviteHistory instance, BfPage bfPage,
                                               HttpServletRequest httpServletRequest) throws Exception {

        Page<InviteHistory> pages = repos.findAll(bfPage.generatePageable());
        List<InviteHistory> list = new ArrayList<InviteHistory>();
        for(InviteHistory inviteHistory : pages) {
            list.add(inviteHistory);
        }
        long count = repos.count();

        return new ListResponse<InviteHistory>(list, count, bfPage);
    }

    @Override
    public InviteHistory generate(InviteHistory instance) {
        return InviteHistory.builder()
            .id(instance.getId())
            .contents(instance.getContents())
            .groupId(instance.getGroupId())
            .groupName(instance.getGroupName())
            .urlData(instance.getUrlData())
            .userMail(instance.getUserMail())
            .efectiveEndAt(instance.getEfectiveEndAt())
            .createdAt(instance.getCreatedAt() == null ? dateUtil.getThisTime() : instance.getCreatedAt())
            .build();
    }

    @Override
    public void remove(long seq, HttpServletRequest httpServletRequest) throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public InviteHistory find(long seq, HttpServletRequest httpServletRequest) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }
}
