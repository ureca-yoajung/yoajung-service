package com.ureca.yoajungserver.user.dto.reqeust;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetConfirmRequest {
    @NotBlank(message = "토큰 필요")
    private String token;

    @NotBlank(message = "비밀번호")
    @Size(min = 1, message = "비밀번호는 1자이상")
    private String password;
}
