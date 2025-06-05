package com.ureca.yoajungserver.review.controller;

import com.ureca.yoajungserver.common.ApiResponse;
import com.ureca.yoajungserver.review.dto.*;
import com.ureca.yoajungserver.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ureca.yoajungserver.common.BaseCode.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;

    // 리뷰 등록
    @PostMapping
    public ResponseEntity<ApiResponse<?>> insertReview(@RequestBody ReviewCreateRequest request) {
        ReviewCreateResponse response = reviewService.insertReview(request);

        return ResponseEntity.status(REVIEW_CREATE_SUCCESS.getStatus())
                .body(ApiResponse.of(REVIEW_CREATE_SUCCESS, response));
    }

    // 리뷰 수정
    @PutMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<?>> updateReview(@PathVariable Long reviewId, @RequestBody ReviewUpdateRequest request) {
        ReviewUpdateResponse response = reviewService.updateReview(reviewId, request);

        return ResponseEntity.status(REVIEW_UPDATE_SUCCESS.getStatus())
                .body(ApiResponse.of(REVIEW_UPDATE_SUCCESS, response));
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<?>> deleteReview(@PathVariable Long reviewId) {
        ReviewDeleteResponse response = reviewService.deleteReview(reviewId);

        return ResponseEntity.status(REVIEW_DELETE_SUCCESS.getStatus())
                .body(ApiResponse.of(REVIEW_DELETE_SUCCESS, response));
    }

    // 리뷰 좋아요 기능
    @PostMapping("/like/{reviewId}")
    public ResponseEntity<ApiResponse<?>> insertReview(@PathVariable Long reviewId) {
        ReviewLikeResponse response = reviewService.reviewLike(reviewId);

        // 좋아요 취소
        if(response.isDeleted()){
            return ResponseEntity.status(REVIEW_LIKE_CANCELED.getStatus())
                .body(ApiResponse.ok(REVIEW_LIKE_CANCELED));
        }

        // 좋아요 등록
        return ResponseEntity.status(REVIEW_LIKE_SUCCESS.getStatus())
                .body(ApiResponse.ok(REVIEW_LIKE_SUCCESS));
    }
}
