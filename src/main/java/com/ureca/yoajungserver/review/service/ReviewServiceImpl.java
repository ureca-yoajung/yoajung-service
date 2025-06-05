package com.ureca.yoajungserver.review.service;

import com.ureca.yoajungserver.plan.entity.Plan;
import com.ureca.yoajungserver.review.dto.*;
import com.ureca.yoajungserver.review.entity.Review;
import com.ureca.yoajungserver.review.exception.*;
import com.ureca.yoajungserver.review.repository.PlanRepository;
import com.ureca.yoajungserver.review.repository.UserRepository;
import com.ureca.yoajungserver.user.entity.User;
import com.ureca.yoajungserver.review.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ureca.yoajungserver.common.BaseCode.*;
/**
 * build.gradle 시큐리티 설정 부분 일단 주석처리해놓음.
 */
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;



    // 리뷰 등록
    @Override
    @Transactional
    public ReviewCreateResponse insertReview(ReviewCreateRequest request) {

        // 로그인한 유저 (더미데이터)
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        Plan plan = planRepository.findById(request.getPlanId())
                .orElseThrow(() -> new PlanNotFoundException(PLAN_NOT_FOUND));

        // 이용중인 요금젱에만 리뷰 작성 가능
        if(!user.getPlan().equals(plan)) {
            throw new ReviewNotAllowedException(REVIEW_NOT_ALLOWED);
        }

        Review review = Review.builder()
                .user(user)
                .plan(plan)
                .content(request.getContent())
                .star(request.getStar())
                .build();

        // 이미 리뷰를 작성했을 경우 예외처리
        if(reviewRepository.existsByUserAndPlanAndIsDeletedFalse(user, plan)) {
            throw new ReviewAlreadyExistException(REVIEW_ALREADY_EXIST);
        }

        reviewRepository.save(review);

        return ReviewCreateResponse.builder()
                .reviewId(review.getId())
                .build();
    }

    // 리뷰 수정
    @Override
    @Transactional
    public ReviewUpdateResponse updateReview(Long reviewId, ReviewUpdateRequest request) {

        // 로그인한 유저 (더미데이터)
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(()->new ReviewNotFoundException(REVIEW_NOT_FOUND));

        // 리뷰 작성자 아니면 수정불가
        if(!review.getUser().getId().equals(user.getId())) {
            throw new NotReviewAuthorException(NOT_REVIEW_AUTHOR);
        }

        review.updateReview(request.getStar(), request.getContent());

        return ReviewUpdateResponse.builder()
                .reviewId(review.getId())
                .build();
    }

    // 리뷰 삭제
    @Override
    @Transactional
    public ReviewDeleteResponse deleteReview(Long reviewId) {

        // 로그인한 유저 (더미데이터)
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(()->new ReviewNotFoundException(REVIEW_NOT_FOUND));

        // 리뷰 작성자 아니면 삭제불가
        if(!review.getUser().getId().equals(user.getId())) {
            throw new NotReviewAuthorException(NOT_REVIEW_AUTHOR);
        }

        review.deleteReview();

        return ReviewDeleteResponse.builder()
                .reviewId(review.getId())
                .build();
    }
}
