package com.luckyseven.backend.domain.member.dto;

import com.luckyseven.backend.domain.member.entity.Member;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MemberResponseDto {
    private String nickname;

    private String email;

    private String password;

    public static MemberResponseDto of(Member member) {
        return MemberResponseDto.builder()
                .nickname(member.getNickname())
                .email(member.getEmail())
                .password(member.getPassword())
                .build();
    }
}
