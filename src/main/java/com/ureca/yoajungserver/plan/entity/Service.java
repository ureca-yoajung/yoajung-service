package com.ureca.yoajungserver.plan.entity;

import com.ureca.yoajungserver.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Service extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String serviceImage;

    @OneToMany(mappedBy = "service")
    private List<PlanService> planServices = new ArrayList<>();

    @Builder
    private Service(ServiceType serviceType, String name, String description, String serviceImage) {
        this.serviceType = serviceType;
        this.name = name;
        this.description = description;
        this.serviceImage = serviceImage;
    }
}
