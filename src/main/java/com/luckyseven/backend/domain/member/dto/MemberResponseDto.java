package com.luckyseven.backend.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.luckyseven.backend.domain.member.Member;
import com.luckyseven.backend.global.config.security.dto.TokenResponseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private String profileImage;

    private boolean isSocial;

    private String accessToken;

    private String refreshToken;

    public static MemberResponseDto of(Member member) {
        return MemberResponseDto.builder()
                .nickname(member.getNickname())
                .email(member.getEmail())
                .password(member.getPasswd())
                .profileImage(member.getProfileImage())
                .isSocial(member.isSocial())
                .build();
    }

    public static MemberResponseDto of(Member member, TokenResponseDto tokenResponseDto) {
        return MemberResponseDto.builder()
                .nickname(member.getNickname())
                .email(member.getEmail())
                .password(member.getPasswd())
                .profileImage(member.getProfileImage())
                .isSocial(member.isSocial())
                .accessToken(tokenResponseDto.getAccessToken())
                .refreshToken(tokenResponseDto.getRefreshToken())
                .build();
    }
}
