package com.ureca.yoajungserver.external.kakao;

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
public class KakaoSignupRequest {

    @NotBlank(message = "이름은 필수")
    private String name;

    @Email(message = "이메일 필수")
    @NotBlank(message = "이메일도 필수")
    private String email;

    @NotBlank(message = "비밀번호")
    @Size(min = 1, message = "비밀번호는 1자이상")
    private String password;

    private Long planId;

    @NotBlank(message = "전화번호 입력")
    @Pattern(regexp = "^010\\d{8}$", message = "형식을 맞추세요")
    private String phoneNumber;

    @NotNull(message = "성별")
    private Gender gender;

    @NotNull(message = "연령")
    private AgeGroup ageGroup;

    @NotNull(message = "가족 구성원수")
    private Integer familyCount;
}
