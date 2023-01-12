package com.binoofactory.cornsqure.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.binoofactory.cornsqure.utils.ResponseUtil;
import com.binoofactory.cornsqure.web.model.entity.ImportPayment;
import com.binoofactory.cornsqure.web.service.cmmn.ImportService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@Api(tags = {"Order"}, description = "주문", basePath = "/orders")
public class OrderController {

    private final ImportService importService;

    @Autowired
    public OrderController(ImportService importService) {
        this.importService = importService;
    }
    
    @PostMapping(value = "/generate-code")
    @ResponseBody
    @ApiOperation(value = "주문번호 채번", notes = "주문번호 채번")
    public ResponseEntity generateOrderNumber(@RequestBody ImportPayment payment, HttpServletRequest request) throws Exception {
        return ResponseUtil.sendResponse(importService.generateOrderNumber(payment, request));
    }

    @PostMapping(value = "/payment/complete")
    @ApiOperation(value = "결제 승인 처리", notes = "결제 승인 처리")
    public String callBackImportPayment(
        @RequestParam(value = "imp_uid", required = true) String imp_uid,
        @RequestParam(value = "merchant_uid", required = true) String merchant_uid,
        HttpServletRequest request) throws Exception {

        String redirectUri = importService.callBackImportPayment(imp_uid, merchant_uid);
        return "redirect:" + redirectUri;
    }
}
