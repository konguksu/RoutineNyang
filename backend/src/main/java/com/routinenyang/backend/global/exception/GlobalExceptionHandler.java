package com.routinenyang.backend.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.routinenyang.backend.global.response.ApiResponse;
import com.routinenyang.backend.global.response.ResponseFactory;

import static com.routinenyang.backend.global.exception.ErrorCode.INTERNAL_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException e) {
        return ResponseFactory.fail(e.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("Unhandled server error", e); // 로그
        return ResponseFactory.fail(INTERNAL_ERROR);
    }
}
