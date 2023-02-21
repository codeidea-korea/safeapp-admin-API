package com.safeapp.admin.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.utils.ResponseUtil;
import com.safeapp.admin.web.dto.request.RequestMembershipEditDTO;
import com.safeapp.admin.web.dto.response.ResponseMembershipDTO;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.UserAuth;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safeapp.admin.web.service.MembershipService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/membership")
@AllArgsConstructor
@Api(tags = {"Membership"}, description = "멤버쉽 결제 관리")
public class MembershipController {

    private final MembershipService membershipService;

    /*
    @PostMapping(value = "/add")
    @ApiOperation(value = "멤버쉽 결제 등록", notes = "멤버쉽 결제 등록")
    public UserAuth add(@RequestBody UserAuth newUserAuth, HttpServletRequest request) throws Exception {

        return membershipService.add(newUserAuth, request);
    }

    @DeleteMapping(value = "/remove/{id}")
    @ApiOperation(value = "멤버쉽 결제 삭제", notes = "멤버쉽 결제 삭제")
    public void remove(@PathVariable("id") @ApiParam(value = "멤버쉽 결제 PK", required = true) long id,
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

    @PutMapping(value = "/edit/{id}")
    @ApiOperation(value = "멤버쉽 결제 수정", notes = "멤버쉽 결제 수정")
    public ResponseEntity<ResponseMembershipDTO> edit(@PathVariable("id") @ApiParam(value = "멤버쉽 결제 PK", required = true) long id,
            @RequestBody RequestMembershipEditDTO editDto, HttpServletRequest request) throws Exception {

        UserAuth newUserAuth = membershipService.toEditEntity(editDto);
        newUserAuth.setId(id);

        UserAuth editedUserAuth = membershipService.edit(newUserAuth, request);
        return new ResponseEntity<>(ResponseMembershipDTO.builder().userAuth(editedUserAuth).build(), OK);
    }

    @PutMapping(value = "/unsubscribe/{id}")
    @ApiOperation(value = "멤버쉽 결제 해지(구독해지)", notes = "멤버쉽 결제 해지(구독해지)")
    public ResponseEntity unsubscribe(@PathVariable("id") @ApiParam(value = "멤버쉽 결제 PK", required = true) long id,
            HttpServletRequest request) {

        membershipService.unsubscribe(id, request);
        return ResponseUtil.sendResponse(null);
    }

    @GetMapping(value = "/list")
    @ApiOperation(value = "멤버쉽 결제 목록 조회", notes = "멤버쉽 결제 목록 조회")
    public ResponseEntity findAll(
            @RequestParam(value = "userName", required = false) @Parameter(description = "멤버쉽 결제자 이름") String userName,
            @RequestParam(value = "orderType", required = false) @Parameter(description = "멤버쉽 유형") String orderType,
            @RequestParam(value = "status", required = false) @Parameter(description = "멤버십 상태") String status,
            @RequestParam(value = "createdAtStart", required = false) String createdAtStart,
            @RequestParam(value = "createdAtEnd", required = false) String createdAtEnd,
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