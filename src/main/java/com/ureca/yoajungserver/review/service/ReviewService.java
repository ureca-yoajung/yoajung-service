package com.ureca.yoajungserver.review.service;

import com.ureca.yoajungserver.review.dto.request.ReviewCreateRequest;
import com.ureca.yoajungserver.review.dto.request.ReviewUpdateRequest;
import com.ureca.yoajungserver.review.dto.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {
    Page<ReviewListResponse> reviewList(Long planId, Pageable pageable);
    ReviewCreateResponse insertReview(Long planId, ReviewCreateRequest request);
    ReviewUpdateResponse updateReview(Long reviewId, ReviewUpdateRequest request);
    ReviewDeleteResponse deleteReview(Long reviewId);

    ReviewLikeResponse reviewLike(Long reviewId);
}
