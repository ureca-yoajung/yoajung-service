package com.ureca.yoajungserver.review.repository;


import com.ureca.yoajungserver.review.entity.Review;
import com.ureca.yoajungserver.review.entity.ReviewLike;
import com.ureca.yoajungserver.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

    @Query("select rl from ReviewLike rl where rl.user.id = :userId and rl.review.id = :reviewId")
    Optional<ReviewLike> findByUserIdAndReviewId(@Param("userId") Long userId, @Param("reviewId") Long reviewId);
}
