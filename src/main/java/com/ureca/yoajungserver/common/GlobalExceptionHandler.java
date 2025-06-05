package com.ureca.yoajungserver.common;

import com.ureca.yoajungserver.common.exception.BusinessException;
import jakarta.transaction.SystemException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException exception) {
        BaseCode code = exception.getBaseCode();
        return ResponseEntity
                .status(code.getStatus())
                .body(ApiResponse.ok(code));
    }// 비즈니스 예외 처리

    @ExceptionHandler(SystemException.class)
    public ResponseEntity<ApiResponse<Void>> handleSystemException(SystemException exception) {
        return ResponseEntity
                .internalServerError()
                .body(ApiResponse.ok(BaseCode.INTERNAL_SERVER_ERROR));
    }// 서버 예외

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.of(BaseCode.INVALID_REQUEST, message));
    }// 검증 예외


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {
        return ResponseEntity
                .internalServerError()
                .body(ApiResponse.ok(BaseCode.INTERNAL_SERVER_ERROR));
    }// 최종예외
}
