package com.ureca.yoajungserver.review.advice;

import com.ureca.yoajungserver.common.ApiResponse;
import com.ureca.yoajungserver.review.controller.ReviewController;
import com.ureca.yoajungserver.review.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {ReviewController.class})
public class ReviewControllerAdvice {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(e.getBaseCode().getStatus())
                .body(ApiResponse.of(e.getBaseCode(), null));
    }

    @ExceptionHandler(PlanNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handlePlanNotFoundException(PlanNotFoundException e) {
        return ResponseEntity.status(e.getBaseCode().getStatus())
                .body(ApiResponse.of(e.getBaseCode(), null));
    }

    @ExceptionHandler(ReviewAlreadyExistException.class)
    public ResponseEntity<ApiResponse<?>> handleReviewAlreadyExistException(ReviewAlreadyExistException e) {
        return ResponseEntity.status(e.getBaseCode().getStatus())
                .body(ApiResponse.of(e.getBaseCode(), null));
    }

    @ExceptionHandler(ReviewNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleReviewNotFoundException(ReviewNotFoundException e) {
        return ResponseEntity.status(e.getBaseCode().getStatus())
                .body(ApiResponse.of(e.getBaseCode(), null));
    }

    @ExceptionHandler(NotReviewAuthorException.class)
    public ResponseEntity<ApiResponse<?>> handleNotReviewAuthorException(NotReviewAuthorException e) {
        return ResponseEntity.status(e.getBaseCode().getStatus())
                .body(ApiResponse.of(e.getBaseCode(), null));
    }

    @ExceptionHandler(ReviewNotAllowedException.class)
    public ResponseEntity<ApiResponse<?>> handleReviewNotAllowedException(ReviewNotAllowedException e) {
        return ResponseEntity.status(e.getBaseCode().getStatus())
                .body(ApiResponse.of(e.getBaseCode(), null));
    }

    @ExceptionHandler(DuplicatedReviewLikeException.class)
    public ResponseEntity<ApiResponse<?>> handleDuplicatedReviewLikeException(DuplicatedReviewLikeException e) {
        return ResponseEntity.status(e.getBaseCode().getStatus())
                .body(ApiResponse.of(e.getBaseCode(), null));
    }
}
