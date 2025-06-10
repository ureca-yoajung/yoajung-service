package com.ureca.yoajungserver.plan.entity;

import org.springframework.data.domain.Sort;

import java.util.Optional;

public enum PlanSortType {
    POPULAR,
    LOW_PRICE,
    HIGH_PRICE,
    HIGH_DATA;

    public static Optional<PlanSortType> fromType(String name) {
        try {
            return Optional.of(PlanSortType.valueOf(name.toUpperCase()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<Sort> toSort(PlanSortType sortType) {
        switch (sortType) {
            case LOW_PRICE:
                return Optional.of(Sort.by(
                        Sort.Order.asc("basePrice"),
                        Sort.Order.desc("dataAllowance")
                ));
            case HIGH_PRICE:
                return Optional.of(Sort.by(
                        Sort.Order.desc("basePrice"),
                        Sort.Order.desc("dataAllowance")
                ));
            case HIGH_DATA:
                return Optional.of(Sort.by(
                        Sort.Order.desc("dataAllowance"),
                        Sort.Order.desc("tetheringSharingAllowance"),
                        Sort.Order.asc("speedAfterLimit")
                ));
            case POPULAR:
            default:
                return Optional.empty(); // 실시간 데이터 사용하므로 Sort 불필요
        }
    }

}
