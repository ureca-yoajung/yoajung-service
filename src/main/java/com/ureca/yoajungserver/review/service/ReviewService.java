package com.ureca.yoajungserver.review.service;

import com.ureca.yoajungserver.review.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {
    Page<ReviewListResponse> reviewList(Long planId, Pageable pageable);
    ReviewCreateResponse insertReview(ReviewCreateRequest request);
    ReviewUpdateResponse updateReview(Long reviewId, ReviewUpdateRequest request);
    ReviewDeleteResponse deleteReview(Long reviewId);

    ReviewLikeResponse reviewLike(Long reviewId);
}
