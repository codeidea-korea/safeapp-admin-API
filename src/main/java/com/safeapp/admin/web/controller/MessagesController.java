package com.safeapp.admin.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.utils.ResponseUtil;
import com.safeapp.admin.web.data.UserType;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.cmmn.BfListResponse;
import com.safeapp.admin.web.model.cmmn.BfPage;
import com.safeapp.admin.web.model.entity.Messages;
import com.safeapp.admin.web.model.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.safeapp.admin.web.service.MessagesService;
import com.safeapp.admin.web.service.cmmn.JwtService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/messages/user-notices")
@Api(tags = {"Messages"}, description = "알림", basePath = "/messages/user-notices")
public class MessagesController {

    private final MessagesService messagesService;

    private final JwtService jwtService;

    @Autowired
    public MessagesController(MessagesService messagesService, JwtService jwtService) {
        this.messagesService = messagesService;
        this.jwtService = jwtService;
    }

    @PostMapping(value = "/")
    @ApiOperation(value = "등록", notes = "등록")
    public ResponseEntity add(
        @RequestBody Messages params,
        HttpServletRequest request) throws Exception {
        return ResponseUtil.sendResponse(messagesService.add(params, request));
    }

    @PutMapping(value = "/{id}")
    @ApiOperation(value = "수정", notes = "수정")
    public ResponseEntity modify(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        @RequestBody Messages params,
        HttpServletRequest request) throws Exception {

        params.setId(id);
        return ResponseUtil.sendResponse(messagesService.edit(params, request));
    }

    @PatchMapping(value = "/{id}/viewed")
    @ApiOperation(value = "읽음", notes = "읽음")
    public ResponseEntity read(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        @RequestBody Messages params,
        HttpServletRequest request) throws Exception {

        Messages old = messagesService.find(id, request);
        if(old == null) {
            return ResponseUtil.sendResponse(HttpStatus.BAD_REQUEST, "없는 자료입니다.");
        }
        old.setViewed(YN.Y);
        return ResponseUtil.sendResponse(messagesService.edit(old, request));
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "삭제", notes = "삭제")
    public ResponseEntity remove(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        messagesService.remove(id, request);
        return ResponseUtil.sendResponse(null);
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "조회 (단건)", notes = "조회 (단건)")
    public ResponseEntity find(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        return ResponseUtil.sendResponse(messagesService.find(id, request));
    }

    @GetMapping(value = "")
    @ApiOperation(value = "목록 조회 (다건)", notes = "목록 조회 (다건)")
    public ResponseEntity<BfListResponse> findAll(
        BfPage bfPage,
        HttpServletRequest request) throws Exception {

        Users user = jwtService.getUserInfoByToken(request);

        Messages searchParam = new Messages();
        if (user.getType() != UserType.ADMIN) {
            searchParam.setUser(user);
        }
        // 내 모든 알림
        return ResponseUtil.sendResponse(messagesService.findAll(
            searchParam,
            bfPage,
            request));
    }
}
