package com.safeapp.admin.web.service.impl;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.dto.request.RequestAccidentCaseDTO;
import com.safeapp.admin.web.repos.jpa.AdminRepos;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.AccidentExp;
import com.safeapp.admin.web.repos.jpa.AccidentExpRepos;
import lombok.AllArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.safeapp.admin.web.repos.jpa.dsl.AccidentExpDslRepos;
import com.safeapp.admin.web.service.AccidentExpService;

@Service
@AllArgsConstructor
public class AccidentExpServiceImpl implements AccidentExpService {

    private final AdminRepos adminRepos;
    private final AccidentExpRepos repos;
    private final AccidentExpDslRepos dslRepos;

    @Transactional
    @Override
    public AccidentExp add(AccidentExp instance, HttpServletRequest httpServletRequest) throws Exception {
        if(Objects.isNull(instance)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "등록할 [사고 사례]를 먼저 입력해주세요.");
        }

        AccidentExp savedInstance = repos.save(instance);
        if(Objects.isNull(savedInstance)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "저장 중 오류가 발생하였습니다.");
        }

        return savedInstance;
    }

    @Override
    public AccidentExp edit(AccidentExp instance, HttpServletRequest httpServletRequest) throws Exception {
        // 해당 키가 존재하지 않음
        AccidentExp savedInstance =
                repos.findById(instance.getId()).orElseThrow(()
                        -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "수정하실 [사고 사례]가 존재하지 않습니다."));

        // 해당 키의 사고 사례 자체는 존재하나 사고 사례에 대한 내용이 아무것도 존재하지 않음
        if(Objects.isNull(savedInstance)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "수정하실 [사고 사례]가 존재하지 않습니다.");
        }

        savedInstance = repos.save(generate(instance));
        return savedInstance;
    }

    @Override
    public void remove(long id, HttpServletRequest httpServletRequest) {
        AccidentExp savedInstance =
                repos.findById(id).orElseThrow(()
                        -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "삭제하실 [사고 사례]가 존재하지 않습니다."));

        repos.delete(savedInstance);
    }

    @Override
    public AccidentExp find(long id, HttpServletRequest httpServletRequest) throws Exception {
        AccidentExp savedInstance =
                repos.findById(id).orElseThrow(() 
                        -> new HttpServerErrorException(HttpStatus.BAD_REQUEST,
                        "이미 삭제된 [사고 사례]이거나, 삭제하실 [사고 사례]가 존재하지 않습니다."));
        return savedInstance;
    }

    @Override
    public ListResponse<AccidentExp> findAll(AccidentExp instance, Pages bfPage,
            HttpServletRequest httpServletRequest) throws Exception {

        long count = dslRepos.countAll(instance);
        List<AccidentExp> list = dslRepos.findAll(instance, bfPage);

        return new ListResponse<>(count, list, bfPage);
    }

    @Override
    public AccidentExp generate(AccidentExp instance) {
        return AccidentExp.builder()
            .accidentAt(instance.getAccidentAt())
            .accidentCause(instance.getAccidentCause())
            .accidentReason(instance.getAccidentReason())
            .accidentUid(instance.getAccidentUid())
            .causeDetail(instance.getCauseDetail())
            .id(instance.getId())
            .image(instance.getImage())
            .name(instance.getName())
            .response(instance.getResponse())
            .tags(instance.getTags())
            .title(instance.getTitle())
            .id(instance.getAdmin().getId())
            .views(instance.getViews())
            .build();
    }

    @Override
    public AccidentExp toEntity(RequestAccidentCaseDTO dto) throws NotFoundException {
        AccidentExp accidentExp = new AccidentExp();
        accidentExp.setTitle(dto.getTitle());
        accidentExp.setAdmin(adminRepos.findById(dto.getUserId()).orElseThrow(() -> new NotFoundException("dueuser does not exist. input checker id: " + dto.getUserId())));
        accidentExp.setTags(dto.getTags());
        accidentExp.setName(dto.getName());
        accidentExp.setAccidentAt(dto.getAccidentAt());
        accidentExp.setAccidentUid(dto.getAccidentUid());
        accidentExp.setAccidentReason(dto.getAccidentReason());

        return accidentExp;
    }
}
