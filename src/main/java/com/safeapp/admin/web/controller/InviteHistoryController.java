package com.safeapp.admin.web.controller;

import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.utils.ResponseUtil;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.BfPage;
import com.safeapp.admin.web.model.docs.InviteHistory;
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

import com.safeapp.admin.web.service.InviteHistoryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/invite")
@Api(tags = {"InviteHistory"}, description = "초대", basePath = "/invite")
public class InviteHistoryController {

    private final InviteHistoryService inviteHistoryService;

    @Autowired
    public InviteHistoryController(InviteHistoryService inviteHistoryService) {
        this.inviteHistoryService = inviteHistoryService;
    }

    @PostMapping(value = "/invite/user")
    @ApiOperation(value = "등록", notes = "등록")
    public ResponseEntity add(
        @RequestBody InviteHistory params,
        HttpServletRequest request) throws Exception {
        return ResponseUtil.sendResponse(inviteHistoryService.add(params, request));
    }

    // 회원 초대 코드 발송 <- 초대코드로 들어온 경우, 회원 그룹 편입.
    @PostMapping(value = "/invite/users")
    @ApiOperation(value = "이메일 초청 벌크", notes = "이메일 초청 벌크")
    public ResponseEntity addAll(
        @RequestParam(value = "group_id", required = true) long group_id,
        @RequestParam(value = "contents", required = true) String contents,
        @RequestParam(value = "user_mails", required = true) List<String> user_mails,
        HttpServletRequest request) throws Exception {
        
        for(String userMail : user_mails) {
            InviteHistory inviteHistory = InviteHistory.builder()
                .contents(contents)
                .groupId(group_id)
                .userMail(userMail)
                .groupName(group_id + "")
                .urlData(Base64.getEncoder().encodeToString((group_id + "-" + userMail).getBytes()))
                .build();
            
            inviteHistoryService.add(inviteHistory, request);
        }
        
        return ResponseUtil.sendResponse(true);
    }

    @PutMapping(value = "/invite/user/{id}")
    @ApiOperation(value = "수정", notes = "수정")
    public ResponseEntity modify(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) String id,
        @RequestBody InviteHistory params,
        HttpServletRequest request) throws Exception {

        params.setId(id);
        return ResponseUtil.sendResponse(inviteHistoryService.edit(params, request));
    }

    @DeleteMapping(value = "/invite/user/{id}")
    @ApiOperation(value = "삭제", notes = "삭제")
    public ResponseEntity remove(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) String id,
        HttpServletRequest request) throws Exception {
        inviteHistoryService.remove(id, request);
        return ResponseUtil.sendResponse(null);
    }

    @GetMapping(value = "/invite/user/{id}")
    @ApiOperation(value = "조회 (단건)", notes = "조회 (단건)")
    public ResponseEntity find(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) String id,
        HttpServletRequest request) throws Exception {
        return ResponseUtil.sendResponse(inviteHistoryService.find(id, request));
    }

    @GetMapping(value = "/invite/users")
    @ApiOperation(value = "목록 조회 (다건)", notes = "목록 조회 (다건)")
    public ResponseEntity<ListResponse> findAll(
        BfPage bfPage,
        HttpServletRequest request) throws Exception {
        return ResponseUtil.sendResponse(inviteHistoryService.findAll(
            InviteHistory.builder()
                .build(),
            bfPage,
            request));
    }
}
