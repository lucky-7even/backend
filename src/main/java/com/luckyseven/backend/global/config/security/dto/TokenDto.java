package com.luckyseven.backend.global.config.security.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {
    // accessToken
    private String token;
}
