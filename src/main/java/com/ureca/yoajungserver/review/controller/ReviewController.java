package com.ureca.yoajungserver.review.controller;

import com.ureca.yoajungserver.common.ApiResponse;
import com.ureca.yoajungserver.review.dto.request.ReviewCreateRequest;
import com.ureca.yoajungserver.review.dto.request.ReviewUpdateRequest;
import com.ureca.yoajungserver.review.dto.response.*;
import com.ureca.yoajungserver.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ureca.yoajungserver.common.BaseCode.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;

    // 리뷰 조회
    @GetMapping("/{planId}")
    public ResponseEntity<ApiResponse<ReviewPageResponse>> listReview(@PathVariable Long planId, Pageable pageable) {

        ReviewPageResponse response = reviewService.reviewList(planId, pageable);

        return ResponseEntity.status(STATUS_OK.getStatus())
                .body(ApiResponse.of(STATUS_OK, response));
    }

    // 리뷰 등록
    @PostMapping("/{planId}")
    public ResponseEntity<ApiResponse<ReviewCreateResponse>> insertReview(@PathVariable Long planId,
                                                                          @RequestBody ReviewCreateRequest request) {
        ReviewCreateResponse response = reviewService.insertReview(planId, request);

        return ResponseEntity.status(REVIEW_CREATE_SUCCESS.getStatus())
                .body(ApiResponse.of(REVIEW_CREATE_SUCCESS, response));
    }

    // 리뷰 수정
    @PutMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewUpdateResponse>> updateReview(@PathVariable Long reviewId,
                                                                          @RequestBody ReviewUpdateRequest request) {
        ReviewUpdateResponse response = reviewService.updateReview(reviewId, request);

        return ResponseEntity.status(REVIEW_UPDATE_SUCCESS.getStatus())
                .body(ApiResponse.of(REVIEW_UPDATE_SUCCESS, response));
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewDeleteResponse>> deleteReview(@PathVariable Long reviewId) {
        ReviewDeleteResponse response = reviewService.deleteReview(reviewId);

        return ResponseEntity.status(REVIEW_DELETE_SUCCESS.getStatus())
                .body(ApiResponse.of(REVIEW_DELETE_SUCCESS, response));
    }

    // 리뷰 좋아요 기능
    @PostMapping("/like/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewLikeResponse>> toggleReviewLike(@PathVariable Long reviewId) {
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
