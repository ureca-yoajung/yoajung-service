package com.ureca.yoajungserver.user.dto.reqeust;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @Email(message = "이메일 필수")
    @NotBlank(message = "이메일도 필수")
    private String email;

    @NotBlank(message = "비밀번호")
    @Size(min = 1, message = "비밀번호는 1자이상")
    private String password;

    private Boolean remember;
}
