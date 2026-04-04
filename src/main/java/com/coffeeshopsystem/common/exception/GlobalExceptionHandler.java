package com.coffeeshopsystem.common.exception;

import com.coffeeshopsystem.common.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ApiResponse> handleServiceException(ServiceException e) {

        HttpStatus httpStatus = e.getErrorEnum().getHttpStatus();

        return ResponseEntity.status(httpStatus).body(ApiResponse.fail(e.getErrorEnum()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e, HttpServletRequest request) {
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(ErrorEnum.INVALID_ARGUMENT, message));
    }
}
