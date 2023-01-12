package com.binoofactory.cornsqure.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.binoofactory.cornsqure.web.model.cmmn.BfListResponse;
import com.binoofactory.cornsqure.web.model.cmmn.BfPage;
import com.binoofactory.cornsqure.web.model.entity.Auth;
import com.binoofactory.cornsqure.web.service.AuthService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/auth")
@Api(tags = {"Auth"}, description = "권한", basePath = "/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "")
    @ApiOperation(value = "등록", notes = "등록")
    public Auth add(
        @RequestBody Auth params,
        HttpServletRequest request) throws Exception {
        return authService.add(params, request);
    }

    @PutMapping(value = "/{id}")
    @ApiOperation(value = "수정", notes = "수정")
    public Auth modify(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        @RequestBody Auth params,
        HttpServletRequest request) throws Exception {

        params.setId(id);
        return authService.edit(params, request);
    }

    @DeleteMapping(value = "")
    @ApiOperation(value = "삭제", notes = "삭제")
    public void remove(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        authService.remove(id, request);
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "조회 (단건)", notes = "조회 (단건)")
    public Auth find(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        return authService.find(id, request);
    }

    @GetMapping(value = "")
    @ApiOperation(value = "목록 조회 (다건)", notes = "목록 조회 (다건)")
    public BfListResponse findAll(
        BfPage bfPage,
        @RequestParam(value = "name", required = false, defaultValue = "아이언맨") String name,
        @RequestParam(value = "allowedMenu", required = false, defaultValue = "jpg") String allowedMenu,
        HttpServletRequest request) throws Exception {
        return authService.findAll(
            Auth.builder()
                .name(name)
                .allowedMenu(allowedMenu)
                .build(),
            bfPage,
            request);
    }
}
