package com.luckyseven.backend.global.config.security.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenResponseDto {

    private String accessToken;

    private String refreshToken;
}
