package com.ureca.yoajungserver.user.dto.reqeust;

import com.ureca.yoajungserver.user.entity.AgeGroup;
import com.ureca.yoajungserver.user.entity.Gender;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequest {
    private String name;

    @Pattern(regexp = "^010\\d{8}$", message = "형식을 맞추세요")
    private String phoneNumber;

    private Gender gender;
    private AgeGroup ageGroup;
    private Integer familyCount;
    private Long planId;
}
