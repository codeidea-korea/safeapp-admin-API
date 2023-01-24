package com.safeapp.admin.web.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.data.StatusType;
import com.safeapp.admin.web.dto.request.RequestRiskCheckDTO;
import com.safeapp.admin.web.dto.response.ResponseRiskcheckDTO;
import com.safeapp.admin.web.model.entity.Admins;
import com.safeapp.admin.web.repos.jpa.AccidentExpRepos;
import com.safeapp.admin.web.repos.jpa.ProjectRepos;
import com.safeapp.admin.web.repos.jpa.RiskCheckRepository;
import com.safeapp.admin.web.repos.jpa.UserRepos;
import com.safeapp.admin.utils.DateUtil;
import com.safeapp.admin.utils.PasswordUtil;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.docs.LikeHistory;
import com.safeapp.admin.web.model.entity.RiskCheck;
import lombok.AllArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.safeapp.admin.web.repos.jpa.dsl.RiskCheckDslRepos;
import com.safeapp.admin.web.repos.mongo.LikeHistoryRepos;
import com.safeapp.admin.web.service.RiskCheckService;
import com.safeapp.admin.web.service.cmmn.JwtService;

@Service
@AllArgsConstructor
public class RiskCheckServiceImpl implements RiskCheckService {

    private final RiskCheckRepository repos;

    private final RiskCheckDslRepos dslRepos;

    private final JwtService jwtService;

    private final PasswordUtil passwordUtil;

    private final DateUtil dateUtil;

    private final LikeHistoryRepos likeRepos;

    private final ProjectRepos projectRepos;
    private final UserRepos userRepos;
    private final AccidentExpRepos accidentExpRepos;

    @Transactional
    @Override
    public RiskCheck add(RiskCheck instance, HttpServletRequest httpServletRequest) throws Exception {
        if (Objects.isNull(instance)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }
        instance.setViews(0);
        instance.setLikes(0);
        RiskCheck savedInstance = repos.save(instance);
        if (Objects.isNull(savedInstance)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "저장중 오류가 발생했습니다.");
        }
//        for(RiskCheckDetail detail : instance.getDetails()) {
//            detail.setRiskCheckId(savedInstance.getId());
//        }
        return savedInstance;
    }

    @Transactional
    @Override
    public RiskCheck edit(RiskCheck instance, HttpServletRequest httpServletRequest)
        throws Exception {
        RiskCheck savedInstance = repos.findById(instance.getId())
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
        RiskCheck savedInstance = repos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "이미 삭제된 내용이거나 존재하지 않는 키입니다."));
        repos.delete(savedInstance);
    }

    @Override
    public RiskCheck find(long id, HttpServletRequest httpServletRequest) throws Exception {
        RiskCheck savedInstance = repos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "이미 삭제된 내용이거나 존재하지 않는 키입니다."));
        if(savedInstance.getViews() != null) {
            savedInstance.setViews(savedInstance.getViews() + 1);
        }
        else{
            savedInstance.setViews(1);
        }

        savedInstance = repos.save(savedInstance);

        //로그인한 유저가 해당 게시글 좋아요 눌렀는지 체크
        savedInstance.setLiked(checkMyLike(savedInstance.getId(),httpServletRequest));
        
        return savedInstance;
    }

    @Override
    public ListResponse<RiskCheck> findAll(RiskCheck instance, Pages bfPage,
                                           HttpServletRequest httpServletRequest) throws Exception {

        List<RiskCheck> list = dslRepos.findAll(instance, bfPage);
        long count = dslRepos.countAll(instance);

//        for(RiskCheck item : list) {
//            if(item != null && item.getDetails() == null) {
//                item.setDetails(new ArrayList<>());
//            }
//        }

//        Users user = userJwtService.getUserInfoByTokenAnyway(httpServletRequest);
//        if(user != null) {
//            for(RiskCheck item : list) {
//                LikeHistory liked = likeRepos.findByUserIdAndTypeAndBoardId(item.getUserId(), "risk", item.getId());
//
//                if(liked != null) {
//                    item.setLiked(liked.getLiked());
//                }
//            }
//        }
        return new ListResponse<RiskCheck>(count, list, bfPage);
    }

    @Override
    public RiskCheck generate(RiskCheck instance) {
        return RiskCheck.builder()
            .id(instance.getId())
            .createdAt(instance.getCreatedAt() == null ? dateUtil.getThisTime() : instance.getCreatedAt())

            .etcRiskMemo(instance.getEtcRiskMemo())
            .instructDetail(instance.getInstructDetail())
            .instructWork(instance.getInstructWork())
            .likes(instance.getLikes())
            .name(instance.getName())
            .recheckReason(instance.getRecheckReason())
            .relatedAcidNo(instance.getRelatedAcidNo())
            .tag(instance.getTag())
            .updatedAt(dateUtil.getThisTime())
            .views(instance.getViews())
            .visibled(instance.getVisibled() == null ? YN.Y : instance.getVisibled())
            .workEndAt(instance.getWorkEndAt())
            .workStartAt(instance.getWorkStartAt())

            .build();
    }

    @Override
    public void addLike(long id, HttpServletRequest httpServletRequest) {
        
        Admins admin = jwtService.getAdminInfoByToken(httpServletRequest);
        
        if(admin == null) {
            throw new HttpServerErrorException(HttpStatus.UNAUTHORIZED, "먼저 로그인하여주세요.");
        }
        
        LikeHistory liked = likeRepos.findByUserIdAndTypeAndBoardId(admin.getId(), "risk", id);
        if(liked != null) {
            if(liked.getLiked() == YN.Y) {
                throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "이미 좋아요 하셨습니다.");
            }
            liked.setLiked(YN.Y);
        } else {
            likeRepos.save(LikeHistory.builder()
                .liked(YN.Y)
                .type("risk")
                .boardId(id)
                .userId(admin.getId())
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

        LikeHistory liked = likeRepos.findByUserIdAndTypeAndBoardId(admin.getId(), "checklist", id);

        if(liked != null) {
            yn = YN.Y;
        }

        return yn;
    }

    @Override
    public void removeLike(long id, HttpServletRequest httpServletRequest) {
        
        Admins admin = jwtService.getAdminInfoByToken(httpServletRequest);
        
        if(admin == null) {
            throw new HttpServerErrorException(HttpStatus.UNAUTHORIZED, "먼저 로그인하여주세요.");
        }

        LikeHistory liked = likeRepos.findByUserIdAndTypeAndBoardId(admin.getId(), "risk", id);
        if(liked != null) {
            likeRepos.delete(liked);
        } else {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "좋아요를 먼저 선택해 주세요.");
        }
    }

    @Override
    public boolean isLiked(long id, HttpServletRequest httpServletRequest) {
        
        Admins admin = jwtService.getAdminInfoByToken(httpServletRequest);

        LikeHistory liked = likeRepos.findByUserIdAndTypeAndBoardId(admin.getId(), "risk", id);
        return liked != null && liked.getLiked() == YN.Y;
    }

    @Override
    public RiskCheck toEntity(RequestRiskCheckDTO dto) throws NotFoundException{
        RiskCheck riskCheck = new RiskCheck();
        if(dto.getProjectId() != null){
            riskCheck.setProject(projectRepos.findById(dto.getProjectId()).orElseThrow(() -> new NotFoundException("project does not exist. input project id: " + dto.getProjectId())));
        }
        if(dto.getCheckerId() != null) {
            riskCheck.setChecker(userRepos.findById(dto.getCheckerId()).orElseThrow(() -> new NotFoundException("checker does not exist. input checker id: " + dto.getCheckerId())));
        }
        if(dto.getReviewerId() != null) {
            riskCheck.setReviewer(userRepos.findById(dto.getReviewerId()).orElseThrow(() -> new NotFoundException("reviewer does not exist. input reviewer id: " + dto.getReviewerId())));
        }
        if(dto.getApproverId() != null) {
            riskCheck.setApprover(userRepos.findById(dto.getApproverId()).orElseThrow(() -> new NotFoundException("user does not exist. input user id: " + dto.getUserId())));
        }
        riskCheck.setUser(userRepos.findById(dto.getUserId()).orElseThrow(() -> new NotFoundException("user does not exist. input user id: " + dto.getUserId())));
        riskCheck.setName(dto.getName());
        riskCheck.setVisibled(dto.getVisibled());
        riskCheck.setTag(dto.getTag());
        riskCheck.setWorkStartAt(dto.getWorkStartAt());
        riskCheck.setWorkEndAt(dto.getWorkEndAt());
        riskCheck.setInstructWork(dto.getInstructWork());
        riskCheck.setInstructDetail(dto.getInstructDetail());
        riskCheck.setEtcRiskMemo(dto.getEtcRiskMemo());
        riskCheck.setRecheckReason(dto.getRecheckReason());
        riskCheck.setRelatedAcidNo(dto.getRelatedAcidNo());

        return riskCheck;
    }

    @Override
    public void updateStatus(Long id, StatusType type) throws NotFoundException {
        RiskCheck checklist = repos.findById(id).orElseThrow(() -> new NotFoundException("riskCheck does not exist. riskCheckid: " + id));
        checklist.setStatus(type);
        LocalDateTime statusTime = LocalDateTime.now();
        switch (type){
            case CHECK:
                checklist.setCheckAt(statusTime);
                break;
            case REVIEW:
                checklist.setReviewAt(statusTime);
                break;
            case APPROVE:
                checklist.setApproveAt(statusTime);
                break;
        }
        repos.save(checklist);
    }

    @Override
    public List<ResponseRiskcheckDTO> findAllByCondition(
            Long userId,
            Long projectId,
            String name,
            String tag,
            YN visibled,
            String status,
            YN created_at_descended,
            YN views_descended,
            YN likes_descended,
            String detail_contents,
            Pageable page,
            HttpServletRequest request
    ){
        List<ResponseRiskcheckDTO> list = repos.findAllByCondition(userId, projectId, name, tag,visibled, status, created_at_descended, views_descended, likes_descended, detail_contents, page);

        //로그인한 유저가 해당 게시글 좋아요 눌렀는지 체크
        list.forEach(dto -> dto.setLiked(checkMyLike(dto.getId(),request)));
        return list;
    }
}
