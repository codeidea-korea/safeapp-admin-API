package com.safeapp.admin.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.dto.request.RequestUsersDTO;
import com.safeapp.admin.utils.ResponseUtil;
import com.safeapp.admin.web.dto.request.RequestUsersModifyDTO;
import com.safeapp.admin.web.dto.response.ResponseCheckListProjectDTO;
import com.safeapp.admin.web.dto.response.ResponseCheckListProjectSelectDTO;
import com.safeapp.admin.web.dto.response.ResponseUsersDTO;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.CheckListProject;
import com.safeapp.admin.web.model.entity.Users;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.safeapp.admin.web.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
@Api(tags = {"User"}, description = "회원")
public class UsersController {

    private final UserService userService;

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
        Users addedUser = userService.add(userService.toEntity(addDto), request);
        return new ResponseEntity<>(ResponseUsersDTO.builder().user(addedUser).build(), OK);
    }

    @GetMapping(value = "/find/{id}")
    @ApiOperation(value = "회원 단독 조회", notes = "회원 단독 조회")
    public ResponseEntity<ResponseUsersDTO> find(@PathVariable("id") @ApiParam(value = "회원 PK", required = true) long id,
            HttpServletRequest request) throws Exception {

        Users oldUser = userService.find(id, request);
        return new ResponseEntity<>(ResponseUsersDTO.builder().user(oldUser).build(), OK);
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
            @RequestBody RequestUsersModifyDTO modifyDto, HttpServletRequest request) throws Exception {

        Users user = userService.toEntityModify(modifyDto);
        user.setId(id);

        Users editedUser = userService.edit(user, request);
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
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            HttpServletRequest request) throws Exception {

        Pages pages = new Pages(pageNo, pageSize);
        return
            ResponseUtil.sendResponse(
                userService.findAll(
                    Users.builder()
                    .userId("%" + userId + "%")
                    .userName("%" + userName + "%")
                    .phoneNo("%" + phoneNo + "%")
                    .build(),
                pages, request)
            );
    }

}