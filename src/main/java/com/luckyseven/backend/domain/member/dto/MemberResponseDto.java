package com.luckyseven.backend.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.luckyseven.backend.domain.member.entity.Member;
import com.luckyseven.backend.global.config.security.dto.TokenResponseDto;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) //NULL 필드 가림
public class MemberResponseDto {
    private String nickname;

    private String email;

    private String password;

    private String accessToken;

    private String refreshToken;

    public static MemberResponseDto of(Member member) {
        return MemberResponseDto.builder()
                .nickname(member.getNickname())
                .email(member.getEmail())
                .password(member.getPassword())
                .build();
    }

    public static MemberResponseDto of(Member member, TokenResponseDto tokenResponseDto) {
        return MemberResponseDto.builder()
                .nickname(member.getNickname())
                .email(member.getEmail())
                .password(member.getPassword())
                .accessToken(tokenResponseDto.getAccessToken())
                .refreshToken(tokenResponseDto.getRefreshToken())
                .build();
    }
}
