package com.ureca.yoajungserver.plan.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ureca.yoajungserver.plan.dto.request.PlanFilterRequest;
import com.ureca.yoajungserver.plan.entity.Plan;
import com.ureca.yoajungserver.plan.entity.QPlan;
import com.ureca.yoajungserver.plan.entity.QPlanProduct;
import com.ureca.yoajungserver.plan.entity.QProduct;
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
        booleanBuilder.and(plan.planCategory.eq(planFilterRequest.getCategory())); // 카테고리 필터

        // 요금 범위
        if (planFilterRequest.getMinPrice()!=null) booleanBuilder.and(plan.basePrice.goe(planFilterRequest.getMinPrice()));
        if (planFilterRequest.getMaxPrice()!=null) booleanBuilder.and(plan.basePrice.loe(planFilterRequest.getMaxPrice()));

        // 데이터 ( 무제한, 정량제/속도제한 )
        if (Boolean.TRUE.equals(planFilterRequest.getUnlimited()))
            booleanBuilder.and(plan.dataAllowance.goe(9999)); // 무제한
        else if (planFilterRequest.getUnlimited() != null && !planFilterRequest.getUnlimited())
            booleanBuilder.and(plan.dataAllowance.lt(9999)); // 정량/속도제한

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
