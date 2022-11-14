package com.luckyseven.backend.domain.member.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    @ApiModelProperty(example = "jimin112688@gmail.com")
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    private String email;

    @ApiModelProperty(example = "rejin0421")
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;
}
