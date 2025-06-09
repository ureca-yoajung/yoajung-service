package com.ureca.yoajungserver.user.entity;

import com.ureca.yoajungserver.common.BaseTimeEntity;
import com.ureca.yoajungserver.user.dto.reqeust.TendencyRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tendency extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    @Column(nullable = false)
    private Integer avgMonthlyDataGB;

    @Column(nullable = false)
    private Integer avgMonthlyVoiceMin;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private Integer preferencePrice;

    @Builder
    private Tendency(User user, Integer avgMonthlyDataGB, Integer avgMonthlyVoiceMin, String comment, Integer preferencePrice) {
        this.user = user;
        this.avgMonthlyDataGB = avgMonthlyDataGB;
        this.avgMonthlyVoiceMin = avgMonthlyVoiceMin;
        this.comment = comment;
        this.preferencePrice = preferencePrice;
    }

    public void updateTendency(TendencyRequest request) {
        this.avgMonthlyDataGB = request.getAvgMonthLyDataGB();
        this.avgMonthlyVoiceMin = request.getAvgMonthlyVoiceMin();
        this.comment = request.getComment();
        this.preferencePrice = request.getPreferencePrice();
    }
}
