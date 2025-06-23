package com.ureca.yoajungserver.swagger.api;

import com.ureca.yoajungserver.common.ApiResponse;
import com.ureca.yoajungserver.review.dto.request.ReviewCreateRequest;
import com.ureca.yoajungserver.review.dto.request.ReviewUpdateRequest;
import com.ureca.yoajungserver.review.dto.response.*;
import com.ureca.yoajungserver.swagger.error.ErrorCode400;
import com.ureca.yoajungserver.swagger.error.ErrorCode404;
import com.ureca.yoajungserver.swagger.response.ApiCreatedResponse;
import com.ureca.yoajungserver.swagger.response.ApiSuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "리뷰 API", description = "리뷰 CRUD, 좋아요 토글")
@RequestMapping("/review")
public interface ReviewControllerSwagger {

    @Operation(
            summary = "리뷰 목록 조회",
            description = "요금제 리뷰 페이징"
    )
    @ApiSuccessResponse(description = "리뷰 목록 조회 성공")
    @GetMapping("/{planId}")
    ResponseEntity<ApiResponse<ReviewPageResponse>> listReview(
            @PathVariable Long planId,
            @Parameter(hidden = true) Pageable pageable
    );

    @Operation(
            summary = "리뷰 등록",
            description = "요금제에 새 리뷰를 등록"
    )
    @ApiCreatedResponse(description = "리뷰 등록 성공")
    @ErrorCode400(description = "잘못된 요청 또는 입력값 오류")
    @PostMapping("/{planId}")
    ResponseEntity<ApiResponse<ReviewCreateResponse>> insertReview(
            @PathVariable Long planId,
            @Valid @RequestBody ReviewCreateRequest request
    );

    @Operation(
            summary = "리뷰 수정",
            description = "주어진 리뷰 수정"
    )
    @ApiSuccessResponse(description = "리뷰 수정 성공")
    @ErrorCode400(description = "잘못된 요청 또는 입력값 오류")
    @ErrorCode404(description = "리뷰를 찾을 수 없습니다")
    @PutMapping("/{reviewId}")
    ResponseEntity<ApiResponse<ReviewUpdateResponse>> updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewUpdateRequest request
    );

    @Operation(
            summary = "리뷰 삭제",
            description = "주어진 리뷰를 삭제"
    )
    @ApiSuccessResponse(description = "리뷰 삭제 성공")
    @ErrorCode400(description = "잘못된 요청")
    @ErrorCode404(description = "리뷰를 찾을 수 없습니다")
    @DeleteMapping("/{reviewId}")
    ResponseEntity<ApiResponse<ReviewDeleteResponse>> deleteReview(
            @PathVariable Long reviewId
    );

    @Operation(
            summary = "리뷰 좋아요",
            description = "리뷰 좋아요를 등록, 취소"
    )
    @ApiSuccessResponse(description = "리뷰 좋아요 토글 성공")
    @ErrorCode400(description = "잘못된 요청")
    @ErrorCode404(description = "리뷰를 찾을 수 없습니다")
    @PostMapping("/like/{reviewId}")
    ResponseEntity<ApiResponse<ReviewLikeResponse>> toggleReviewLike(
            @PathVariable Long reviewId
    );
}