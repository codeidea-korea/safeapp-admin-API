package com.safeapp.admin.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.web.model.entity.cmmn.BfFile;
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
@Api(tags = {"File"}, description = "파일 업로드", basePath = "/file")
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(value = "", consumes = "multipart/form-data")
    @ApiOperation(value = "파일 업로드", notes = "파일 업로드")
    public BfFile uploadAllowedFile(
        @RequestParam(required = true, value = "file") MultipartFile file,
        HttpServletRequest request) throws Exception {
        return fileService.uploadAllowedFile(file, request);
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "파일 찾기 (단건)", notes = "파일 찾기 (단건)")
    public BfFile find(
        @PathVariable("id") @ApiParam(value = "파일 일련번호", required = true) long id,
        HttpServletRequest request) throws Exception {
        return fileService.findById(id, request);
    }
}
