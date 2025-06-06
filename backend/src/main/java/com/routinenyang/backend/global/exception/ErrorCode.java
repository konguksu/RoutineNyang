package com.routinenyang.backend.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 400 Bad Request
    INVALID_REQUEST(BAD_REQUEST, "잘못된 요청입니다."),
    USER_ALREADY_EXISTS(BAD_REQUEST, "이미 등록된 사용자입니다."),
    ROUTINE_ALREADY_DELETED(BAD_REQUEST, "이미 삭제된 루틴은 다시 삭제할 수 없습니다."),
    ITEM_NOT_PURCHASED(BAD_REQUEST, "유저가 보유하지 않은 아이템입니다."),
    COIN_NOT_ENOUGH(BAD_REQUEST, "보유한 코인이 부족합니다."),

    // 401 Unauthorized,
    UNAUTHORIZED_REQUEST(UNAUTHORIZED, "인증이 필요한 요청입니다."),

    // 403 Forbidden
    ACCESS_DENIED(FORBIDDEN, "접근 권한이 없습니다."),
    ROUTINE_GROUP_IMMUTABLE(FORBIDDEN, "기본 루틴 그룹은 수정하거나 삭제할 수 없습니다."),

    // 404 Not Found
    USER_NOT_FOUND(NOT_FOUND, "사용자를 찾을 수 없습니다."),
    SURVEY_NOT_FOUND(NOT_FOUND, "설문 데이터를 찾을 수 없습니다."),
    ROUTINE_GROUP_NOT_FOUND(NOT_FOUND, "루틴 그룹을 찾을 수 없습니다."),
    ROUTINE_NOT_FOUND(NOT_FOUND, "루틴을 찾을 수 없습니다. 존재하지 않거나 삭제된 루틴입니다."),
    ITEM_NOT_FOUND(NOT_FOUND, "아이템을 찾을 수 없습니다."),

    // 500 Internal Server Error
    INTERNAL_ERROR(INTERNAL_SERVER_ERROR, "알 수 없는 서버 내부 에러가 발생했습니다.");

    private final HttpStatus status;
    private final String message;
}
