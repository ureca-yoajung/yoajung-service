package com.ureca.yoajungserver.plan.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ureca.yoajungserver.plan.dto.request.PlanFilterRequest;
import com.ureca.yoajungserver.plan.entity.*;
import com.ureca.yoajungserver.user.entity.AgeGroup;
import com.querydsl.core.types.OrderSpecifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


import com.ureca.yoajungserver.plan.entity.QPlanBenefit;
import com.ureca.yoajungserver.plan.entity.QBenefit;

@RequiredArgsConstructor
@Repository
public class PlanFilterRepository {
    private final JPAQueryFactory queryFactory; // Querydsl JPA 연산 담당 팩토리

    /**
     * 필터 조건에 맞는 요금제 목록 조회
     */
    public List<Plan> plans(PlanFilterRequest planFilterRequest){
        // Q 타입 인스턴스
        QPlan plan = QPlan.plan;
        QPlanProduct planProduct = QPlanProduct.planProduct;
        QProduct product = QProduct.product;
        QPlanBenefit planBenefit = QPlanBenefit.planBenefit;
        QBenefit benefit = QBenefit.benefit;
        String sort = planFilterRequest.getSort();
        boolean popularSort = "POPULAR".equalsIgnoreCase(sort);
        QPlanStatistic stat = popularSort ? QPlanStatistic.planStatistic : null;

        // 최신 통계일
        LocalDateTime from = null, to = null;
        if (popularSort){
            LocalDateTime latestTime = queryFactory.select(stat.createDate.max())
                    .from(stat)
                    .fetchOne();
            if (latestTime == null) return List.of();

            from = latestTime.toLocalDate().atStartOfDay();
            to   = from.plusDays(1).minusNanos(1);
        }

        BooleanBuilder booleanBuilder = buildWhereClause(planFilterRequest);

        // 정렬 지정
        OrderSpecifier<?> orderSpec;
        if ("POPULAR".equalsIgnoreCase(sort)) {
            orderSpec = null;
        } else if ("LOW_PRICE".equalsIgnoreCase(sort)) {
            orderSpec = plan.basePrice.asc();
        } else if ("HIGH_PRICE".equalsIgnoreCase(sort)) {
            orderSpec = plan.basePrice.desc();
        } else if ("DATA".equalsIgnoreCase(sort)) {
            orderSpec = plan.dataAllowance.desc();
        } else {
            orderSpec = plan.id.asc(); // default
        }

        int page = Optional.ofNullable(planFilterRequest.getPage()).orElse(0);
        int size = Optional.ofNullable(planFilterRequest.getSize()).orElse(9);

        JPAQuery<Plan> query;

        if (popularSort) {
            //  인기순
            query = queryFactory.select(plan)
                    .from(plan)
                    .leftJoin(plan.planProducts, planProduct)
                    .leftJoin(planProduct.product, product)
                    .leftJoin(stat).on(
                            stat.planId.eq(plan.id)
                                    .and(stat.createDate.between(from, to))
                    )
                    .where(booleanBuilder)
                    .groupBy(plan.id)
                    .orderBy(stat.userCount.max().desc())
                    .offset((long) page * size)
                    .limit(size);
        } else {
            query = queryFactory.selectDistinct(plan)
                    .from(plan)
                    .leftJoin(plan.planProducts, planProduct).fetchJoin()
                    .leftJoin(planProduct.product, product).fetchJoin()
                    .where(booleanBuilder)
                    .orderBy(orderSpec)
                    .offset((long) page * size)
                    .limit(size);
        }

        return query.fetch();
    }

    public long countPlans(PlanFilterRequest request) {
        QPlan plan = QPlan.plan;
        QPlanProduct planProduct = QPlanProduct.planProduct;
        QProduct product = QProduct.product;
        QPlanStatistic stat = QPlanStatistic.planStatistic;
        String sort = request.getSort();
        boolean popularSort = "POPULAR".equalsIgnoreCase(sort);
        LocalDateTime from = null, to = null;
        if (popularSort){
            LocalDateTime latestTime = queryFactory.select(stat.createDate.max())
                    .from(stat)
                    .fetchOne();
            if (latestTime == null) return 0;
            from = latestTime.toLocalDate().atStartOfDay();
            to   = from.plusDays(1).minusNanos(1);
        }
        BooleanBuilder booleanBuilder = buildWhereClause(request);
        JPAQuery<?> query;
        if (popularSort) {
            query = queryFactory.select(plan.id.countDistinct())
                    .from(plan)
                    .leftJoin(plan.planProducts, planProduct)
                    .leftJoin(planProduct.product, product)
                    .leftJoin(stat).on(
                            stat.planId.eq(plan.id)
                                    .and(stat.createDate.between(from, to))
                    )
                    .where(booleanBuilder);
        } else {
            query = queryFactory.select(plan.id.countDistinct())
                    .from(plan)
                    .leftJoin(plan.planProducts, planProduct)
                    .leftJoin(planProduct.product, product)
                    .where(booleanBuilder);
        }
        Long count = (Long) query.fetchOne();
        return count != null ? count : 0L;
    }

    // Added: buildWhereClause helper for PlanFilterRequest
    private BooleanBuilder buildWhereClause(PlanFilterRequest planFilterRequest) {
        QPlan plan = QPlan.plan;
        QPlanProduct planProduct = QPlanProduct.planProduct;
        QProduct product = QProduct.product;
        QPlanStatistic stat = QPlanStatistic.planStatistic;
        BooleanBuilder builder = new BooleanBuilder();

        // 카테고리
        if(planFilterRequest.getCategory() != null && planFilterRequest.getCategory() != PlanCategory.ALL){
            builder.and(plan.planCategory.eq(planFilterRequest.getCategory()));
        }
        // 연령대 (인기순인 경우에만 사용됨)
        if ("POPULAR".equalsIgnoreCase(planFilterRequest.getSort()) &&
            planFilterRequest.getAgeGroup() != null &&
            planFilterRequest.getAgeGroup() != AgeGroup.ALL) {
            builder.and(stat.ageGroup.eq(planFilterRequest.getAgeGroup()));
        }
        // 가격 범위
        if (planFilterRequest.getPriceRange() != null) {
            switch (planFilterRequest.getPriceRange()) {
                case "UNDER5"      -> builder.and(plan.basePrice.loe(59900));
                case "BETWEEN6_8"  -> builder.and(plan.basePrice.between(60000, 89900));
                case "ABOVE9"      -> builder.and(plan.basePrice.goe(90000));
            }
        }
        // 데이터 타입
        String dt = planFilterRequest.getDataType();
        if (dt != null && !dt.isBlank()) {
            switch (dt) {
                case "UNLIMITED"  -> builder.and(plan.dataAllowance.goe(9999));
                case "FIXED"      -> builder.and(plan.dataAllowance.lt(9999)
                                              .and(plan.speedAfterLimit.eq(0)));
                case "THROTTLED"  -> builder.and(plan.dataAllowance.lt(9999)
                                              .and(plan.speedAfterLimit.gt(0)));
            }
        }
        // 프로덕트 이름
        if (planFilterRequest.getProductNames() != null && !planFilterRequest.getProductNames().isEmpty())
            builder.and(product.name.in(planFilterRequest.getProductNames()));

        builder.and(plan.deletedAt.isNull());
        return builder;
    }
}
