package com.ureca.yoajungserver.review.advice;

import com.ureca.yoajungserver.common.ApiResponse;
import com.ureca.yoajungserver.review.controller.ReviewController;
import com.ureca.yoajungserver.review.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

public class ReviewControllerAdvice {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(e.getBaseCode().getStatus())
                .body(ApiResponse.ok(e.getBaseCode()));
    }

    @ExceptionHandler(PlanNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handlePlanNotFoundException(PlanNotFoundException e) {
        return ResponseEntity.status(e.getBaseCode().getStatus())
                .body(ApiResponse.ok(e.getBaseCode()));
    }

    @ExceptionHandler(ReviewAlreadyExistException.class)
    public ResponseEntity<ApiResponse<?>> handleReviewAlreadyExistException(ReviewAlreadyExistException e) {
        return ResponseEntity.status(e.getBaseCode().getStatus())
                .body(ApiResponse.ok(e.getBaseCode()));
    }

    @ExceptionHandler(ReviewNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleReviewNotFoundException(ReviewNotFoundException e) {
        return ResponseEntity.status(e.getBaseCode().getStatus())
                .body(ApiResponse.ok(e.getBaseCode()));
    }

    @ExceptionHandler(NotReviewAuthorException.class)
    public ResponseEntity<ApiResponse<?>> handleNotReviewAuthorException(NotReviewAuthorException e) {
        return ResponseEntity.status(e.getBaseCode().getStatus())
                .body(ApiResponse.ok(e.getBaseCode()));
    }

    @ExceptionHandler(ReviewNotAllowedException.class)
    public ResponseEntity<ApiResponse<?>> handleReviewNotAllowedException(ReviewNotAllowedException e) {
        return ResponseEntity.status(e.getBaseCode().getStatus())
                .body(ApiResponse.ok(e.getBaseCode()));
    }

    @ExceptionHandler(DuplicatedReviewLikeException.class)
    public ResponseEntity<ApiResponse<?>> handleDuplicatedReviewLikeException(DuplicatedReviewLikeException e) {
        return ResponseEntity.status(e.getBaseCode().getStatus())
                .body(ApiResponse.ok(e.getBaseCode()));
    }
}
