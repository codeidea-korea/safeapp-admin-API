package com.safeapp.admin.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.utils.ResponseUtil;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.dto.response.ResponseCheckListProjectDTO;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.Auth;
import com.safeapp.admin.web.model.entity.UserAuth;
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

import com.safeapp.admin.web.service.MembershipService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/membership")
@AllArgsConstructor
@Api(tags = {"Membership"}, description = "멤버쉽 결제 관리")
public class MembershipController {

    private final MembershipService membershipService;

    /*
    @PostMapping(value = "/add")
    @ApiOperation(value = "멤버쉽 결제 등록", notes = "멤버쉽 결제 등록")
    public UserAuth add(@RequestBody UserAuth userAuth, HttpServletRequest request) throws Exception {

        return membershipService.add(userAuth, request);
    }

    @PutMapping(value = "/{id}")
    @ApiOperation(value = "수정", notes = "수정")
    public Auth modify(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        @RequestBody UserAuth params,
        HttpServletRequest request) throws Exception {

        params.setId(id);
        return membershipService.edit(params, request);
    }

    @DeleteMapping(value = "")
    @ApiOperation(value = "삭제", notes = "삭제")
    public void remove(
        @PathVariable("id") @ApiParam(value = "일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        authService.remove(id, request);
    }
    */

    @GetMapping(value = "/find/{id}")
    @ApiOperation(value = "멤버쉽 결제 단독 조회", notes = "멤버쉽 결제 단독 조회")
    public Map<String, Object> find(@PathVariable("id") @ApiParam(value = "멤버쉽 결제 PK", required = true) long id,
            HttpServletRequest request) throws Exception {

        return membershipService.findMembership(id, request);
    }

    @GetMapping(value = "/list")
    @ApiOperation(value = "멤버쉽 결제 목록 조회", notes = "멤버쉽 결제 목록 조회")
    public ResponseEntity findAll(
            @RequestParam(value = "userName", required = false) @Parameter(description = "멤버쉽 결제자 이름") String userName,
            @RequestParam(value = "orderType", required = false) @Parameter(description = "멤버쉽 유형") String orderType,
            @RequestParam(value = "status", required = false) @Parameter(description = "멤버십 상태") String status,
            @RequestParam(value = "createdAtStart", required = false) LocalDateTime createdAtStart,
            @RequestParam(value = "createdAtEnd", required = false) LocalDateTime createdAtEnd,
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            HttpServletRequest request) throws Exception {

        Long count =
            membershipService.countMembershipList(userName, orderType, status, createdAtStart, createdAtEnd);
        List<Map<String, Object>> list =
            membershipService.findMembershipList(userName, orderType, status, createdAtStart, createdAtEnd, pageNo, pageSize, request);
        Pages pages = new Pages(pageNo, pageSize);

        ListResponse membershipList = new ListResponse(count, list, pages);
        return ResponseUtil.sendResponse(membershipList);
    }

}
