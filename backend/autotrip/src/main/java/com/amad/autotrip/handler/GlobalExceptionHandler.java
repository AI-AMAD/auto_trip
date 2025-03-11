package com.amad.autotrip.handler;

import com.amad.autotrip.exception.DuplicateMemberException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateMemberException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateMemberException(DuplicateMemberException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage()); // "이미 가입되어 있는 유저입니다."

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
