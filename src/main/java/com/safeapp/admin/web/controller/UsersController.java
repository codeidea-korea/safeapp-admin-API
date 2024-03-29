package com.safeapp.admin.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.dto.request.RequestUsersDTO;
import com.safeapp.admin.utils.ResponseUtil;
import com.safeapp.admin.web.dto.request.RequestUsersEditDTO;
import com.safeapp.admin.web.dto.response.ResponseCheckListProjectDTO;
import com.safeapp.admin.web.dto.response.ResponseCheckListProjectSelectDTO;
import com.safeapp.admin.web.dto.response.ResponseUsersDTO;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.docs.LoginHistory;
import com.safeapp.admin.web.model.entity.CheckListProject;
import com.safeapp.admin.web.model.entity.UserAuth;
import com.safeapp.admin.web.model.entity.Users;
import com.safeapp.admin.web.repos.jpa.UserAuthRepos;
import com.safeapp.admin.web.repos.mongo.LoginHistoryRepos;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.safeapp.admin.web.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.client.HttpServerErrorException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
@Api(tags = {"User"}, description = "회원 관리")
@Slf4j
public class UsersController {

    private final UserService userService;

    private final LoginHistoryRepos loginHistoryRepos;
    private final UserAuthRepos userAuthRepos;

    @GetMapping(value = "/chk/{userId}")
    @ApiOperation(value = "회원 등록 → 아이디 중복여부 확인", notes = "회원 등록 → 아이디 중복여부 확인")
    public ResponseEntity chkUserId(@PathVariable("userId") @ApiParam("아이디") String userId) {

        return ResponseUtil.sendResponse(userService.chkUserId(userId));
    }

    @GetMapping(value = "/reqNum")
    @ApiOperation(value = "회원 등록 → 핸드폰 본인인증 번호 요청", notes = "회원 등록 → 핸드폰 본인인증 번호 요청")
    public ResponseEntity sendAuthSMSCode(@RequestParam(value = "phoneNo") String phoneNo) throws Exception {

        return ResponseUtil.sendResponse(userService.sendAuthSMSCode(phoneNo));
    }

    @PostMapping(value = "/resNum")
    @ApiOperation(value = "회원 등록 → 핸드폰 본인인증 번호 확인", notes = "회원 등록 → 핸드폰 본인인증 번호 확인")
    public ResponseEntity isCorrectSMSCode(@RequestParam(value = "phoneNo") String phoneNo,
            @RequestParam(value = "authNo") String authNo) throws Exception {

        return ResponseUtil.sendResponse(userService.isCorrectSMSCode(phoneNo, authNo));
    }

    @PostMapping(value = "/add")
    @ApiOperation(value = "회원 등록", notes = "회원 등록")
    public ResponseEntity<ResponseUsersDTO> add(@RequestBody RequestUsersDTO addDto, HttpServletRequest request) throws Exception {
        Users addedUser = userService.add(userService.toAddEntity(addDto), request);
        return new ResponseEntity<>(ResponseUsersDTO.builder().user(addedUser).build(), OK);
    }

    @GetMapping(value = "/find/{id}")
    @ApiOperation(value = "회원 단독 조회", notes = "회원 단독 조회")
    public ResponseEntity<HashMap<String, Object>> find(@PathVariable("id") @ApiParam(value = "회원 PK", required = true) long id,
            HttpServletRequest request) throws Exception {

        HashMap<String, Object> resultMap = new HashMap<>();

        Users user = userService.find(id, request);
        resultMap.put("oldUser", user);

        LoginHistory loginHistory =
            loginHistoryRepos.findTopByUserIdAndIsSuccessOrderByCreateDtDesc(user.getUserId(), true);
        if(Objects.nonNull(loginHistory)) {
            resultMap.put("loginHistory", loginHistory);
        } else {
            resultMap.put("loginHistory", null);
        }

        UserAuth userAuth =
            userAuthRepos.findTopByUserAndStatusOrderByIdDesc(user.getId(), "ing");
        if(Objects.nonNull(userAuth)) {
            resultMap.put("userAuth", userAuth);
        } else {
            resultMap.put("userAuth", null);
        }

        return ResponseUtil.sendResponse(resultMap);
    }

    @GetMapping(value = "/find/{id}/project")
    @ApiOperation(value = "회원 단독 프로젝트 조회", notes = "회원 단독 프로젝트 조회")
    public ResponseEntity<HashMap<String, Object>> findProject(
            @PathVariable("id") @ApiParam(value = "회원 PK", required = true) long id,
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            HttpServletRequest request) {

        HashMap<String, Object> resultMap = new HashMap<>();

        Map<String, Object> myAuth = userService.findMyAuth(id, request);
        resultMap.put("myAuth", myAuth);

        long count = userService.countMyProjectList(id, request);
        List<Map<String, Object>> list = userService.findMyProjectList(id, pageNo, pageSize, request);
        Pages pages = new Pages(pageNo, pageSize);

        ListResponse myProjectList = new ListResponse(count, list, pages);
        resultMap.put("myProjectList", myProjectList);

        return ResponseUtil.sendResponse(resultMap);
    }

    @PatchMapping(value = "/editPass")
    @ApiOperation(value = "회원 비밀번호 수정", notes = "회원 비밀번호 수정")
    public ResponseEntity editPassword(
            @RequestParam(value = "userId", defaultValue = "user1") String userId,
            @RequestParam(value = "newPass1", defaultValue = "user2_") String newPass1,
            @RequestParam(value = "newPass2", defaultValue = "user2_") String newPass2,
            HttpServletRequest request) throws Exception {

        userService.editPassword(userId, newPass1, newPass2, request);
        return ResponseUtil.sendResponse(null);
    }

    @PutMapping(value = "/edit/{id}")
    @ApiOperation(value = "회원 수정", notes = "회원 수정")
    public ResponseEntity<ResponseUsersDTO> edit(@PathVariable("id") @ApiParam(value = "회원 PK", required = true) long id,
            @RequestBody RequestUsersEditDTO editDto, HttpServletRequest request) throws Exception {

        Users newUser = userService.toEditEntity(editDto);
        newUser.setId(id);

        Users editedUser = userService.edit(newUser, request);
        return new ResponseEntity<>(ResponseUsersDTO.builder().user(editedUser).build(), OK);
    }

    @DeleteMapping(value = "/remove/{id}")
    @ApiOperation(value = "회원 삭제", notes = "회원 삭제")
    public ResponseEntity remove(@PathVariable("id") @ApiParam("회원 PK") long id,
            HttpServletRequest request) throws Exception {

        userService.remove(id, request);
        return ResponseUtil.sendResponse(null);
    }

    @GetMapping(value = "/list")
    @ApiOperation(value = "회원 목록 조회", notes = "회원 목록 조회")
    public ResponseEntity<ListResponse<Users>> findAll(
            @RequestParam(value = "userId", required = false, defaultValue = "") String userId,
            @RequestParam(value = "userName", required = false, defaultValue = "") String userName,
            @RequestParam(value = "phoneNo", required = false, defaultValue = "") String phoneNo,
            @RequestParam(value = "email", required = false, defaultValue = "") String email,
            @RequestParam(value = "pageNo", defaultValue = "1") @Parameter(description = "현재 페이지 번호") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") @Parameter(description = "1 페이지 당 목록 수") int pageSize,
            HttpServletRequest request) throws Exception {

        Pages pages = new Pages(pageNo, pageSize);
        return
            ResponseUtil.sendResponse(
                userService.findAll(
                    Users.builder()
                    .userId(userId)
                    .userName(userName)
                    .phoneNo(phoneNo)
                    .email(email)
                    .build(),
                pages, request)
            );
    }

}