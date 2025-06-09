package com.ureca.yoajungserver.review.repository.custom;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ureca.yoajungserver.review.dto.response.ReviewListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ureca.yoajungserver.review.entity.QReview.review;
import static com.ureca.yoajungserver.review.entity.QReviewLike.reviewLike;
import static com.ureca.yoajungserver.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    // 리뷰 조회
    @Override
    public Page<ReviewListResponse> findReviewList(Long userId, Long planId, Pageable pageable) {
        // 조회 (페이징)
        List<ReviewListResponse> reviewList = jpaQueryFactory
                .select(Projections.constructor(ReviewListResponse.class,
                        review.id,
                        review.user.id,
                        review.user.name,
                        review.content,
                        review.star,
                        reviewLike.id.count(),
                        review.createDate,
                        review.user.id.eq(userId),
                        JPAExpressions
                                .selectOne()
                                .from(reviewLike)
                                .where(reviewLike.user.id.eq(userId)
                                        .and(reviewLike.review.id.eq(review.id)))
                                .exists()
                ))
                .from(review)
                .join(review.user, user)
                .leftJoin(reviewLike).on(reviewLike.review.id.eq(review.id))
                .where(review.plan.id.eq(planId)
                        .and(review.isDeleted.isFalse()))
                .groupBy(review.id, review.user.id, review.user.name, review.content, review.star, review.createDate)
                .orderBy(review.createDate.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        // 개수 count
        JPAQuery<Long> count = jpaQueryFactory
                .select(review.count())
                .from(review)
                .where(review.plan.id.eq(planId)
                        .and(review.isDeleted.isFalse()));

        return PageableExecutionUtils.getPage(reviewList, pageable, count::fetchOne);
    }

    // 별점 평균
    @Override
    public Double avgStar(Long planId) {
        return jpaQueryFactory
                .select(review.star.avg())
                .from(review)
                .where(review.plan.id.eq(planId)
                        .and(review.isDeleted.isFalse()))
                .fetchOne();
    }

    // 요금제 사용자 여부
    @Override
    public Boolean isPlanUser(Long userId, Long planId) {
        return jpaQueryFactory
                .selectOne()
                .from(user)
                .where(user.id.eq(userId)
                        .and(user.plan.id.eq(planId)))
                .fetchOne() != null;
    }

}