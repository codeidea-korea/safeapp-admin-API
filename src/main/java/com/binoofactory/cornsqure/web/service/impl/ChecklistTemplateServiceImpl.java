package com.binoofactory.cornsqure.web.service.impl;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.binoofactory.cornsqure.web.dto.request.RequestChecklistTemplateDTO;
import com.binoofactory.cornsqure.web.dto.response.ResponseChecklistProjectDTO;
import com.binoofactory.cornsqure.web.dto.response.ResponseChecklistProjectDetailDTO;
import com.binoofactory.cornsqure.web.dto.response.ResponseChecklistTemplateDTO;
import com.binoofactory.cornsqure.web.repos.jpa.ProjectRepos;
import com.binoofactory.cornsqure.web.repos.jpa.UserRepos;
import lombok.AllArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.binoofactory.cornsqure.utils.DateUtil;
import com.binoofactory.cornsqure.utils.PasswordUtil;
import com.binoofactory.cornsqure.web.data.YN;
import com.binoofactory.cornsqure.web.model.cmmn.BfListResponse;
import com.binoofactory.cornsqure.web.model.cmmn.BfPage;
import com.binoofactory.cornsqure.web.model.docs.LikeHistory;
import com.binoofactory.cornsqure.web.model.entity.ChecklistProject;
import com.binoofactory.cornsqure.web.model.entity.ChecklistProjectDetail;
import com.binoofactory.cornsqure.web.model.entity.ChecklistTemplate;
import com.binoofactory.cornsqure.web.model.entity.ChecklistTemplateDetail;
import com.binoofactory.cornsqure.web.model.entity.Users;
import com.binoofactory.cornsqure.web.repos.jpa.ChecklistTemplateRepository;
import com.binoofactory.cornsqure.web.repos.jpa.dsl.ChecklistTemplateDslRepos;
import com.binoofactory.cornsqure.web.repos.mongo.LikeHistoryRepos;
import com.binoofactory.cornsqure.web.service.ChecklistTemplateService;
import com.binoofactory.cornsqure.web.service.cmmn.UserJwtService;

@AllArgsConstructor
@Service
public class ChecklistTemplateServiceImpl implements ChecklistTemplateService {

    private final ChecklistTemplateRepository repos;

    private final ChecklistTemplateDslRepos dslRepos;

    private final UserJwtService userJwtService;

    private final PasswordUtil passwordUtil;

    private final DateUtil dateUtil;

    private final LikeHistoryRepos likeRepos;
    private final UserRepos userRepos;
    private final ProjectRepos projectRepos;

    @Transactional
    @Override
    public ChecklistTemplate add(ChecklistTemplate instance, HttpServletRequest httpServletRequest) throws Exception {
        if (Objects.isNull(instance)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }
        ChecklistTemplate savedInstance = repos.save(instance);
        if (Objects.isNull(savedInstance)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "저장중 오류가 발생했습니다.");
        }
        return savedInstance;
    }

    @Transactional
    @Override
    public ChecklistTemplate edit(ChecklistTemplate instance, HttpServletRequest httpServletRequest) throws Exception {
        ChecklistTemplate savedInstance = repos.findById(instance.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "수정을 위해서는 키가 필요합니다."));
        if (Objects.isNull(savedInstance)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "정보가 없습니다.");
        }
        savedInstance = repos.save(generate(instance));
        return savedInstance;
    }

    @Override
    public void remove(long id, HttpServletRequest httpServletRequest) {
        ChecklistTemplate savedInstance = repos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "이미 삭제된 내용이거나 존재하지 않는 키입니다."));
        repos.delete(savedInstance);
    }

    @Override
    public ChecklistTemplate find(long id, HttpServletRequest httpServletRequest) throws Exception {
        ChecklistTemplate savedInstance = repos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "이미 삭제된 내용이거나 존재하지 않는 키입니다."));
        
        savedInstance.setViews(savedInstance.getViews() + 1);
        savedInstance = repos.save(savedInstance);
        
        return savedInstance;
    }

    @Override
    public BfListResponse<ChecklistTemplate> findAll(ChecklistTemplate instance, BfPage bfPage,
        HttpServletRequest httpServletRequest) throws Exception {

        List<ChecklistTemplate> list = dslRepos.findAll(instance, bfPage);
        long count = dslRepos.countAll(instance);

        Users user = userJwtService.getUserInfoByTokenAnyway(httpServletRequest);
        if(user != null) {
            for(ChecklistTemplate item : list) {
                LikeHistory liked = likeRepos.findByUserIdAndTypeAndBoardId(item.getUser().getId(), "checklist-template", item.getId());
                
                if(liked != null) {
                    item.setLiked(liked.getLiked());
                }
            }
        }

        return new BfListResponse<ChecklistTemplate>(list, count, bfPage);
    }

    @Override
    public ChecklistTemplate generate(ChecklistTemplate instance) {
        return ChecklistTemplate.builder()
            .id(instance.getId())
            .name(instance.getName())
            .relatedAcidNo(instance.getRelatedAcidNo())

            .tag(instance.getTag())
            .details(instance.getDetails())
            .build();
    }

    @Override
    public void addLike(long id, HttpServletRequest httpServletRequest) {
        
        Users user = userJwtService.getUserInfoByToken(httpServletRequest);
        
        if(user == null) {
            throw new HttpServerErrorException(HttpStatus.UNAUTHORIZED, "먼저 로그인하여주세요.");
        }
        
        LikeHistory liked = likeRepos.findByUserIdAndTypeAndBoardId(user.getId(), "checklist-template", id);
        if(liked != null) {
            if(liked.getLiked() == YN.Y) {
                throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "이미 좋아요 하셨습니다.");
            }
            liked.setLiked(YN.Y);
        } else {
            likeRepos.save(LikeHistory.builder()
                .liked(YN.Y)
                .type("checklist-template")
                .boardId(id)
                .userId(user.getId())
                .build());
        }
    }

    @Override
    public void removeLike(long id, HttpServletRequest httpServletRequest) {
        
        Users user = userJwtService.getUserInfoByToken(httpServletRequest);
        
        if(user == null) {
            throw new HttpServerErrorException(HttpStatus.UNAUTHORIZED, "먼저 로그인하여주세요.");
        }

        LikeHistory liked = likeRepos.findByUserIdAndTypeAndBoardId(user.getId(), "checklist-template", id);
        if(liked != null) {
            likeRepos.delete(liked);
        } else {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "좋아요를 먼저 선택해 주세요.");
        }
    }

    @Override
    public boolean isLiked(long id, HttpServletRequest httpServletRequest) {
        
        Users user = userJwtService.getUserInfoByToken(httpServletRequest);

        LikeHistory liked = likeRepos.findByUserIdAndTypeAndBoardId(user.getId(), "checklist-template", id);
        return liked != null && liked.getLiked() == YN.Y;
    }

    @Override
    public ChecklistTemplate toEntity(RequestChecklistTemplateDTO dto) throws NotFoundException {
        ChecklistTemplate template = new ChecklistTemplate();
        template.setProject(projectRepos.findById(dto.getProjectId()).orElseThrow(() -> new NotFoundException("project does not exist. input project id: " + dto.getProjectId())));
        template.setChecker(userRepos.findById(dto.getCheckerId()).orElseThrow(() -> new NotFoundException("checker does not exist. input checker id: " + dto.getCheckerId())));
        template.setReviewer(userRepos.findById(dto.getReviewerId()).orElseThrow(() -> new NotFoundException("reviewer does not exist. input reviewer id: " + dto.getReviewerId())));
        template.setUser(userRepos.findById(dto.getUserId()).orElseThrow(() -> new NotFoundException("user does not exist. input user id: " + dto.getUserId())));
        template.setApprover(userRepos.findById(dto.getApproverId()).orElseThrow(() -> new NotFoundException("user does not exist. input user id: " + dto.getUserId())));
        template.setName(dto.getName());
        template.setRelatedAcidNo(dto.getRelatedAcidNo());
        template.setTag(dto.getTag());

        return template;
    }

    @Override
    public List<ResponseChecklistTemplateDTO> findAllByCondition(
            Long userId,
            Long projectId,
            String name,
            String tag,
            YN created_at_descended,
            YN views_descended,
            YN likes_descended,
            String detail_contents,
            Pageable page
    ){
        return repos.findAllByCondition(userId, projectId, name, tag, created_at_descended, views_descended, likes_descended, detail_contents, page);
    }
}
