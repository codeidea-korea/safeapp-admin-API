package com.safeapp.admin.web.service.impl;

import com.safeapp.admin.web.dto.request.RequestPolicyEditDTO;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.Notice;
import com.safeapp.admin.web.model.entity.Policy;
import com.safeapp.admin.web.repos.jpa.AdminRepos;
import com.safeapp.admin.web.repos.jpa.PolicyRepos;
import com.safeapp.admin.web.repos.jpa.dsl.NoticeDslRepos;
import com.safeapp.admin.web.repos.jpa.dsl.PolicyDslRepos;
import com.safeapp.admin.web.service.PolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PolicyServiceImpl implements PolicyService {

    private final PolicyRepos policyRepos;
    private final PolicyDslRepos policyDslRepos;
    private final AdminRepos adminRepos;

    @Override
    public Policy find(long id, HttpServletRequest request) throws Exception {
        Policy policy =
            policyRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 정책입니다."));

        return policy;
    }

    @Override
    public Policy toEditEntity(RequestPolicyEditDTO editDto) {
        Policy newPolicy = new Policy();

        newPolicy.setAdmin(adminRepos.findById(editDto.getAdminId()).orElse(null));
        newPolicy.setUpdatedAt(LocalDateTime.now());
        newPolicy.setContents(editDto.getContents());

        return newPolicy;
    }

    @Override
    public Policy edit(Policy newPolicy, HttpServletRequest request) throws Exception {
        Policy policy =
            policyRepos.findById(newPolicy.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 정책입니다."));

        policy.edit(newPolicy);

        Policy editedPolicy = policyRepos.save(policy);
        return editedPolicy;
    }

    @Override
    public ListResponse<Policy> findAll(Policy policy, Pages pages, HttpServletRequest request) throws Exception {
        long count = 100;
        List<Policy> list = policyDslRepos.findAll(policy, pages);

        return new ListResponse<>(count, list, pages);
    }

    @Override
    public Policy add(Policy newPolicy, HttpServletRequest request) throws Exception { return null; }

    @Override
    public Policy generate(Policy newPolicy) { return null; }

    @Override
    public void remove(long id, HttpServletRequest request) { }

}