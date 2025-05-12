package com.routinenyang.backend.global.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.routinenyang.backend.global.exception.ErrorCode;

public class ResponseFactory {

    public static <T> ResponseEntity<ApiResponse<T>> ok(String message, T data) {
        return ResponseEntity.ok(ApiResponse.success(data, message));
    }

    public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(data));
    }
    public static ResponseEntity<ApiResponse<Void>> fail(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.fail(errorCode));
    }

    public static <T> ResponseEntity<ApiResponse<T>> noContent() {
        return ResponseEntity.noContent().build(); // ApiResponse 없이 204 응답
    }
}
