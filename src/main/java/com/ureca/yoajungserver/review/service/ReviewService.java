package com.ureca.yoajungserver.review.service;

import com.ureca.yoajungserver.review.dto.*;

public interface ReviewService {
    ReviewCreateResponse insertReview(ReviewCreateRequest request);
    ReviewUpdateResponse updateReview(Long reviewId, ReviewUpdateRequest request);
    ReviewDeleteResponse deleteReview(Long reviewId);
}
