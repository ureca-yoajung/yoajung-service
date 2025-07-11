package com.ureca.yoajungserver.user.repository;

import com.ureca.yoajungserver.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("select u from User u left join fetch u.plan where u.email = :email")
    Optional<User> findByEmailWithPlan(@Param("email") String email);
    // user 엔티티 DTO 매핑에서 plan 까지 한번에 가져오는 fetch join

    // 일주일 전부터 하루 전까지의 데이터로 인기 요금제 조회
    @Query("SELECT u.plan.id " +
            "FROM User u " +
            "WHERE u.createDate >= :startDate AND u.createDate < :endDate " +
            "GROUP BY u.plan " +
            "ORDER BY COUNT(u.plan) DESC")
    List<Long> findPopularPlansInDateRange(@Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);
}
