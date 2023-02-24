package com.safeapp.admin.web.service.impl;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.components.FileUploadProvider;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.request.RequestAccidentCaseDTO;
import com.safeapp.admin.web.dto.request.RequestAccidentCaseEditDTO;
import com.safeapp.admin.web.dto.response.ResponseAccidentCaseDTO;
import com.safeapp.admin.web.dto.response.ResponseConcernAccidentDTO;
import com.safeapp.admin.web.dto.response.ResponseRiskCheckDTO;
import com.safeapp.admin.web.model.entity.*;
import com.safeapp.admin.web.model.entity.cmmn.Files;
import com.safeapp.admin.web.repos.jpa.AccidentExpFilesRepository;
import com.safeapp.admin.web.repos.jpa.AccidentExpRepository;
import com.safeapp.admin.web.repos.jpa.AdminRepos;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.repos.jpa.ConcernAccidentExpFilesRepository;
import com.safeapp.admin.web.repos.jpa.dsl.AccidentExpDslRepos;
import com.safeapp.admin.web.service.cmmn.FileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.safeapp.admin.web.service.AccidentExpService;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
@Slf4j
public class AccidentExpServiceImpl implements AccidentExpService {

    private final AccidentExpRepository accExpRepos;
    private final AccidentExpFilesRepository accExpFileRepos;
    private final AccidentExpDslRepos accExpDslRepos;
    private final AdminRepos adminRepos;

    private final FileService fileService;

    private final FileUploadProvider fileUploadProvider;

    @Override
    public AccidentExp toAddEntity(RequestAccidentCaseDTO addDto) {
        AccidentExp newAccExp = new AccidentExp();

        newAccExp.setAdmin(adminRepos.findById(addDto.getAdminId()).orElse(null));
        newAccExp.setCreatedAt(LocalDateTime.now());
        newAccExp.setTitle(addDto.getTitle());
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
    public void addFiles(long id, List<MultipartFile> files, HttpServletRequest request) throws NotFoundException {
        AccidentExp accExp = accExpRepos.findById(id).orElseThrow(() -> new NotFoundException("존재하지 않는 사고사례입니다."));

        /*
        List<AccidentExpFiles> prevFiles = accExpFileRepos.findByAccExp(accExp);
        for(AccidentExpFiles prevFile : prevFiles) {
            fileUploadProvider.delete(new File("/home/safeapp/api" + prevFile.getUrl()));
            accExpFileRepos.delete(prevFile);
        }
        */

        if(files != null || files.isEmpty() == false) {
            for(MultipartFile file : files) {
                Files uploadFile = fileService.uploadAllowedFile(file, request);
                AccidentExpFiles resultFile =
                    AccidentExpFiles
                    .builder()
                    .accExp(accExp)
                    .url(uploadFile.getWebFileNm())
                    .build();

                accExpFileRepos.save(resultFile);
            }
        }
    }

    @Override
    public ResponseAccidentCaseDTO findAccExp(long id, HttpServletRequest request) {
        AccidentExp accExp =
            accExpRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 아차사고입니다."));

        List<AccidentExpFiles> files = accExpFileRepos.findByAccExp(accExp);

        return
            ResponseAccidentCaseDTO
            .builder()
            .accExp(accExp)
            .files(files)
            .build();
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
    public void removeFile(long id, HttpServletRequest request) {
        AccidentExpFiles accExpFile =
            accExpFileRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 사고사례 첨부파일입니다."));

        fileUploadProvider.delete(new File("/home/safeapp/api" + accExpFile.getUrl()));
        accExpFileRepos.delete(accExpFile);
    }

    @Override
    public void remove(long id, HttpServletRequest request) {
        AccidentExp accExp =
            accExpRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 사고사례입니다."));

        accExp.setDeleteYn(true);
        accExpRepos.save(accExp);
    }

    @Override
    public ListResponse<ResponseAccidentCaseDTO> findAllByCondition(AccidentExp accExp, Pages pages, HttpServletRequest request) {
        long count = accExpDslRepos.countAll(accExp);
        List<AccidentExp> list = accExpDslRepos.findAll(accExp, pages);

        List<ResponseAccidentCaseDTO> resultList = new ArrayList<>();
        for(AccidentExp each : list) {
            List<AccidentExpFiles> files = accExpFileRepos.findByAccExp(each);
            ResponseAccidentCaseDTO resAccExpDTO =
                ResponseAccidentCaseDTO
                .builder()
                .accExp(each)
                .files(files)
                .build();

            resultList.add(resAccExpDTO);
        }

        return new ListResponse<>(count, resultList, pages);
    }

    @Override
    public AccidentExp find(long id, HttpServletRequest request) throws Exception { return null; }

    @Override
    public AccidentExp generate(AccidentExp newAccExp) { return null; }

    @Override
    public ListResponse<AccidentExp> findAll(AccidentExp accExp, Pages pages, HttpServletRequest request) throws Exception {

        return null;
    }

}