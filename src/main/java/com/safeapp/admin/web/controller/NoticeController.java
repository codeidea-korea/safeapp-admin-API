package com.safeapp.admin.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.safeapp.admin.utils.ResponseUtil;
import com.safeapp.admin.web.data.NoticeType;
import com.safeapp.admin.web.dto.request.RequestNoticeDTO;
import com.safeapp.admin.web.dto.request.RequestNoticeEditDTO;
import com.safeapp.admin.web.dto.response.ResponseAccidentCaseDTO;
import com.safeapp.admin.web.dto.response.ResponseNoticeDTO;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.AccidentExp;
import com.safeapp.admin.web.model.entity.Inquiry;
import com.safeapp.admin.web.model.entity.Notice;
import com.safeapp.admin.web.model.entity.NoticeFiles;
import com.safeapp.admin.web.model.entity.cmmn.Files;
import com.safeapp.admin.web.repos.jpa.NoticeFilesRepository;
import com.safeapp.admin.web.service.cmmn.FileService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.safeapp.admin.web.service.NoticeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/board/notice")
@AllArgsConstructor
@Api(tags = {"Notice"}, description = "컨텐츠 관리 > 공지사항 관리")
public class NoticeController {

    private final NoticeService noticeService;
    private final NoticeFilesRepository noticeFileRepos;

    @PostMapping(value = "/add")
    @ApiOperation(value = "공지사항 등록", notes = "공지사항 등록")
    public ResponseEntity<ResponseNoticeDTO> add(@RequestBody RequestNoticeDTO addDto, HttpServletRequest request) throws Exception {
        Notice addedNotice = noticeService.add(noticeService.toAddEntity(addDto), request);
        return new ResponseEntity<>(ResponseNoticeDTO.builder().notice(addedNotice).build(), OK);
    }

    @PostMapping(value = "/add/{id}/files", consumes = "multipart/form-data")
    @ApiOperation(value = "공지사항 첨부파일 등록")
    public ResponseEntity addFiles(
            @PathVariable("id") @ApiParam(value = "공지사항 PK", required = true) long id,
            @RequestPart(required = false) List<MultipartFile> files,
            HttpServletRequest request) throws Exception {

        noticeService.addFiles(id, files, request);
        return new ResponseEntity(null, CREATED);
    }

    @GetMapping(value = "/find/{id}")
    @ApiOperation(value = "공지사항 단독 조회", notes = "공지사항 단독 조회")
    public ResponseEntity<ResponseNoticeDTO> find(@PathVariable("id") @ApiParam(value = "공지사항 PK", required = true) long id,
            HttpServletRequest request) throws Exception {

        ResponseNoticeDTO notice = noticeService.findNotice(id, request);
        return new ResponseEntity<>(notice, OK);
    }

    @GetMapping(value = "/download/{id}")
    @ApiOperation(value = "공지사항 첨부파일 다운로드", notes = "공지사항 첨부파일 다운로드")
    public void download(@PathVariable("id") @ApiParam(value = "공지사항 파일 PK", required = true) long id,
            HttpServletResponse response) throws Exception {

        NoticeFiles noticeFile =
            noticeFileRepos.findById(id)
            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 공지사항 첨부파일입니다."));
        String filePath = "/home/safeapp/api" + noticeFile.getUrl();

        File file = new File(filePath);
        if(!file.exists()) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "다운로드할 첨부파일이 존재하지 않습니다.");
        } else {
            byte[] fileByte = FileUtils.readFileToByteArray(file);

            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(noticeFile.getId().toString(), "UTF-8") + "\";");
            response.setHeader("Content-Transfer-Encoding", "binary");

            response.getOutputStream().write(fileByte);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        }
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

    @DeleteMapping(value = "/file/remove/{id}")
    @ApiOperation(value = "공지사항 첨부파일 삭제", notes = "공지사항 첨부파일 삭제")
    public ResponseEntity removeFile(@PathVariable("id") @ApiParam(value = "공지사항 첨부파일 PK", required = true) long id,
        HttpServletRequest request) throws Exception {

        noticeService.removeFile(id, request);
        return ResponseUtil.sendResponse(null);
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
            ResponseUtil.sendResponse(noticeService.findAllByCondition(
                Notice.builder()
                .type(type)
                .build(), pages, request)
            );
    }

}