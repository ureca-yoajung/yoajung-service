package com.ureca.yoajungserver.review.entity;

import com.ureca.yoajungserver.common.BaseTimeEntity;
import jakarta.persistence.*;
import com.ureca.yoajungserver.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="reviewLike",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"userId","reviewId"})}) // unique 제약조건 설정.
public class ReviewLike extends BaseTimeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="reviewId", nullable = false)
    private Review review;


    @Builder
    private ReviewLike(Long id, User user, Review review) {
        this.id = id;
        this.user = user;
        this.review = review;
    }
}
