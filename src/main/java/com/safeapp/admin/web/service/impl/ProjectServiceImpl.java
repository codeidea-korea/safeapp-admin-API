package com.safeapp.admin.web.service.impl;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.dto.request.RequestDetailModifyDTO;
import com.safeapp.admin.web.dto.request.RequestStatusChangeDTO;
import com.safeapp.admin.web.dto.response.ResponseChecklistProjectDetailDTO;
import com.safeapp.admin.web.dto.response.ResponseRiskCheckDetailDTO;
import com.safeapp.admin.web.model.entity.*;
import com.safeapp.admin.web.repos.jpa.ChecklistProjectRepository;
import com.safeapp.admin.web.repos.jpa.RiskCheckRepository;
import com.safeapp.admin.web.data.DocumentType;
import com.safeapp.admin.web.data.StatusType;
import com.safeapp.admin.web.repos.jpa.*;
import com.safeapp.admin.utils.DateUtil;
import com.safeapp.admin.utils.PasswordUtil;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.BfPage;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.safeapp.admin.web.repos.jpa.ProjectRepos;
import com.safeapp.admin.web.repos.jpa.dsl.ProjectDslRepos;
import com.safeapp.admin.web.service.ProjectService;
import com.safeapp.admin.web.service.cmmn.JwtService;

@RequiredArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepos repos;

    private final ProjectDslRepos dslRepos;

    private final JwtService jwtService;

    private final PasswordUtil passwordUtil;
    private final EntityManagerFactory emf;

    private final DateUtil dateUtil;

    private final ChecklistProjectRepository checklistProjectRepository;
    private final ChecklistTemplateRepository checklistTemplateRepository;
    private final RiskCheckRepository riskCheckRepository;
    private final RiskTemplateRepository riskTemplateRepository;
    private final ChecklistProjectDetailRepository checklistProjectDetailRepository;
    private final RiskCheckDetailRepos riskCheckDetailRepos;
    private final ChecklistTemplateDetailRepos checklistTemplateDetailRepos;
    private final UserRepos userRepos;
    private final RiskTemplateDetailRepos riskTemplateDetailRepos;

    @Transactional
    @Override
    public Project add(Project instance, HttpServletRequest httpServletRequest) throws Exception {
        if (Objects.isNull(instance)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }
        Project savedInstance = repos.save(instance);
        if (Objects.isNull(savedInstance)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "저장중 오류가 발생했습니다.");
        }
        return savedInstance;
    }

    @Override
    public Project edit(Project instance, HttpServletRequest httpServletRequest)
        throws Exception {
        Project savedInstance = repos.findById(instance.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "수정을 위해서는 키가 필요합니다."));
        if (Objects.isNull(savedInstance)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }
        savedInstance = repos.save(generate(instance));
        return savedInstance;
    }

    @Override
    public void remove(long id, HttpServletRequest httpServletRequest) {
        Project savedInstance = repos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "이미 삭제된 내용이거나 존재하지 않는 키입니다."));
        repos.delete(savedInstance);
    }

    @Override
    public Project find(long id, HttpServletRequest httpServletRequest) throws Exception {
        Project savedInstance = repos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "이미 삭제된 내용이거나 존재하지 않는 키입니다."));
        return savedInstance;
    }

    @Override
    public ListResponse<Project> findAll(Project instance, BfPage bfPage,
                                         HttpServletRequest httpServletRequest) throws Exception {

        List<Project> list = dslRepos.findAll(instance, bfPage);
        long count = dslRepos.countAll(instance);

        return new ListResponse<Project>(list, count, bfPage);
    }

    @Override
    public Project generate(Project instance) {
        return Project.builder()
            .id(instance.getId())
            .address(instance.getAddress())
            .addressDetail(instance.getAddressDetail())
            .contents(instance.getContents())
            .createdAt(instance.getCreatedAt() == null ? dateUtil.getThisTime() : instance.getCreatedAt())
            .endAt(instance.getEndAt())
            .image(instance.getImage())
            .maxUserCount(instance.getMaxUserCount())
            .name(instance.getName())
            .startAt(instance.getStartAt())
            .status(instance.getStatus())
            .build();
    }

    @Override
    public List<ResponseChecklistProjectDetailDTO> findAllChecklistDetails(Long id) {

        ChecklistProject checklist = checklistProjectRepository.findById(id).orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "해당 아이디로 조회가 불가합니다."));

        List<ChecklistProjectDetail> details = checklist.getChecklistProjectDetailList();

        List<ResponseChecklistProjectDetailDTO> list = new ArrayList<>();

        details.forEach(detail ->  list.add(ResponseChecklistProjectDetailDTO.builder().detail(detail).build()));

        return list;
    }

    @Override
    public List<ResponseRiskCheckDetailDTO> findAllRiskCheckDetails(Long id) {

        RiskCheck risk = riskCheckRepository.findById(id).orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "해당 아이디로 조회가 불가합니다."));

        List<RiskCheckDetail> details = risk.getRiskCheckDetailList();

        List<ResponseRiskCheckDetailDTO> list = new ArrayList<>();

        details.forEach(detail -> list.add(ResponseRiskCheckDetailDTO.builder().detail(detail).build()));

        return list;
    }

    @Override
    public Long addProject(Long id, Long projectId, DocumentType documentType) throws NotFoundException {
        Long result = 0L;
        switch (documentType){
            case CHECKLIST:
                ChecklistTemplate checklistTemplate = checklistTemplateRepository.findById(id).orElseThrow(() -> new NotFoundException("not exist"));
                if(projectId != null){
                    Project newProject = repos.findById(projectId).orElseThrow(() -> new NotFoundException("not exist"));
                    ChecklistTemplate newChecklistTemplate = ChecklistTemplate
                            .builder()
                            .project(newProject)
                            .user(checklistTemplate.getUser())
                            .name(checklistTemplate.getName())
                            .tag(checklistTemplate.getTag())
                            .relatedAcidNo(checklistTemplate.getRelatedAcidNo())
                            .approver(checklistTemplate.getApprover())
                            .reviewer(checklistTemplate.getReviewer())
                            .checker(checklistTemplate.getChecker())
                            .build();

                    checklistTemplateRepository.save(newChecklistTemplate);

                    ChecklistProject checklistProject = ChecklistProject
                            .builder()
                            .name(checklistTemplate.getName())
                            .visibled(checklistTemplate.getVisibled())
                            .tag(checklistTemplate.getTag())
                            .relatedAcidNo(checklistTemplate.getRelatedAcidNo())
                            .project(newProject)
                            .user(checklistTemplate.getUser())
                            .checker(checklistTemplate.getChecker())
                            .reviewer(checklistTemplate.getReviewer())
                            .approver(checklistTemplate.getApprover())
                            .build();
                    checklistProjectRepository.save(checklistProject);

                    for(ChecklistTemplateDetail detail : checklistTemplate.getDetails()){
                        ChecklistProjectDetail projectDetail = templateDetailToProjectDetail(detail);
                        projectDetail.setChecklistProject(checklistProject);
                        checklistProjectDetailRepository.save(projectDetail);
                        ChecklistTemplateDetail templateDetail = projectDetailToTemplateDetail(projectDetail);
                        templateDetail.setChecklistTemplate(newChecklistTemplate);
                        checklistTemplateDetailRepos.save(templateDetail);
                    }
                    result = checklistProject.getId();
                }
                else{
                    ChecklistProject checklistProject = ChecklistProject
                            .builder()
                            .name(checklistTemplate.getName())
                            .visibled(checklistTemplate.getVisibled())
                            .tag(checklistTemplate.getTag())
                            .relatedAcidNo(checklistTemplate.getRelatedAcidNo())
                            .project(checklistTemplate.getProject())
                            .user(checklistTemplate.getUser())
                            .checker(checklistTemplate.getChecker())
                            .reviewer(checklistTemplate.getReviewer())
                            .approver(checklistTemplate.getApprover())
                            .build();
                    checklistProjectRepository.save(checklistProject);
                    for(ChecklistTemplateDetail detail : checklistTemplate.getDetails()){
                        ChecklistProjectDetail projectDetail = templateDetailToProjectDetail(detail);
                        projectDetail.setChecklistProject(checklistProject);
                        checklistProjectDetailRepository.save(projectDetail);
                    }
                    result = checklistProject.getId();
                }
                return result;
            case RISKCHECK:
                RiskTemplate riskTemplate = riskTemplateRepository.findById(id).orElseThrow(() -> new NotFoundException("checklist does not exist. input checklist id: " + id));
                RiskCheck riskCheck = new RiskCheck();
                if(projectId != null) {
                    Project riskProject = repos.findById(projectId).orElseThrow(() -> new NotFoundException("not exist"));
                    RiskTemplate newTemplate = RiskTemplate
                            .builder()
                            .id(null)
                            .project(riskProject)
                            .user(riskTemplate.getUser())
                            .name(riskTemplate.getName())
                            .tag(riskTemplate.getTag())
                            .relatedAcidNo(riskTemplate.getRelatedAcidNo())
                            .visibled(riskTemplate.getVisibled())
                            .instructDetail(riskTemplate.getInstructDetail())
                            .instructWork(riskTemplate.getInstructWork())
                            .workEndAt(riskTemplate.getWorkEndAt())
                            .workStartAt(riskTemplate.getWorkStartAt())
                            .etcRiskMemo(riskTemplate.getEtcRiskMemo())
                            .checker(riskTemplate.getChecker())
                            .reviewer(riskTemplate.getReviewer())
                            .approver(riskTemplate.getApprover())
                            .build();
                    riskTemplateRepository.save(newTemplate);
                    riskCheck.setProject(riskProject);
                    riskCheck.setUser(riskTemplate.getUser());
                    riskCheck.setName(riskTemplate.getName());
                    riskCheck.setTag(riskTemplate.getTag());
                    riskCheck.setRelatedAcidNo(riskTemplate.getRelatedAcidNo());
                    riskCheck.setWorkStartAt(riskTemplate.getWorkStartAt());
                    riskCheck.setWorkEndAt(riskTemplate.getWorkEndAt());
                    riskCheck.setVisibled(riskTemplate.getVisibled());
                    riskCheck.setEtcRiskMemo(riskTemplate.getEtcRiskMemo());
                    riskCheck.setInstructWork(riskTemplate.getInstructWork());
                    riskCheck.setInstructDetail(riskTemplate.getInstructDetail());
                    if(riskTemplate.getChecker() != null){
                        riskCheck.setChecker(riskTemplate.getChecker());
                    }
                    if(riskTemplate.getReviewer() != null){
                        riskCheck.setReviewer(riskTemplate.getReviewer());
                    }
                    if(riskTemplate.getApprover() != null){
                        riskCheck.setApprover(riskTemplate.getApprover());
                    }
                    riskCheckRepository.save(riskCheck);
                    for (RiskTemplateDetail templateDetail : riskTemplate.getDetails()) {
                        RiskCheckDetail riskCheckDetail = this.templateDetailToRiskDetail(templateDetail);
                        riskCheckDetail.setRiskCheck(riskCheck);
                        riskCheckDetailRepos.save(riskCheckDetail);
                        RiskTemplateDetail riskTemplateDetail = this.riskDetailToTemplateDetail(riskCheckDetail);
                        riskTemplateDetail.setRiskTemplate(newTemplate);
                        riskTemplateDetailRepos.save(riskTemplateDetail);
                    }
                    result = riskCheck.getId();
                }
                else{
                        riskCheck.setProject(riskTemplate.getProject());
                        riskCheck.setUser(riskTemplate.getUser());
                        riskCheck.setName(riskTemplate.getName());
                        riskCheck.setTag(riskTemplate.getTag());
                        riskCheck.setRelatedAcidNo(riskTemplate.getRelatedAcidNo());
                        riskCheck.setWorkStartAt(riskTemplate.getWorkStartAt());
                        riskCheck.setWorkEndAt(riskTemplate.getWorkEndAt());
                        riskCheck.setVisibled(riskTemplate.getVisibled());
                        riskCheck.setEtcRiskMemo(riskTemplate.getEtcRiskMemo());
                        riskCheck.setInstructWork(riskTemplate.getInstructWork());
                        riskCheck.setInstructDetail(riskTemplate.getInstructDetail());
                        if(riskTemplate.getChecker() != null){
                            riskCheck.setChecker(riskTemplate.getChecker());
                        }
                        if(riskTemplate.getReviewer() != null){
                            riskCheck.setReviewer(riskTemplate.getReviewer());
                        }
                        if(riskTemplate.getApprover() != null){
                            riskCheck.setApprover(riskTemplate.getApprover());
                        }
                        riskCheckRepository.save(riskCheck);

                    for (RiskTemplateDetail templateDetail : riskTemplate.getDetails()) {
                        RiskCheckDetail riskCheckDetail = this.templateDetailToRiskDetail(templateDetail);
                        riskCheckDetail.setRiskCheck(riskCheck);
                        riskCheckDetailRepos.save(riskCheckDetail);
                    }
                    result = riskCheck.getId();
                }
            return result;
            default:
                return null;
        }
    }

    @Override
    public Long addNewProject(Long projectId, Long documentId, DocumentType documentType) throws NotFoundException {
        switch (documentType){
            case CHECKLIST:
                ChecklistProject checklistProject = checklistProjectRepository.findById(documentId).orElseThrow(() -> new NotFoundException("checklist does not exist. input checklist id: " + documentId));
                Project checkProject = repos.findById(projectId).orElseThrow((() -> new NotFoundException("checklist does not exist. input checklist id: " + projectId)));
                List<ChecklistProjectDetail> details = checklistProject.getChecklistProjectDetailList();

                ChecklistProject newChecklist = ChecklistProject
                        .builder()
                        .name(checklistProject.getName())
                        .visibled(checklistProject.getVisibled())
                        .tag(checklistProject.getTag())
                        .relatedAcidNo(checklistProject.getRelatedAcidNo())
                        .project(checkProject)
                        .user(checklistProject.getUser())
                        .checker(checklistProject.getChecker())
                        .reviewer(checklistProject.getReviewer())
                        .approver(checklistProject.getApprover())
                        .detailContents(checklistProject.getDetailContents())
                        .build();
                checklistProjectRepository.saveAndFlush(newChecklist);

                ChecklistTemplate template = ChecklistTemplate
                        .builder()
                        .id(null)
                        .project(checkProject)
                        .user(checklistProject.getUser())
                        .name(checklistProject.getName())
                        .tag(checklistProject.getTag())
                        .checker(checklistProject.getChecker())
                        .reviewer(checklistProject.getReviewer())
                        .approver(checklistProject.getApprover())
                        .relatedAcidNo(checklistProject.getRelatedAcidNo())
                        .build();
                saveTemplate(template);

                for(ChecklistProjectDetail detail : details){
                    ChecklistTemplateDetail templateDetail = projectDetailToTemplateDetail(detail);
                    templateDetail.setChecklistTemplate(template);
                    checklistTemplateDetailRepos.save(templateDetail);
                    ChecklistProjectDetail projectDetail = templateDetailToProjectDetail(templateDetail);
                    projectDetail.setChecklistProject(newChecklist);
                    checklistProjectDetailRepository.save(projectDetail);
                }
                return newChecklist.getId();
            case RISKCHECK:
                RiskCheck riskCheck = riskCheckRepository.findById(documentId).orElseThrow(() -> new NotFoundException("checklist does not exist. input checklist id: " + documentId));
                Project riskProject = repos.findById(projectId).orElseThrow((() -> new NotFoundException("checklist does not exist. input checklist id: " + projectId)));
                List<RiskCheckDetail> checkDetails = riskCheck.getRiskCheckDetailList();

                RiskCheck newRiskCheck = RiskCheck
                        .builder()
                        .name(riskCheck.getName())
                        .user(riskCheck.getUser())
                        .project(riskProject)
                        .tag(riskCheck.getTag())
                        .relatedAcidNo(riskCheck.getRelatedAcidNo())
                        .visibled(riskCheck.getVisibled())
                        .instructWork(riskCheck.getInstructWork())
                        .instructDetail(riskCheck.getInstructDetail())
                        .workStartAt(riskCheck.getWorkStartAt())
                        .workEndAt(riskCheck.getWorkEndAt())
                        .etcRiskMemo(riskCheck.getEtcRiskMemo())
                        .checker(riskCheck.getChecker())
                        .reviewer(riskCheck.getReviewer())
                        .approver(riskCheck.getApprover())
                        .build();
                riskCheckRepository.save(newRiskCheck);

                RiskTemplate riskTemplate = RiskTemplate
                        .builder()
                        .id(null)
                        .name(riskCheck.getName())
                        .project(riskProject)
                        .user(riskCheck.getUser())
                        .tag(riskCheck.getTag())
                        .relatedAcidNo(riskCheck.getRelatedAcidNo())
                        .visibled(riskCheck.getVisibled())
                        .instructWork(riskCheck.getInstructWork())
                        .instructDetail(riskCheck.getInstructDetail())
                        .workStartAt(riskCheck.getWorkStartAt())
                        .workEndAt(riskCheck.getWorkEndAt())
                        .etcRiskMemo(riskCheck.getEtcRiskMemo())
                        .checker(riskCheck.getChecker())
                        .reviewer(riskCheck.getReviewer())
                        .approver(riskCheck.getApprover())
                        .build();
                riskTemplateRepository.save(riskTemplate);

                for(RiskCheckDetail detail : checkDetails){
                    RiskTemplateDetail templateDetail = riskDetailToTemplateDetail(detail);
                    templateDetail.setRiskTemplate(riskTemplate);
                    riskTemplateDetailRepos.save(templateDetail);
                    RiskCheckDetail riskCheckDetail = templateDetailToRiskDetail(templateDetail);
                    riskCheckDetail.setRiskCheck(newRiskCheck);
                    riskCheckDetailRepos.save(riskCheckDetail);
                }
                return newRiskCheck.getId();
            default:
                return null;
        }
    }

    private void saveTemplate(ChecklistTemplate template){
        checklistTemplateRepository.saveAndFlush(template);
    }

    @Override
    public Boolean changeStatus(Long id, DocumentType documentType, StatusType status, String recheckReason, RequestStatusChangeDTO dto, HttpServletRequest request) throws NotFoundException {
        Users user = jwtService.getUserInfoByToken(request);
        switch (documentType){
            case CHECKLIST:
                ChecklistProject dbChecklist = checklistProjectRepository.findById(id).orElseThrow(() -> new NotFoundException("checklist does not exist. input checklist id: " + id));
                dbChecklist.setStatus(status);
                switch (status){
                    case CHECK:
                        dbChecklist.setChecker(user);
                        for(RequestStatusChangeDTO.Detail memo : dto.getDetails()){
                            ChecklistProjectDetail dbDetail = checklistProjectDetailRepository.findById(memo.getDetailId()).orElseThrow(() -> new NotFoundException("checklist does not exist. input checklist id: " + id));
                            dbDetail.setMemo(memo.getMemo());
                            checklistProjectDetailRepository.save(dbDetail);
                        }
                    case REVIEW:
                        dbChecklist.setReviewer(user);
                        dbChecklist.setReview_at(LocalDateTime.now());
                        break;
                    case APPROVE:
                        dbChecklist.setApprover(user);
                        dbChecklist.setApprove_at(LocalDateTime.now());
                        break;
                    case RECHECK:
                        dbChecklist.setRecheckReason(recheckReason);
                        break;
                }
                checklistProjectRepository.save(dbChecklist);
                return true;
            case RISKCHECK:
                RiskCheck dbRisk = riskCheckRepository.findById(id).orElseThrow(() -> new NotFoundException("riskcheck does not exist. input riskcheck id : " + id));
                dbRisk.setStatus(status);
                switch (status){
                    case CHECK:
                        dbRisk.setChecker(user);
                        dbRisk.setCheckAt(LocalDateTime.now());
                    case REVIEW:
                        dbRisk.setReviewer(user);
                        dbRisk.setReviewAt(LocalDateTime.now());
                        break;
                    case APPROVE:
                        dbRisk.setApprover(user);
                        dbRisk.setApproveAt(LocalDateTime.now());
                        break;
                    case RECHECK:
                        dbRisk.setRecheckReason(recheckReason);
                        break;
                }
                riskCheckRepository.save(dbRisk);
                return true;
            default:
                return false;
        }
    }
    @Override
    public ChecklistProjectDetail templateDetailToProjectDetail(ChecklistTemplateDetail templateDetail){
        ChecklistProjectDetail projectDetail = new ChecklistProjectDetail();
        projectDetail.setDepth(templateDetail.getDepth());
        projectDetail.setIzTitle(templateDetail.getIzTitle());
        projectDetail.setParentDepth(templateDetail.getParentDepth());
        projectDetail.setContents(templateDetail.getContents());
        projectDetail.setOrders(templateDetail.getOrders());
        projectDetail.setParentOrders(templateDetail.getParentOrders());
        projectDetail.setTypes(templateDetail.getTypes());

        return projectDetail;
    }

    @Override
    public ChecklistTemplateDetail projectDetailToTemplateDetail(ChecklistProjectDetail detail){
        return ChecklistTemplateDetail
                .builder()
                .depth(detail.getDepth())
                .izTitle(detail.getIzTitle())
                .parentDepth(detail.getParentDepth())
                .contents(detail.getContents())
                .orders(detail.getOrders())
                .types(detail.getTypes())
                .parentOrders(detail.getParentOrders())
                .build();
    }

    @Override
    public RiskCheckDetail templateDetailToRiskDetail(RiskTemplateDetail templateDetail) {
        return RiskCheckDetail
                .builder()
                .contents(templateDetail.getContents())
                .address(templateDetail.getAddress())
                .addressDetail(templateDetail.getAddressDetail())
                .tools(templateDetail.getTools())
                .riskFactorType(templateDetail.getRiskFactorType())
                .relateLaw(templateDetail.getRelateLaw())
                .relateGuide(templateDetail.getRelateGuide())
                .riskType(templateDetail.getRiskType())
                .reduceResponse(templateDetail.getReduceResponse())
                .checkMemo(templateDetail.getCheckMemo())
                .parentDepth(templateDetail.getParentDepth())
                .orders(templateDetail.getOrders())
                .depth(templateDetail.getDepth())
                .parentOrders(templateDetail.getParentOrders())
                .build();
    }

    @Override
    public RiskTemplateDetail riskDetailToTemplateDetail(RiskCheckDetail detail){
        return RiskTemplateDetail
                .builder()
                .contents(detail.getContents())
                .address(detail.getAddress())
                .addressDetail(detail.getAddressDetail())
                .tools(detail.getTools())
                .riskFactorType(detail.getRiskFactorType())
                .relateLaw(detail.getRelateLaw())
                .relateGuide(detail.getRelateGuide())
                .riskType(detail.getRiskType())
                .reduceResponse(detail.getReduceResponse())
                .checkMemo(detail.getCheckMemo())
                .parentOrders(detail.getParentOrders())
                .orders(detail.getOrders())
                .depth(detail.getDepth())
                .parentDepth(detail.getParentDepth()).build();
    }

    @Override
    public Long modifyDetails(List<RequestDetailModifyDTO> dto, DocumentType documentType) throws NotFoundException {
        Long result = 0L;
        switch (documentType){
            case CHECKLIST:
                for(RequestDetailModifyDTO detail : dto){
                    ChecklistProjectDetail dbDetail = checklistProjectDetailRepository.findById(detail.getId()).orElseThrow(() -> new NotFoundException("checklist does not exist. input checklist id: " + detail.getId()));
                    result = dbDetail.getChecklistProject().getId();
                    dbDetail.setDepth(detail.getDepth());
                    dbDetail.setIzTitle(detail.getIzTitle());
                    dbDetail.setParentDepth(detail.getParentDepth());
                    dbDetail.setContents(detail.getContents());
                    dbDetail.setOrders(detail.getOrders());
                    dbDetail.setParentOrders(detail.getParentOrders());
                    dbDetail.setTypes(detail.getTypes());
                    checklistProjectDetailRepository.save(dbDetail);
                }
                return result;
            case RISKCHECK:
                for(RequestDetailModifyDTO detail : dto){
                    RiskCheckDetail dbRiskDetail = riskCheckDetailRepos.findById(detail.getId()).orElseThrow(() -> new NotFoundException("checklist does not exist. input checklist id: " + detail.getId()));
                    result = dbRiskDetail.getRiskCheck().getId();
                    dbRiskDetail.setDepth(detail.getDepth());
                    dbRiskDetail.setParentDepth(detail.getParentDepth());
                    dbRiskDetail.setContents(detail.getContents());
                    dbRiskDetail.setOrders(detail.getOrders());
                    dbRiskDetail.setParentOrders(detail.getParentOrders());
                    dbRiskDetail.setContents(detail.getContents());
                    dbRiskDetail.setAddressDetail(detail.getAddressDetail());
                    dbRiskDetail.setAddress(detail.getAddress());
                    dbRiskDetail.setTools(detail.getTools());
                    dbRiskDetail.setRiskFactorType(detail.getRiskFactorType());
                    dbRiskDetail.setRelateLaw(detail.getRelateLaw());
                    dbRiskDetail.setRelateGuide(detail.getRelateGuide());
                    dbRiskDetail.setRiskType(detail.getRiskType());
                    dbRiskDetail.setReduceResponse(detail.getReduceResponse());
                    dbRiskDetail.setCheckMemo(detail.getCheckMemo());
                    dbRiskDetail.setParentOrders(detail.getParentOrders());
                    dbRiskDetail.setStatus(detail.getStatus());
                    dbRiskDetail.setParentDepth(detail.getParentDepth());
                    dbRiskDetail.setOrders(detail.getOrders());
                    dbRiskDetail.setDepth(detail.getDepth());
                    riskCheckDetailRepos.save(dbRiskDetail);
                }
                return result;
            default:
                return null;
        }
    }
}
