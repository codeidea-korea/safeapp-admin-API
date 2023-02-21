package com.safeapp.admin.web.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.utils.DateUtil;
import com.safeapp.admin.utils.PasswordUtil;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.request.RequestInquiryAnswerDTO;
import com.safeapp.admin.web.dto.request.RequestNoticeEditDTO;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.Inquiry;
import com.safeapp.admin.web.model.entity.Notice;
import com.safeapp.admin.web.repos.jpa.AdminRepos;
import com.safeapp.admin.web.repos.jpa.InquiryRepos;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.safeapp.admin.web.repos.jpa.dsl.InquiryDslRepos;
import com.safeapp.admin.web.service.InquiryService;
import com.safeapp.admin.web.service.cmmn.JwtService;

@Service
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {

    private final InquiryRepos inquiryRepos;
    private final InquiryDslRepos inquiryDslRepos;
    private final AdminRepos adminRepos;

    @Override
    @Transactional
    public Inquiry add(Inquiry newInquiry, HttpServletRequest request) throws Exception {
        /*
        if(Objects.isNull(newInquiry)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 고객센터입니다.");
        }

        Inquiry addedInquiry = inquiryRepos.save(newInquiry);
        if(Objects.isNull(addedInquiry)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "DB 저장 중 오류가 발생하였습니다.");
        }
        
        return addedInquiry;
        */

        return null;
    }

    @Override
    public Inquiry find(long id, HttpServletRequest request) throws Exception {
        Inquiry inquiry =
            inquiryRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 고객센터입니다."));

        return inquiry;
    }

    @Override
    public Inquiry edit(Inquiry newInquiry, HttpServletRequest request) throws Exception {
        /*
        Inquiry inquiry =
            inquiryRepos.findById(newInquiry.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 고객센터입니다."));

        inquiry.edit(newInquiry);

        Inquiry editedInquiry = inquiryRepos.save(inquiry);
        return editedInquiry;
        */

        return null;
    }

    @Override
    public void remove(long id, HttpServletRequest request) {
        Inquiry inquiry =
            inquiryRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 고객센터입니다."));

        inquiry.setDeleteYn(true);
        inquiryRepos.save(inquiry);
    }

    @Override
    public ListResponse<Inquiry> findAll(Inquiry inquiry, Pages pages, HttpServletRequest request) throws Exception {
        long count = inquiryDslRepos.countAll(inquiry);
        List<Inquiry> list = inquiryDslRepos.findAll(inquiry, pages);

        return new ListResponse<>(count, list, pages);
    }

    @Override
    public Inquiry toAnswerEntity(RequestInquiryAnswerDTO answerDto) {
        Inquiry newInquiry = new Inquiry();

        newInquiry.setIsAnswer(YN.Y);
        newInquiry.setAnswerAt(LocalDateTime.now());
        newInquiry.setAnswerAdmin(adminRepos.findById(answerDto.getAnswerAdminId()).orElse(null));
        newInquiry.setAnswer(answerDto.getAnswer());

        return newInquiry;
    }

    @Override
    public Inquiry answer(Inquiry newInquiry, HttpServletRequest request) {
        Inquiry inquiry =
            inquiryRepos.findById(newInquiry.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 고객센터입니다."));

        inquiry.answer(newInquiry);

        Inquiry answerdInquiry = inquiryRepos.save(inquiry);
        return answerdInquiry;
    }

    @Override
    public Inquiry generate(Inquiry newInquiry) { return null; }

}