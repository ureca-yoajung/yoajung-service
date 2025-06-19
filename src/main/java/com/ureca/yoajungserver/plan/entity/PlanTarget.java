package com.ureca.yoajungserver.plan.entity;

import java.util.Optional;

public enum PlanTarget {
    ALL, YOUTH, WELFARE, SENIOR, KIDS, TEEN, SOLDIER, ONLINE;

    public static Optional<PlanTarget> fromType(String name) {
        try {
            return Optional.of(PlanTarget.valueOf(name.toUpperCase()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
