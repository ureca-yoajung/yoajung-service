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
@Table(name = "tendency")
public class Tendency extends BaseTimeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(name = "avgMonthlyDataGB", nullable = false)
    private Integer avgMonthlyDataGB;

    @Column(name = "avgMonthlyVoiceMin", nullable = false)
    private Integer avgMonthlyVoiceMin;

    @Column(name = "comment", nullable = false)
    private String comment;

    @Column(name = "preferencePrice", nullable = false)
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
        this.avgMonthlyDataGB = request.getAvgMonthlyDataGB();
        this.avgMonthlyVoiceMin = request.getAvgMonthlyVoiceMin();
        this.comment = request.getComment();
        this.preferencePrice = request.getPreferencePrice();
    }
}
