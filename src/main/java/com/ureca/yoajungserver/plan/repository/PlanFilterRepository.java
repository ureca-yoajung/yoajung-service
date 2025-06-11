package com.ureca.yoajungserver.plan.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ureca.yoajungserver.plan.dto.request.PlanFilterRequest;
import com.ureca.yoajungserver.plan.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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

        BooleanBuilder booleanBuilder = new BooleanBuilder(); // 동적 where 절 구성

        if(planFilterRequest.getCategory() != null && planFilterRequest.getCategory() != PlanCategory.ALL){
            booleanBuilder.and(plan.planCategory.eq(planFilterRequest.getCategory())); // 카테고리 필터, ALL 이 아닐 때
        }

        // 가격 범위 필터
        if (planFilterRequest.getPriceRange() != null) {
            switch (planFilterRequest.getPriceRange()) {
                case "UNDER5"      -> booleanBuilder.and(plan.basePrice.loe(59900));
                case "BETWEEN6_8"  -> booleanBuilder.and(plan.basePrice.between(60000, 89900));
                case "ABOVE9"      -> booleanBuilder.and(plan.basePrice.goe(90000));
            }
        }

        // 데이터 타입 필터
        String dt = planFilterRequest.getDataType();
        if (dt != null && !dt.isBlank()) {
            switch (dt) {
                case "UNLIMITED"  -> booleanBuilder.and(plan.dataAllowance.goe(9999));
                case "FIXED"      -> booleanBuilder.and(plan.dataAllowance.lt(9999)
                                                  .and(plan.speedAfterLimit.eq(0)));
                case "THROTTLED"  -> booleanBuilder.and(plan.dataAllowance.lt(9999)
                                                  .and(plan.speedAfterLimit.gt(0)));
            }
        }

        // 프로덕트 이름 필터
        if (planFilterRequest.getProductNames() != null && !planFilterRequest.getProductNames().isEmpty())
            booleanBuilder.and(product.name.in(planFilterRequest.getProductNames()));

        return queryFactory.selectDistinct(plan)
                .from(plan)
                .leftJoin(plan.planProducts, planProduct).fetchJoin()
                .leftJoin(planProduct.product, product).fetchJoin()
                .where(booleanBuilder)
                .fetch();
    }
}
