package com.safeapp.admin.web.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.data.StatusType;
import com.safeapp.admin.web.dto.request.RequestChecklistProjectDTO;
import com.safeapp.admin.web.dto.request.RequestChecklistProjectModifyDTO;
import com.safeapp.admin.web.dto.response.ResponseChecklistProjectDTO;
import com.safeapp.admin.web.model.entity.Admins;
import com.safeapp.admin.web.model.entity.ChecklistProjectDetail;
import com.safeapp.admin.web.repos.jpa.ProjectRepos;
import com.safeapp.admin.web.repos.jpa.UserRepos;
import com.safeapp.admin.utils.DateUtil;
import com.safeapp.admin.utils.PasswordUtil;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.docs.LikeHistory;
import com.safeapp.admin.web.model.entity.ChecklistProject;
import com.safeapp.admin.web.model.entity.Users;
import com.safeapp.admin.web.repos.jpa.ChecklistProjectRepository;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.safeapp.admin.web.repos.jpa.dsl.ChecklistProjectDslRepos;
import com.safeapp.admin.web.repos.mongo.LikeHistoryRepos;
import com.safeapp.admin.web.service.ChecklistProjectDetailService;
import com.safeapp.admin.web.service.ChecklistProjectService;
import com.safeapp.admin.web.service.cmmn.JwtService;

@Service
@RequiredArgsConstructor
public class ChecklistProjectServiceImpl implements ChecklistProjectService {

    private final ChecklistProjectRepository repos;

    private final ChecklistProjectDslRepos dslRepos;

    private final ProjectRepos projectRepos;

    private final UserRepos userRepos;

    private final JwtService jwtService;

    private final PasswordUtil passwordUtil;

    private final DateUtil dateUtil;

    private final ChecklistProjectDetailService detailService;

    private final LikeHistoryRepos likeRepos;

    @Transactional
    @Override
    public ChecklistProject add(ChecklistProject instance, HttpServletRequest httpServletRequest) throws Exception {
        if (Objects.isNull(instance)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }
        ChecklistProject savedInstance = repos.save(instance);
        if (Objects.isNull(savedInstance)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "저장중 오류가 발생했습니다.");
        }
        return savedInstance;
    }

    @Transactional
    @Override
    public ChecklistProject edit(ChecklistProject instance, HttpServletRequest httpServletRequest) throws Exception {
        ChecklistProject savedInstance = repos.findById(instance.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "수정을 위해서는 키가 필요합니다."));
        if (Objects.isNull(savedInstance)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }
        savedInstance.update(instance);
        repos.save(savedInstance);
        return savedInstance;
    }

    @Override
    public void remove(long id, HttpServletRequest httpServletRequest) {
        ChecklistProject savedInstance = repos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "이미 삭제된 내용이거나 존재하지 않는 키입니다."));
        repos.delete(savedInstance);
    }

    @Override
    public ChecklistProject find(long id, HttpServletRequest httpServletRequest) throws Exception {
        ChecklistProject savedInstance = repos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "이미 삭제된 내용이거나 존재하지 않는 키입니다."));

        savedInstance.setViews(savedInstance.getViews() + 1);
        savedInstance = repos.save(savedInstance);

        //로그인한 유저가 해당 게시글 좋아요 눌렀는지 체크
        savedInstance.setLiked(checkMyLike(savedInstance.getId(),httpServletRequest));

        return savedInstance;
    }

    @Override
    public ListResponse<ChecklistProject> findAll(ChecklistProject instance, Pages bfPage,
                                                  HttpServletRequest httpServletRequest) throws Exception {

        List<ChecklistProject> list = dslRepos.findAll(instance, bfPage);
        long count = dslRepos.countAll(instance);

        Admins admin = jwtService.getAdminInfoByTokenAnyway(httpServletRequest);
        if(admin != null) {
            for(ChecklistProject item : list) {
                LikeHistory liked = likeRepos.findByUserIDAndTypeAndBoardId(item.getUser().getId(), "checklist", item.getId());

                if(liked != null) {
                    item.setLiked(liked.getLiked());
                }
            }
        }

        return new ListResponse<>(count, list, bfPage);
    }

    @Override
    public ChecklistProject generate(ChecklistProject instance) {
        ChecklistProject project = ChecklistProject.builder()
            .id(instance.getId())
            .checkAt(instance.getCheckAt())
            .likes(instance.getLikes())
            .name(instance.getName())
            .recheckReason(instance.getRecheckReason())
            .relatedAcidNo(instance.getRelatedAcidNo())
            .tag(instance.getTag())
            .views(instance.getViews())
            .visibled(instance.getVisibled() == null ? YN.Y : instance.getVisibled())
            .build();

        return project;
    }

    @Override
    public void addLike(long id, HttpServletRequest httpServletRequest) {

        Admins admin = jwtService.getAdminInfoByToken(httpServletRequest);

        if(admin == null) {
            throw new HttpServerErrorException(HttpStatus.UNAUTHORIZED, "먼저 로그인하여주세요.");
        }

        LikeHistory liked = likeRepos.findByUserIDAndTypeAndBoardId(admin.getId(), "checklist", id);
        if(liked != null) {
            if(liked.getLiked() == YN.Y) {
                throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "이미 좋아요 하셨습니다.");
            }
            liked.setLiked(YN.Y);
        } else {
            likeRepos.save(LikeHistory.builder()
                .liked(YN.Y)
                .type("checklist")
                .boardId(id)
                .userID(admin.getId())
                .build());
        }
    }

    @Override
    public YN checkMyLike(long id, HttpServletRequest httpServletRequest) {

        YN yn = YN.N;

        Admins admin = jwtService.getAdminInfoByToken(httpServletRequest);

        if(admin == null) {
            throw new HttpServerErrorException(HttpStatus.UNAUTHORIZED, "먼저 로그인하여주세요.");
        }

        LikeHistory liked = likeRepos.findByUserIDAndTypeAndBoardId(admin.getId(), "checklist", id);

        if(liked != null) {
            yn = YN.Y;
        }

        return yn;
    }

    @Override
    public void removeLike(long id, HttpServletRequest httpServletRequest) {

        Admins admin = jwtService.getAdminInfoByToken(httpServletRequest);

        if(admin == null) {
            throw new HttpServerErrorException(HttpStatus.UNAUTHORIZED, "먼저 로그인하여주세요.");        }

        LikeHistory liked = likeRepos.findByUserIDAndTypeAndBoardId(admin.getId(), "checklist", id);
        if(liked != null) {
            likeRepos.delete(liked);
        } else {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "좋아요를 먼저 선택해 주세요.");
        }
    }

    @Override
    public boolean isLiked(long id, HttpServletRequest httpServletRequest) {

        Admins admin = jwtService.getAdminInfoByToken(httpServletRequest);

        LikeHistory liked = likeRepos.findByUserIDAndTypeAndBoardId(admin.getId(), "checklist", id);
        return liked != null && liked.getLiked() == YN.Y;
    }

    @Override
    public ChecklistProject toEntity(RequestChecklistProjectDTO dto) throws NotFoundException {
        ChecklistProject checklistProject = new ChecklistProject();
        if(dto.getProjectId() != null) {
            checklistProject.setProject(projectRepos.findById(dto.getProjectId()).orElseThrow(() -> new NotFoundException("project does not exist. input project id: " + dto.getProjectId())));
        }
        if(dto.getCheckerId() != null){
            checklistProject.setChecker(userRepos.findById(dto.getCheckerId()).orElseThrow(() -> new NotFoundException("checker does not exist. input checker id: " + dto.getCheckerId())));
            checklistProject.setCheckAt(LocalDateTime.now());
        }
        if(dto.getReviewerId() != null){
            checklistProject.setReviewer(userRepos.findById(dto.getReviewerId()).orElseThrow(() -> new NotFoundException("reviewer does not exist. input reviewer id: " + dto.getReviewerId())));
            checklistProject.setReview_at(LocalDateTime.now());
        }
        if(dto.getApproverId() != null) {
            checklistProject.setApprover(userRepos.findById(dto.getApproverId()).orElseThrow(() -> new NotFoundException("user does not exist. input user id: " + dto.getUserId())));
            checklistProject.setApprove_at(LocalDateTime.now());
        }
        checklistProject.setUser(userRepos.findById(dto.getUserId()).orElseThrow(() -> new NotFoundException("user does not exist. input user id: " + dto.getUserId())));
        checklistProject.setName(dto.getName());
        checklistProject.setVisibled(dto.getVisibled());
        checklistProject.setTag(dto.getTag());
        checklistProject.setRelatedAcidNo(dto.getRelatedAcidNo());
        checklistProject.setRecheckReason(dto.getRecheckReason());

        return checklistProject;
    }

    @Override
    public ChecklistProject toEntityModify(RequestChecklistProjectModifyDTO dto) throws NotFoundException {
        ChecklistProject checklistProject = new ChecklistProject();
        checklistProject.setProject(projectRepos.findById(dto.getProjectId()).orElseThrow(() -> new NotFoundException("project does not exist. input project id: " + dto.getProjectId())));
        if(dto.getCheckerId() != null) {
            checklistProject.setChecker(userRepos.findById(dto.getCheckerId()).orElseThrow(() -> new NotFoundException("checker does not exist. input checker id: " + dto.getCheckerId())));
            checklistProject.setCheckAt(LocalDateTime.now());
        }
        if(dto.getReviewerId() != null) {
            checklistProject.setReviewer(userRepos.findById(dto.getReviewerId()).orElseThrow(() -> new NotFoundException("reviewer does not exist. input reviewer id: " + dto.getReviewerId())));
            checklistProject.setReview_at(LocalDateTime.now());
        }
        if(dto.getApproverId() != null) {
            checklistProject.setApprover(userRepos.findById(dto.getApproverId()).orElseThrow(() -> new NotFoundException("user does not exist. input user id: " + dto.getUserId())));
            checklistProject.setApprove_at(LocalDateTime.now());
        }
        checklistProject.setUser(userRepos.findById(dto.getUserId()).orElseThrow(() -> new NotFoundException("user does not exist. input user id: " + dto.getUserId())));

        checklistProject.setName(dto.getName());
        checklistProject.setVisibled(dto.getVisibled());
        checklistProject.setTag(dto.getTag());
        checklistProject.setCheckAt(dto.getCheckAt());
        checklistProject.setRelatedAcidNo(dto.getRelatedAcidNo());
        checklistProject.setRecheckReason(dto.getRecheckReason());
        List<ChecklistProjectDetail> details = new ArrayList<>();
        if(dto.getDetails().isEmpty() == false) {
            for(RequestChecklistProjectModifyDTO.DetailModifyDTO modify : dto.getDetails()){
                ChecklistProjectDetail detail = new ChecklistProjectDetail();
                detail.setTypes(modify.getTypes());
                detail.setDepth(modify.getDepth());
                detail.setChecklistProject(checklistProject);
                detail.setIzTitle(modify.getIzTitle());
                detail.setParentDepth(modify.getParentDepth());
                detail.setParentOrders(modify.getParentOrders());
                detail.setContents(modify.getContents());
                detail.setOrders(modify.getOrders());
                if(modify.getId() != null) {
                    detail.setId(modify.getId());
                };
                details.add(detail);
            }
        }
        checklistProject.setChecklistProjectDetailList(details);

        return  checklistProject;
    }

    @Override
    public List<ResponseChecklistProjectDTO> findAllByCondition(
            Long userId,
            Long projectId,
            String name,
            String tag,
            YN visibled,
            YN created_at_descended,
            YN views_descended,
            YN likes_descended,
            String detail_contents,
            Pageable page,
            HttpServletRequest request
    ){

        List<ResponseChecklistProjectDTO> list = repos.findAllByConditionAndOrderBy(userId, projectId, name, tag,visibled, created_at_descended, views_descended, likes_descended, detail_contents, page);

        //로그인한 유저가 해당 게시글 좋아요 눌렀는지 체크
        list.forEach(dto -> dto.setLiked(checkMyLike(dto.getId(), request)));

        return list;
    }

    @Override
    public void updateStatus(Long id, StatusType type) throws NotFoundException {
        ChecklistProject checklist = repos.findById(id).orElseThrow(() -> new NotFoundException("user does not exist. input user id: " + id));
        checklist.setStatus(type);
        LocalDateTime statusTime = LocalDateTime.now();
        switch (type){
            case CHECK:
                checklist.setCheckAt(statusTime);
                break;
            case REVIEW:
                checklist.setReview_at(statusTime);
                break;
            case APPROVE:
                checklist.setApprove_at(statusTime);
                break;
        }
        repos.save(checklist);
    }
}
