package com.ureca.yoajungserver.plan.dto.response;

import com.ureca.yoajungserver.plan.entity.Service;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ListServiceDto {
    private String name;
    private String serviceImage;

    public static ListServiceDto fromService(Service service) {
        return ListServiceDto.builder()
                .name(service.getName())
                .serviceImage(service.getServiceImage())
                .build();
    }
}
