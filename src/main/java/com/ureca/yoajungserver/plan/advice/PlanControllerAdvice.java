package com.ureca.yoajungserver.plan.advice;

import com.ureca.yoajungserver.common.ApiResponse;
import com.ureca.yoajungserver.plan.exception.InvalidPlanCategoryException;
import com.ureca.yoajungserver.review.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PlanControllerAdvice {
    @ExceptionHandler(PlanNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handlePlanNotFoundException(PlanNotFoundException e) {
        return ResponseEntity.status(e.getBaseCode().getStatus())
                .body(ApiResponse.ok(e.getBaseCode()));
    }

    @ExceptionHandler(InvalidPlanCategoryException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidPlanCategoryException(InvalidPlanCategoryException e) {
        return ResponseEntity.status(e.getBaseCode().getStatus())
                .body(ApiResponse.ok(e.getBaseCode()));
    }
}
