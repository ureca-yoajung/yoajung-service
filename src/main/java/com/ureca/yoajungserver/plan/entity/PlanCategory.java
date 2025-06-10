package com.ureca.yoajungserver.plan.entity;

import java.util.Optional;

public enum PlanCategory {
    LTE_FIVE_G, ONLINE_ONLY, TABLET_WATCH, DUAL_NUMBER;

    public static Optional<PlanCategory> fromType(String name) {
        try {
            return Optional.of(PlanCategory.valueOf(name.toUpperCase()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
