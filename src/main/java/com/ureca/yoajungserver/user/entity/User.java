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
@Table(name = "user")
public class User extends BaseTimeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "planId")
    private Plan plan;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phoneNumber", length = 11, nullable = false)
    private String phoneNumber;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "ageGroup", nullable = false)
    @Enumerated(EnumType.STRING)
    private AgeGroup ageGroup;

    @Column(name = "familyCount", nullable = false)
    private Integer familyCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
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

    public void updateInfo(String name, String phoneNumber, Gender gender, AgeGroup ageGroup, Integer familyCount, Plan plan) {
        if (name != null) {
            this.name = name;
        }
        if (phoneNumber != null) {
            this.phoneNumber = phoneNumber;
        }
        if (gender != null) {
            this.gender = gender;
        }
        if (ageGroup != null) {
            this.ageGroup = ageGroup;
        }
        if (familyCount != null) {
            this.familyCount = familyCount;
        }
        this.plan = plan;
    }
}
