package com.safeapp.admin.web.service.impl;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.dto.request.RequestRiskTemplateDTO;
import com.safeapp.admin.web.dto.response.ResponseRiskTemplateDTO;
import com.safeapp.admin.web.model.entity.Admins;
import com.safeapp.admin.web.repos.jpa.ProjectRepos;
import com.safeapp.admin.web.repos.jpa.UserRepos;
import com.safeapp.admin.utils.DateUtil;
import com.safeapp.admin.utils.PasswordUtil;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.docs.LikeHistory;
import com.safeapp.admin.web.model.entity.RiskTemplate;
import com.safeapp.admin.web.model.entity.RiskTemplateDetail;
import com.safeapp.admin.web.model.entity.Users;
import com.safeapp.admin.web.repos.jpa.RiskTemplateRepository;
import lombok.AllArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.data.domain.Pageable;


import com.safeapp.admin.web.repos.jpa.dsl.RiskTemplateDslRepos;
import com.safeapp.admin.web.repos.mongo.LikeHistoryRepos;
import com.safeapp.admin.web.service.RiskTemplateService;
import com.safeapp.admin.web.service.cmmn.JwtService;

@AllArgsConstructor
@Service
public class RiskTemplateServiceImpl implements RiskTemplateService {

    private final RiskTemplateRepository repos;

    private final RiskTemplateDslRepos dslRepos;

    private final ProjectRepos projectRepos;
    private final UserRepos userRepos;

    private final JwtService jwtService;

    private final PasswordUtil passwordUtil;

    private final DateUtil dateUtil;

    private final LikeHistoryRepos likeRepos;


    @Transactional
    @Override
    public RiskTemplate add(RiskTemplate instance, HttpServletRequest httpServletRequest) throws Exception {
        if (Objects.isNull(instance)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }
        RiskTemplate savedInstance = repos.save(instance);
        if (Objects.isNull(savedInstance)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "저장중 오류가 발생했습니다.");
        }

        return savedInstance;
    }

    @Transactional
    @Override
    public RiskTemplate edit(RiskTemplate instance, HttpServletRequest httpServletRequest)
        throws Exception {
        RiskTemplate savedInstance = repos.findById(instance.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "수정을 위해서는 키가 필요합니다."));
        if (Objects.isNull(savedInstance)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }
        for(RiskTemplateDetail detail : savedInstance.getDetails()) {
            detail.setRiskTempId(savedInstance.getId());
        }
        savedInstance = repos.save(generate(instance));
        return savedInstance;
    }

    @Override
    public void remove(long id, HttpServletRequest httpServletRequest) {
        RiskTemplate savedInstance = repos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "이미 삭제된 내용이거나 존재하지 않는 키입니다."));
        repos.delete(savedInstance);
    }

    @Override
    public RiskTemplate find(long id, HttpServletRequest httpServletRequest) throws Exception {
        RiskTemplate savedInstance = repos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "이미 삭제된 내용이거나 존재하지 않는 키입니다."));
        
        savedInstance.setViews(savedInstance.getViews() + 1);
        savedInstance = repos.save(savedInstance);
        
        return savedInstance;
    }

    @Override
    public ListResponse<RiskTemplate> findAll(RiskTemplate instance, Pages bfPage,
                                              HttpServletRequest httpServletRequest) throws Exception {

        List<RiskTemplate> list = dslRepos.findAll(instance, bfPage);
        long count = dslRepos.countAll(instance);

        Admins admin = jwtService.getAdminInfoByTokenAnyway(httpServletRequest);
        if(admin != null) {
            for(RiskTemplate item : list) {
                LikeHistory liked = likeRepos.findByUserIDAndTypeAndBoardId(item.getUser().getId(), "risk-template", item.getId());
                
                if(liked != null) {
                    item.setLiked(liked.getLiked());
                }
            }
        }

        return new ListResponse<RiskTemplate>(count, list, bfPage);
    }

    @Override
    public RiskTemplate generate(RiskTemplate instance) {
        return RiskTemplate.builder()
            .id(instance.getId())
            //.createdAt(instance.getCreatedAt() == null ? dateUtil.getThisTime() : instance.getCreatedAt())
            //.dueUserId(instance.getDueUser().getId())
            .etcRiskMemo(instance.getEtcRiskMemo())
            .instructDetail(instance.getInstructDetail())
            .instructWork(instance.getInstructWork())
            .likes(instance.getLikes())
            .name(instance.getName())

            .relatedAcidNo(instance.getRelatedAcidNo())

            .tag(instance.getTag())
//            .updatedAt(dateUtil.getThisTime())

            .views(instance.getViews())
            .visibled(instance.getVisibled() == null ? YN.Y : instance.getVisibled())
            .workEndAt(instance.getWorkEndAt())
            .workStartAt(instance.getWorkStartAt())
            .details(instance.getDetails())
            .build();
    }

    @Override
    public void addLike(long id, HttpServletRequest httpServletRequest) {
        
        Admins admin = jwtService.getAdminInfoByToken(httpServletRequest);
        
        if(admin == null) {
            throw new HttpServerErrorException(HttpStatus.UNAUTHORIZED, "먼저 로그인하여주세요.");
        }
        
        LikeHistory liked = likeRepos.findByUserIDAndTypeAndBoardId(admin.getId(), "risk-template", id);
        if(liked != null) {
            if(liked.getLiked() == YN.Y) {
                throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "이미 좋아요 하셨습니다.");
            }
            liked.setLiked(YN.Y);
        } else {
            likeRepos.save(LikeHistory.builder()
                .liked(YN.Y)
                .type("risk-template")
                .boardId(id)
                .userID(admin.getId())
                .build());
        }
    }

    @Override
    public void removeLike(long id, HttpServletRequest httpServletRequest) {

        Admins admin = jwtService.getAdminInfoByToken(httpServletRequest);
        
        if(admin == null) {
            throw new HttpServerErrorException(HttpStatus.UNAUTHORIZED, "먼저 로그인하여주세요.");
        }

        LikeHistory liked = likeRepos.findByUserIDAndTypeAndBoardId(admin.getId(), "risk-template", id);
        if(liked != null) {
            likeRepos.delete(liked);
        } else {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "좋아요를 먼저 선택해 주세요.");
        }
    }

    @Override
    public boolean isLiked(long id, HttpServletRequest httpServletRequest) {
        
        Admins admin = jwtService.getAdminInfoByToken(httpServletRequest);

        LikeHistory liked = likeRepos.findByUserIDAndTypeAndBoardId(admin.getId(), "risk-template", id);
        return liked != null && liked.getLiked() == YN.Y;
    }

    @Override
    public RiskTemplate toEntity(RequestRiskTemplateDTO dto) throws NotFoundException {
        RiskTemplate template = new RiskTemplate();
        template.setProject(projectRepos.findById(dto.getProjectId()).orElseThrow(() -> new NotFoundException("project does not exist. input project id: " + dto.getProjectId())));
        template.setChecker(userRepos.findById(dto.getCheckerId()).orElseThrow(() -> new NotFoundException("checker does not exist. input checker id: " + dto.getCheckerId())));
        template.setReviewer(userRepos.findById(dto.getReviewerId()).orElseThrow(() -> new NotFoundException("reviewer does not exist. input reviewer id: " + dto.getReviewerId())));
        template.setUser(userRepos.findById(dto.getUserId()).orElseThrow(() -> new NotFoundException("user does not exist. input user id: " + dto.getUserId())));
        template.setDueUser(userRepos.findById(dto.getDueUserId()).orElseThrow(() -> new NotFoundException("user does not exist. input user id: " + dto.getDueUserId())));
        template.setApprover(userRepos.findById(dto.getApproverId()).orElseThrow(() -> new NotFoundException("user does not exist. input user id: " + dto.getUserId())));
        template.setName(dto.getName());
        template.setVisibled(dto.getVisibled());
        template.setTag(dto.getTag());
        template.setWorkEndAt(dto.getWorkEndAt());
        template.setWorkStartAt(dto.getWorkStartAt());
        template.setInstructWork(dto.getInstructWork());
        template.setInstructDetail(dto.getInstructDetail());
        template.setEtcRiskMemo(dto.getEtcRiskMemo());
        template.setRelatedAcidNo(dto.getRelatedAcidNo());

        return template;
    }

    @Override
    public List<ResponseRiskTemplateDTO> findAllByCondition(
            Long checkerId,
            Long projectId,
            Long userId,
            String name,
            YN visibled,
            String tags,
            Pageable page
    ){
        return repos.findAllByCondition(checkerId, projectId, userId, name, visibled, tags, page);
    }
}
