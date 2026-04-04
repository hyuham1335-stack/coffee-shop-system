package com.coffeeshopsystem.common.dto;

import com.coffeeshopsystem.common.exception.ErrorEnum;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@JsonPropertyOrder({"success", "data", "code", "error", "timestamp"})
public class ApiResponse<T> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    private final boolean success;
    private final T data;
    private final int code;
    private final String error;
    private final String timestamp;

    public static <T> ApiResponse<T> success(HttpStatus status, T data) {
        return new ApiResponse<>(true, data, status.value(), null, now());
    }

    public static ApiResponse<Void> fail(ErrorEnum errorEnum) {
        return new ApiResponse<>(false, null, errorEnum.getHttpStatus().value(), errorEnum.getErrorMessage(), now());
    }

    public static ApiResponse<Void> fail(ErrorEnum errorEnum, String errorMessage) {
        return new ApiResponse<>(false, null, errorEnum.getHttpStatus().value(), errorMessage, now());
    }

    private static String now() {
        return LocalDateTime.now().format(FORMATTER);
    }
}
