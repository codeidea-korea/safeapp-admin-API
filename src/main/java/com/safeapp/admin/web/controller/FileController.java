package com.safeapp.admin.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.model.entity.cmmn.Files;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.safeapp.admin.web.service.cmmn.FileService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/file")
@AllArgsConstructor
@Api(tags = {"File"}, description = "파일 단독 관리")
public class FileController {

    private final FileService fileService;

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    @ApiOperation(value = "파일 단독 업로드", notes = "파일 단독 업로드")
    public Files uploadAllowedFile(@RequestParam(value = "file") MultipartFile file, HttpServletRequest request) {
        return fileService.uploadAllowedFile(file, request);
    }

    @GetMapping(value = "/find/{id}")
    @ApiOperation(value = "파일 단독 조회", notes = "파일 단독 조회")
    public Files find(@PathVariable("id") @ApiParam(value = "파일 PK", required = true) long id, HttpServletRequest request) throws Exception {
        return fileService.findById(id, request);
    }

}