package com.ureca.yoajungserver.common;

import com.ureca.yoajungserver.common.exception.BusinessException;
import com.ureca.yoajungserver.user.exception.EmailSendFailedException;
import jakarta.transaction.SystemException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
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
    public ResponseEntity<ApiResponse<String>> handleValidationException(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.of(BaseCode.INVALID_REQUEST, message));
    }// 검증 예외

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentials(BadCredentialsException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.ok(BaseCode.USER_LOGIN_FAIL));
    }// 로그인 실패

    @ExceptionHandler(EmailSendFailedException.class)
    public ResponseEntity<ApiResponse<Void>> handleEmailSendFailed(EmailSendFailedException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.ok(BaseCode.EMAIL_SEND_FAILED));
    }

    @ExceptionHandler(RedisConnectionFailureException.class)
    public ResponseEntity<ApiResponse<Void>> handlerRedisFaulrue(RedisConnectionFailureException exception) {

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ApiResponse.ok(BaseCode.REDIS_UNAVAILABLE));
    }// 레디스연결실패

    @ExceptionHandler(DataAccessResourceFailureException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataAccessFaileure(DataAccessResourceFailureException exception) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ApiResponse.ok(BaseCode.REDIS_UNAVAILABLE));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception exception) {

        return ResponseEntity
                .internalServerError()
                .body(ApiResponse.ok(BaseCode.INTERNAL_SERVER_ERROR));
    }// 최종예외
}
