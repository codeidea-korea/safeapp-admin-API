package com.safeapp.admin.conf;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestController
@ControllerAdvice
public class ResponseHandler extends ResponseEntityExceptionHandler {

    // 2023. 01. 18 - 개발하는 동안에는 임시로 주석처리 함.
    /*
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        Map<String, Object> exceptionResponse = new HashMap<>();
        exceptionResponse.put("result", false);
        exceptionResponse.put("details", request.getDescription(false));
        exceptionResponse.put("msg", ex.getMessage());
        exceptionResponse.put("time", new Date());
        
        return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    */
    
    @ExceptionHandler(HttpServerErrorException.class)
    public final ResponseEntity<Object> handleHttpServerErrorException(HttpServerErrorException ex) {
        Map<String, Object> exceptionResponse = new HashMap<>();
        exceptionResponse.put("result", false);
        exceptionResponse.put("code", ex.getStatusCode());
        exceptionResponse.put("msg", ex.getMessage());
        
        return new ResponseEntity(exceptionResponse, ex.getStatusCode());
    }

}