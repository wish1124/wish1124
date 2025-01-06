package com.smartcontrol.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PasswordResetDTO {

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
             message = "비밀번호는 최소 8자 이상, 대문자, 소문자, 숫자 및 특수문자를 포함해야 합니다.")
    private String newPassword;

    @NotBlank(message = "토큰은 필수 항목입니다.")
    private String token;
}
