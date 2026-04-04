package com.coffeeshopsystem.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorEnum {

    // validation
    INVALID_ARGUMENT(HttpStatus.BAD_REQUEST, "요청값이 올바르지 않습니다"),

    // menu
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 메뉴가 존재하지 않습니다"),

    // userPoint
    INSUFFICIENT_BALANCE(HttpStatus.BAD_REQUEST, "결제를 위한 포인트가 부족합니다"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저 정보가 존재하지 않습니다");

    private final HttpStatus httpStatus;
    private final String errorMessage;
}
