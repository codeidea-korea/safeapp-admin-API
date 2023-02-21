package com.safeapp.admin.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.utils.ResponseUtil;
import com.safeapp.admin.web.data.NoticeType;
import com.safeapp.admin.web.dto.request.RequestNoticeDTO;
import com.safeapp.admin.web.dto.request.RequestNoticeEditDTO;
import com.safeapp.admin.web.dto.response.ResponseAccidentCaseDTO;
import com.safeapp.admin.web.dto.response.ResponseNoticeDTO;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.AccidentExp;
import com.safeapp.admin.web.model.entity.Notice;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safeapp.admin.web.service.NoticeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/board/notice")
@AllArgsConstructor
@Api(tags = {"Notice"}, description = "컨텐츠 관리 > 공지사항 관리")
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping(value = "/add")
    @ApiOperation(value = "공지사항 등록", notes = "공지사항 등록")
    public ResponseEntity<ResponseNoticeDTO> add(@RequestBody RequestNoticeDTO addDto, HttpServletRequest request) throws Exception {
        Notice addedNotice = noticeService.add(noticeService.toAddEntity(addDto), request);
        return new ResponseEntity<>(ResponseNoticeDTO.builder().notice(addedNotice).build(), OK);
    }

    @GetMapping(value = "/find/{id}")
    @ApiOperation(value = "공지사항 단독 조회", notes = "공지사항 단독 조회")
    public ResponseEntity<ResponseNoticeDTO> find(@PathVariable("id") @ApiParam(value = "공지사항 PK", required = true) long id,
            HttpServletRequest request) throws Exception {

        Notice notice = noticeService.find(id, request);
        return new ResponseEntity<>(ResponseNoticeDTO.builder().notice(notice).build(), OK);
    }

    @PutMapping(value = "/edit/{id}")
    @ApiOperation(value = "공지사항 수정", notes = "공지사항 수정")
    public ResponseEntity<ResponseNoticeDTO> edit(@PathVariable("id") @ApiParam(value = "공지사항 PK", required = true) long id,
            @RequestBody RequestNoticeEditDTO editDto, HttpServletRequest request) throws Exception {

        Notice newNotice = noticeService.toEditEntity(editDto);
        newNotice.setId(id);

        Notice editedNotice = noticeService.edit(newNotice, request);
        return new ResponseEntity<>(ResponseNoticeDTO.builder().notice(editedNotice).build(), OK);
    }

    @DeleteMapping(value = "/remove/{id}")
    @ApiOperation(value = "공지사항 삭제", notes = "공지사항 삭제")
    public ResponseEntity remove(@PathVariable("id") @ApiParam(value = "공지사항 PK", required = true) long id,
            HttpServletRequest request) throws Exception {

        noticeService.remove(id, request);
        return ResponseUtil.sendResponse(null);
    }

    @GetMapping(value = "/list")
    @ApiOperation(value = "공지사항 목록 조회", notes = "공지사항 목록 조회")
    public ResponseEntity<List<ResponseNoticeDTO>> findAll(
            @RequestParam(value = "type", required = false) @Parameter(description = "유형") NoticeType type,
            @RequestParam(value = "pageNo", defaultValue = "1") @Parameter(description = "현재 페이지 번호") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") @Parameter(description = "1 페이지 당 목록 수") int pageSize,
            HttpServletRequest request) throws Exception {

        Pages pages = new Pages(pageNo, pageSize);
        return
            ResponseUtil.sendResponse(noticeService.findAll(
                Notice.builder()
                .type(type)
                .build(), pages, request)
            );
    }

}