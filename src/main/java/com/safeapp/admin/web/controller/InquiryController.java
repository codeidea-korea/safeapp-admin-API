package com.safeapp.admin.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.safeapp.admin.utils.ResponseUtil;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.request.RequestInquiryAnswerDTO;
import com.safeapp.admin.web.dto.response.ResponseInquiryDTO;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.Inquiry;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.safeapp.admin.web.service.InquiryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.client.HttpServerErrorException;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/board/inquiry")
@AllArgsConstructor
@Api(tags = {"Inquiry"}, description = "고객센터 관리")
@Slf4j
public class InquiryController {

    private final InquiryService inquiryService;

    @PostMapping(value = "/add")
    @ApiOperation(value = "고객센터 등록", notes = "고객센터 등록")
    public ResponseEntity add(@RequestBody Inquiry newInquiry, HttpServletRequest request) throws Exception {

        return ResponseUtil.sendResponse(inquiryService.add(newInquiry, request));
    }

    @GetMapping(value = "/find/{id}")
    @ApiOperation(value = "고객센터 단독 조회", notes = "고객센터 단독 조회")
    public ResponseEntity<ResponseInquiryDTO> find(@PathVariable("id") @ApiParam(value = "고객센터 PK", required = true) long id,
            HttpServletRequest request) throws Exception {

        Inquiry inquiry = inquiryService.find(id, request);
        return new ResponseEntity<>(ResponseInquiryDTO.builder().inquiry(inquiry).build(), OK);
    }

    @GetMapping(value = "/download/{id}")
    @ApiOperation(value = "고객센터 단독 파일 다운로드", notes = "고객센터 단독 파일 다운로드")
    public void download(@PathVariable("id") @ApiParam(value = "고객센터 PK", required = true) long id,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Inquiry inquiry = inquiryService.find(id, request);

        String filePath = "/home/safeapp/api" + inquiry.getAttachment();
        File file = new File(filePath);
        if(!file.exists()) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "다운로드할 파일이 존재하지 않습니다.");
        } else {
            byte[] fileByte = FileUtils.readFileToByteArray(file);

            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(inquiry.getAttachmentName(), "UTF-8") + "\";");
            response.setHeader("Content-Transfer-Encoding", "binary");

            response.getOutputStream().write(fileByte);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        }
    }

    /*
    @PutMapping(value = "/edit/{id}")
    @ApiOperation(value = "고객센터 수정", notes = "고객센터 수정")
    public ResponseEntity edit(@PathVariable("id") @ApiParam(value = "고객센터 PK", required = true) long id,
            @RequestBody Inquiry newInquiry, HttpServletRequest request) throws Exception {

        newInquiry.setId(id);
        return ResponseUtil.sendResponse(inquiryService.edit(newInquiry, request));
    }
    */

    @DeleteMapping(value = "/remove/{id}")
    @ApiOperation(value = "고객센터 삭제", notes = "고객센터 삭제")
    public ResponseEntity remove(@PathVariable("id") @ApiParam(value = "고객센터 PK", required = true) long id,
            HttpServletRequest request) throws Exception {

        inquiryService.remove(id, request);
        return ResponseUtil.sendResponse(null);
    }

    @GetMapping(value = "/list")
    @ApiOperation(value = "고객센터 목록 조회", notes = "고객센터 목록 조회")
    public ResponseEntity<List<ResponseInquiryDTO>> findAll(
            @RequestParam(value = "isAnswer", required = false) @Parameter(description = "답변 여부") YN isAnswer,
            @RequestParam(value = "pageNo", defaultValue = "1") @Parameter(description = "현재 페이지 번호") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") @Parameter(description = "1 페이지 당 목록 수") int pageSize,
            HttpServletRequest request) throws Exception {

        Pages pages = new Pages(pageNo, pageSize);
        return
            ResponseUtil.sendResponse(inquiryService.findAll(
                Inquiry.builder()
                .isAnswer(isAnswer)
                .build(), pages, request)
            );
    }

    @PutMapping(value = "/answer/{id}")
    @ApiOperation(value = "고객센터 답변 등록", notes = "고객센터 답변 등록")
    public ResponseEntity answer(@PathVariable("id") @ApiParam(value = "고객센터 PK", required = true) long id,
            @RequestBody RequestInquiryAnswerDTO answerDto, HttpServletRequest request) throws Exception {

        Inquiry newInquiry = inquiryService.toAnswerEntity(answerDto);
        newInquiry.setId(id);

        Inquiry answerdInquiry = inquiryService.answer(newInquiry, request);
        return new ResponseEntity<>(ResponseInquiryDTO.builder().inquiry(answerdInquiry).build(), OK);
    }
    
}
