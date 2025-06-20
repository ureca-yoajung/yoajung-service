package com.ureca.yoajungserver.user.dto.response;

import com.ureca.yoajungserver.user.entity.AgeGroup;
import com.ureca.yoajungserver.user.entity.Gender;
import com.ureca.yoajungserver.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserResponse {
    private String email;
    private String name;
    private String phoneNumber;
    private Gender gender;
    private AgeGroup ageGroup;
    private Integer familyCount;
    private String planName;

    public static UserResponse fromEntity(User user) {
        return UserResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender())
                .ageGroup(user.getAgeGroup())
                .familyCount(user.getFamilyCount())
                .planName(user.getPlan() == null ? null : user.getPlan().getName())
                .build();
    }
}
