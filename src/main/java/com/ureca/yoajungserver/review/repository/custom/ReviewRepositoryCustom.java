package com.ureca.yoajungserver.review.repository.custom;

import com.ureca.yoajungserver.review.dto.ReviewListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewRepositoryCustom {
    Page<ReviewListResponse> findReviewList(Long userId, Long planId, Pageable pageable);
}
