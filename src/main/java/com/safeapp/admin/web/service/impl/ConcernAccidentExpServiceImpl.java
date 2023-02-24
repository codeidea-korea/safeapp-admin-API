package com.safeapp.admin.web.service.impl;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.utils.DateUtil;
import com.safeapp.admin.utils.PasswordUtil;
import com.safeapp.admin.web.components.FileUploadProvider;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.request.RequestConcernAccidentDTO;
import com.safeapp.admin.web.dto.request.RequestConcernAccidentEditDTO;
import com.safeapp.admin.web.dto.response.ResponseAccidentCaseDTO;
import com.safeapp.admin.web.dto.response.ResponseConcernAccidentDTO;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.*;
import com.safeapp.admin.web.model.entity.cmmn.Files;
import com.safeapp.admin.web.repos.jpa.*;
import com.safeapp.admin.web.service.cmmn.FileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ConcernAccidentExpServiceImpl implements ConcernAccidentExpService {

    private final ConcernAccidentExpRepository conExpRepos;
    private final ConcernAccidentExpFilesRepository conExpFileRepos;
    private final ConcernAccidentExpDslRepos conExpDslRepos;
    private final AdminRepos adminRepos;
    private final UserRepos userRepos;
    private final ReportRepos reportRepos;

    private final FileService fileService;
    private final JwtService jwtService;

    private final FileUploadProvider fileUploadProvider;

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
        newConExp.setAccidentReason(addDto.getAccidentReason());
        newConExp.setResponse(addDto.getResponse());
        newConExp.setImage(null);
        newConExp.setViews(0);
        newConExp.setAccidentCause(addDto.getAccidentCause());
        newConExp.setAccidentAt(addDto.getAccidentAt());
        newConExp.setUserId(Long.parseLong("0"));
        newConExp.setUser(userRepos.findById(Long.parseLong("2")).orElse(null));

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
    public void addFiles(Long id, List<MultipartFile> files, HttpServletRequest request) throws Exception {
        ConcernAccidentExp conExp = conExpRepos.findById(id).orElseThrow(() -> new NotFoundException("존재하지 않는 아차사고입니다."));

        /*
        List<ConcernAccidentExpFiles> prevFiles = conExpFileRepos.findByConExp(conExp);
        for(ConcernAccidentExpFiles prevFile : prevFiles) {
            fileUploadProvider.delete(new File("/home/safeapp/api" + prevFile.getUrl()));
            conExpFileRepos.delete(prevFile);
        }
        */

        if(files != null || files.isEmpty() == false) {
            for(MultipartFile file : files) {
                Files uploadFile = fileService.uploadAllowedFile(file, request);
                ConcernAccidentExpFiles resultFile =
                    ConcernAccidentExpFiles
                    .builder()
                    .conExp(conExp)
                    .url(uploadFile.getWebFileNm())
                    .build();

                conExpFileRepos.save(resultFile);
            }
        }
    }

    @Override
    public ResponseConcernAccidentDTO findConExp(long id, HttpServletRequest request) {
        ConcernAccidentExp conExp =
            conExpRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 아차사고입니다."));

        List<ConcernAccidentExpFiles> files = conExpFileRepos.findByConExp(conExp);

        return
            ResponseConcernAccidentDTO
            .builder()
            .conExp(conExp)
            .files(files)
            .build();
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
        newConExp.setAccidentReason(editDto.getAccidentReason());
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
    public void removeFile(long id, HttpServletRequest request) {
        ConcernAccidentExpFiles conExpFile =
            conExpFileRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 아차사고 첨부파일입니다."));

        fileUploadProvider.delete(new File("/home/safeapp/api" + conExpFile.getUrl()));
        conExpFileRepos.delete(conExpFile);
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
    public ListResponse<ResponseConcernAccidentDTO> findAllByCondition(ConcernAccidentExp conExp, Pages pages, HttpServletRequest request) {
        long count = conExpDslRepos.countAll(conExp);
        List<ConcernAccidentExp> list = conExpDslRepos.findAll(conExp, pages);

        List<ResponseConcernAccidentDTO> resultList = new ArrayList<>();
        for(ConcernAccidentExp each : list) {
            List<ConcernAccidentExpFiles> files = conExpFileRepos.findByConExp(each);
            ResponseConcernAccidentDTO resConExpDTO =
                ResponseConcernAccidentDTO
                .builder()
                .conExp(each)
                .files(files)
                .build();

            resultList.add(resConExpDTO);
        }

        return new ListResponse<>(count, resultList, pages);
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
    public List<Reports> findReport(long id, HttpServletRequest request) {
        List<Reports> reports = conExpDslRepos.findReport(id);

        return reports;
    }

    @Override
    public void removeReport(long id, HttpServletRequest request) {
        Reports report =
            reportRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 아차사고 신고입니다."));

        report.setDeleteYn(true);
        reportRepos.save(report);
    }

    @Override
    public ListResponse<ConcernAccidentExp> findAllReports(ConcernAccidentExp conExp, Pages pages, HttpServletRequest request) {
        long count = conExpDslRepos.countAllReports(conExp);
        List<ConcernAccidentExp> list = conExpDslRepos.findAllReports(conExp, pages);

        return new ListResponse<>(count, list, pages);
    }

    @Override
    public ConcernAccidentExp find(long id, HttpServletRequest request) throws Exception { return null; }

    @Override
    public ConcernAccidentExp generate(ConcernAccidentExp newConExp) { return null; }

    @Override
    public ListResponse<ConcernAccidentExp> findAll(ConcernAccidentExp conExp, Pages pages, HttpServletRequest request) throws Exception {

        return null;
    }

}