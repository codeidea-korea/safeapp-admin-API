package com.safeapp.admin.web.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.utils.DateUtil;
import com.safeapp.admin.utils.PasswordUtil;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.request.RequestConcernAccidentDTO;
import com.safeapp.admin.web.dto.request.RequestConcernAccidentEditDTO;
import com.safeapp.admin.web.dto.response.ResponseAccidentCaseDTO;
import com.safeapp.admin.web.dto.response.ResponseConcernAccidentDTO;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.AccidentExp;
import com.safeapp.admin.web.model.entity.Admins;
import com.safeapp.admin.web.model.entity.ConcernAccidentExp;
import com.safeapp.admin.web.model.entity.Reports;
import com.safeapp.admin.web.repos.jpa.AdminRepos;
import com.safeapp.admin.web.repos.jpa.ConcernAccidentExpRepository;
import com.safeapp.admin.web.repos.jpa.ReportRepos;
import lombok.AllArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.safeapp.admin.web.repos.jpa.dsl.ConcernAccidentExpDslRepos;
import com.safeapp.admin.web.service.ConcernAccidentExpService;
import com.safeapp.admin.web.service.cmmn.JwtService;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Service
public class ConcernAccidentExpServiceImpl implements ConcernAccidentExpService {

    private final ConcernAccidentExpRepository conExpRepos;
    private final ConcernAccidentExpDslRepos conExpDslRepos;
    private final AdminRepos adminRepos;
    private final ReportRepos reportRepos;

    private final JwtService jwtService;

    @Override
    public ConcernAccidentExp toAddEntity(RequestConcernAccidentDTO addDto) {
        ConcernAccidentExp newConExp = new ConcernAccidentExp();

        newConExp.setAdmin(adminRepos.findById(addDto.getAdminId()).orElse(null));
        newConExp.setCreatedAt(LocalDateTime.now());
        newConExp.setTitle(addDto.getTitle());
        newConExp.setTags(addDto.getTags());
        newConExp.setName(addDto.getName());
        newConExp.setAccidentUserName(addDto.getAccidentUserName());
        newConExp.setAccidentType(addDto.getAccidentType());
        newConExp.setAccidentPlace(addDto.getAccidentPlace());
        newConExp.setCauseDetail(addDto.getCauseDetail());
        newConExp.setAccidentCause(addDto.getAccidentCause());
        newConExp.setResponse(addDto.getResponse());
        newConExp.setImage(addDto.getImage());
        newConExp.setViews(0);

        return newConExp;
    }

    @Override
    @Transactional
    public ConcernAccidentExp add(ConcernAccidentExp newConExp, HttpServletRequest request) throws Exception {
        if (Objects.isNull(newConExp)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 아차사고입니다.");
        }

        ConcernAccidentExp addedConExp = conExpRepos.save(newConExp);
        if(Objects.isNull(addedConExp)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "DB 저장 중 오류가 발생하였습니다.");
        }

        return addedConExp;
    }

    @Override
    public ConcernAccidentExp find(long id, HttpServletRequest request) throws Exception {
        ConcernAccidentExp conExp =
            conExpRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 아차사고입니다."));

        return conExp;
    }

    @Override
    public ConcernAccidentExp toEditEntity(RequestConcernAccidentEditDTO editDto) {
        ConcernAccidentExp newConExp = new ConcernAccidentExp();

        newConExp.setUpdatedAt(LocalDateTime.now());
        newConExp.setTitle(editDto.getTitle());
        newConExp.setTags(editDto.getTags());
        newConExp.setName(editDto.getName());
        newConExp.setAccidentUserName(editDto.getAccidentUserName());
        newConExp.setAccidentType(editDto.getAccidentType());
        newConExp.setAccidentPlace(editDto.getAccidentPlace());
        newConExp.setCauseDetail(editDto.getCauseDetail());
        newConExp.setAccidentCause(editDto.getAccidentCause());
        newConExp.setResponse(editDto.getResponse());
        newConExp.setImage(editDto.getImage());

        return newConExp;
    }

    @Override
    public ConcernAccidentExp edit(ConcernAccidentExp newConExp, HttpServletRequest request) throws Exception {
        ConcernAccidentExp conExp =
            conExpRepos.findById(newConExp.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 아차사고입니다."));

        conExp.edit(newConExp);

        ConcernAccidentExp editedConExp = conExpRepos.save(conExp);
        return editedConExp;
    }

    @Override
    public void remove(long id, HttpServletRequest request) {
        ConcernAccidentExp conExp =
            conExpRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 아차사고입니다."));

        conExp.setDeleteYn(true);
        conExpRepos.save(conExp);
    }

    @Override
    public ListResponse<ConcernAccidentExp> findAll(ConcernAccidentExp conExp, Pages pages, HttpServletRequest request) throws Exception {
        long count = conExpDslRepos.countAll(conExp);
        List<ConcernAccidentExp> list = conExpDslRepos.findAll(conExp, pages);

        return new ListResponse<>(count, list, pages);
    }

    @Override
    public void addReport(long id, String reportReason, HttpServletRequest request) {
        Admins admin = jwtService.getAdminInfoByToken(request);
        ConcernAccidentExp conExp =
            conExpRepos.findById(id).orElseThrow(()
            -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 아차사고입니다."));

        Reports report =
            Reports
            .builder()
            .concernAccidentExp(conExp)
            .reportAdmin(admin)
            .reportReason(reportReason)
            .build();
        reportRepos.save(report);
    }

    @Override
    public List<Reports> findReport(long id, HttpServletRequest request) throws Exception {
        List<Reports> reports = conExpDslRepos.findReport(id);

        return reports;
    }

    @Override
    public ListResponse<ConcernAccidentExp> findAllReports(ConcernAccidentExp conExp, Pages pages, HttpServletRequest request) {
        long count = conExpDslRepos.countAllReports(conExp);
        List<ConcernAccidentExp> list = conExpDslRepos.findAllReports(conExp, pages);

        return new ListResponse<>(count, list, pages);
    }

    @Override
    public ConcernAccidentExp generate(ConcernAccidentExp newConExp) { return null; }

}