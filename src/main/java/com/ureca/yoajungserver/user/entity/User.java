package com.ureca.yoajungserver.user.entity;

import com.ureca.yoajungserver.common.BaseTimeEntity;
import com.ureca.yoajungserver.plan.entity.Plan;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Plan plan;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(length = 11, nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AgeGroup ageGroup;

    @Column(nullable = false)
    private Integer familyCount;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    private User(Plan plan, String name, String email, String password, String phoneNumber, Gender gender, AgeGroup ageGroup, Integer familyCount, Role role) {
        this.plan = plan;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.ageGroup = ageGroup;
        this.familyCount = familyCount;
        this.role = role;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }
}
