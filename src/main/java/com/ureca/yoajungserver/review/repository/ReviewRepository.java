package com.ureca.yoajungserver.review.repository;

import com.ureca.yoajungserver.plan.entity.Plan;
import com.ureca.yoajungserver.review.repository.custom.ReviewRepositoryCustom;
import com.ureca.yoajungserver.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<com.ureca.yoajungserver.review.entity.Review, Long>, ReviewRepositoryCustom {
    boolean existsByUserAndPlanAndIsDeletedFalse(User user, Plan plan);
}
