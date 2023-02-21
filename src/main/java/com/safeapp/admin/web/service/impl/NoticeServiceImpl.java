package com.safeapp.admin.web.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.utils.DateUtil;
import com.safeapp.admin.utils.PasswordUtil;
import com.safeapp.admin.web.dto.request.RequestAccidentCaseDTO;
import com.safeapp.admin.web.dto.request.RequestNoticeDTO;
import com.safeapp.admin.web.dto.request.RequestNoticeEditDTO;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.AccidentExp;
import com.safeapp.admin.web.model.entity.Notice;
import com.safeapp.admin.web.model.entity.cmmn.Files;
import com.safeapp.admin.web.repos.jpa.AdminRepos;
import com.safeapp.admin.web.repos.jpa.NoticeRepos;
import com.safeapp.admin.web.service.cmmn.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.safeapp.admin.web.repos.jpa.dsl.NoticeDslRepos;
import com.safeapp.admin.web.service.NoticeService;
import com.safeapp.admin.web.service.cmmn.JwtService;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepos noticeRepos;
    private final NoticeDslRepos noticeDslRepos;
    private final AdminRepos adminRepos;

    @Override
    public Notice toAddEntity(RequestNoticeDTO addDto) {
        Notice newNotice = new Notice();

        newNotice.setAdmin(adminRepos.findById(addDto.getAdminId()).orElse(null));
        newNotice.setCreatedAt(LocalDateTime.now());
        newNotice.setType(addDto.getType());
        newNotice.setPriority(addDto.getPriority());
        newNotice.setTitle(addDto.getTitle());
        newNotice.setContents(addDto.getContents());

        return newNotice;
    }

    @Override
    @Transactional
    public Notice add(Notice newNotice, HttpServletRequest request) throws Exception {
        if(Objects.isNull(newNotice)) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 공지사항입니다.");
        }

        Notice addedNotice = noticeRepos.save(newNotice);
        if(Objects.isNull(addedNotice)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "DB 저장 중 오류가 발생하였습니다.");
        }

        return addedNotice;
    }

    @Override
    public Notice find(long id, HttpServletRequest request) throws Exception {
        Notice notice =
            noticeRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 공지사항입니다."));

        return notice;
    }

    @Override
    public Notice toEditEntity(RequestNoticeEditDTO editDto) {
        Notice newNotice = new Notice();

        newNotice.setUpdatedAt(LocalDateTime.now());
        newNotice.setType(editDto.getType());
        newNotice.setPriority(editDto.getPriority());
        newNotice.setTitle(editDto.getTitle());
        newNotice.setContents(editDto.getContents());

        return newNotice;
    }

    @Override
    public Notice edit(Notice newNotice, HttpServletRequest request) throws Exception {
        Notice notice =
            noticeRepos.findById(newNotice.getId())
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 공지사항입니다."));

        notice.edit(newNotice);

        Notice editedNotice = noticeRepos.save(notice);
        return editedNotice;
    }

    @Override
    public void remove(long id, HttpServletRequest request) {
        Notice notice =
            noticeRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 공지사항입니다."));

        notice.setDeleteYn(true);
        noticeRepos.save(notice);
    }

    @Override
    public ListResponse<Notice> findAll(Notice notice, Pages pages, HttpServletRequest request) throws Exception {
        long count = noticeDslRepos.countAll(notice);
        List<Notice> list = noticeDslRepos.findAll(notice, pages);

        return new ListResponse<>(count, list, pages);
    }

    @Override
    public Notice generate(Notice newNotice) { return null; }

}