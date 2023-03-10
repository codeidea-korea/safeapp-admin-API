package com.safeapp.admin.web.controller;

import com.safeapp.admin.utils.ResponseUtil;
import com.safeapp.admin.web.dto.request.RequestAdminsDTO;
import com.safeapp.admin.web.dto.request.RequestAdminsEditDTO;
import com.safeapp.admin.web.dto.response.ResponseAdminsDTO;
import com.safeapp.admin.web.model.cmmn.ListResponse;
import com.safeapp.admin.web.model.cmmn.Pages;
import com.safeapp.admin.web.model.entity.Admins;
import com.safeapp.admin.web.service.AdminsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
@Api(tags = {"Admin"}, description = "Admin 관리")
@Slf4j
public class AdminsController {

    private final AdminsService adminsService;

    @GetMapping(value = "/chkAdminId/{adminId}")
    @ApiOperation(value = "관리자 등록 → 아이디 중복여부 확인", notes = "관리자 등록 → 아이디 중복여부 확인")
    public ResponseEntity chkAdminId(@PathVariable("adminId") @ApiParam("아이디") String adminId) {

        return ResponseUtil.sendResponse(adminsService.chkAdminId(adminId));
    }

    @GetMapping(value = "/chkEmail/{email}")
    @ApiOperation(value = "관리자 등록 → 이메일 중복여부 확인", notes = "관리자 등록 → 이메일 중복여부 확인")
    public ResponseEntity chkEmail(@PathVariable("email") @ApiParam("email") String email) {

        return ResponseUtil.sendResponse(adminsService.chkEmail(email));
    }

    @GetMapping(value = "/reqNum")
    @ApiOperation(value = "관리자 등록 → 핸드폰 본인인증 번호 요청", notes = "관리자 등록 → 핸드폰 본인인증 번호 요청")
    public ResponseEntity sendAuthSMSCode(@RequestParam(value = "phoneNo") String phoneNo) throws Exception {

        return ResponseUtil.sendResponse(adminsService.sendAuthSMSCode(phoneNo));
    }

    @PostMapping(value = "/resNum")
    @ApiOperation(value = "관리자 등록 → 핸드폰 본인인증 번호 확인", notes = "관리자 등록 → 핸드폰 본인인증 번호 확인")
    public ResponseEntity isCorrectSMSCode(@RequestParam(value = "phoneNo") String phoneNo,
            @RequestParam(value = "authNo") String authNo) throws Exception {

        return ResponseUtil.sendResponse(adminsService.isCorrectSMSCode(phoneNo, authNo));
    }

    @PostMapping(value = "/add")
    @ApiOperation(value = "관리자 등록", notes = "관리자 등록")
    public ResponseEntity<ResponseAdminsDTO> add(@RequestBody RequestAdminsDTO addDto, HttpServletRequest request) throws Exception {
        Admins addedAdmins = adminsService.add(adminsService.toAddEntity(addDto), request);
        return new ResponseEntity<>(ResponseAdminsDTO.builder().admin(addedAdmins).build(), OK);
    }

    @GetMapping(value = "/find/{id}")
    @ApiOperation(value = "관리자 단독 조회", notes = "관리자 단독 조회")
    public ResponseEntity<ResponseAdminsDTO> find(@PathVariable("id") @ApiParam(value = "관리자 PK", required = true) long id,
            HttpServletRequest request) throws Exception {

        Admins admin = adminsService.find(id, request);
        return new ResponseEntity<>(ResponseAdminsDTO.builder().admin(admin).build(), OK);
    }

    @PatchMapping(value = "/editPass")
    @ApiOperation(value = "관리자 비밀번호 수정", notes = "관리자 비밀번호 수정")
    public ResponseEntity editPassword(
            @RequestParam(value = "email", defaultValue = "admin1@codeidea.dev") String email,
            @RequestParam(value = "newPass1", defaultValue = "admin2_") String newPass1,
            @RequestParam(value = "newPass2", defaultValue = "admin2_") String newPass2,
            HttpServletRequest request) throws Exception {

        adminsService.editPassword(email, newPass1, newPass2, request);
        return ResponseUtil.sendResponse(null);
    }

    @PutMapping(value = "/edit/{id}")
    @ApiOperation(value = "관리자 수정", notes = "관리자 수정")
    public ResponseEntity<ResponseAdminsDTO> edit(@PathVariable("id") @ApiParam(value = "관리자 PK", required = true) long id,
            @RequestBody RequestAdminsEditDTO editDto, HttpServletRequest request) throws Exception {

        Admins newAdmin = adminsService.toEditEntity(editDto);
        newAdmin.setId(id);

        Admins editedAdmin = adminsService.edit(newAdmin, request);
        return new ResponseEntity<>(ResponseAdminsDTO.builder().admin(editedAdmin).build(), OK);
    }

    @DeleteMapping(value = "/remove/{id}")
    @ApiOperation(value = "관리자 삭제", notes = "관리자 삭제")
    public ResponseEntity remove(@PathVariable("id") @ApiParam("관리자 PK") long id,
            HttpServletRequest request) throws Exception {

        adminsService.remove(id, request);
        return ResponseUtil.sendResponse(null);
    }

    @GetMapping(value = "/list")
    @ApiOperation(value = "관리자 목록 조회", notes = "관리자 목록 조회")
    public ResponseEntity<ListResponse<Admins>> findAll(
            @RequestParam(value = "adminId", required = false, defaultValue = "") String adminId,
            @RequestParam(value = "adminName", required = false, defaultValue = "") String adminName,
            @RequestParam(value = "email", required = false, defaultValue = "") String email,
            @RequestParam(value = "phoneNo", required = false, defaultValue = "") String phoneNo,
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            HttpServletRequest request) throws Exception {

        Pages pages = new Pages(pageNo, pageSize);
        return
            ResponseUtil.sendResponse(
                adminsService.findAll(
                    Admins.builder()
                    .adminId(adminId)
                    .adminName(adminName)
                    .email(email)
                    .phoneNo(phoneNo)
                    .build(),
                pages, request)
            );
    }

}
