package com.luckyseven.backend.domain.member;

import static com.luckyseven.backend.global.error.ErrorCode.*;

import java.util.Optional;

import com.luckyseven.backend.domain.member.entity.Member;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luckyseven.backend.domain.member.dto.LoginDto;
import com.luckyseven.backend.domain.member.dto.MemberRequestDto;
import com.luckyseven.backend.domain.member.dto.MemberResponseDto;
import com.luckyseven.backend.global.config.CommonApiResponse;
import com.luckyseven.backend.global.config.security.dto.TokenResponseDto;
import com.luckyseven.backend.global.config.security.jwt.TokenProvider;
import com.luckyseven.backend.global.error.ErrorCode;
import com.luckyseven.backend.global.error.exception.BadRequestException;
import com.luckyseven.backend.global.error.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;

    // 일반 회원가입
    @Transactional
    public MemberResponseDto makeMember(MemberRequestDto memberRequestDto) {
        Optional<Member> checkMember = memberRepository.findByEmail(memberRequestDto.getEmail());
        if (checkMember.isPresent() && checkMember.get().isSocial()) {
            throw new BadRequestException(ErrorCode.SOCIAL_ALREADY_EXIST);
        } else if (checkMember.isPresent() && !checkMember.get().isSocial()) {
            throw new BadRequestException(ErrorCode.MEMBER_ALREADY_EXIST);
        }

        Member member = Member.builder()
                .nickname(memberRequestDto.getNickname())
                .email(memberRequestDto.getEmail())
                .password(passwordEncoder.encode(memberRequestDto.getPassword()))
                .isSocial(false)
                .build();
        memberRepository.save(member);
        return MemberResponseDto.of(member);
    }

    // 일반 로그인
    @Transactional
    public ResponseEntity<CommonApiResponse<MemberResponseDto>> loginMember(LoginDto loginDto) {
        Optional<Member> checkMember = memberRepository.findByEmail(loginDto.getEmail());
        if (checkMember.isEmpty()) {
            throw new BadRequestException(ErrorCode.MEMBER_NOT_FOUND);
        } else if (checkMember.get().isSocial()) {
            throw new BadRequestException(ErrorCode.SOCIAL_ALREADY_EXIST);
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        TokenResponseDto tokenResponseDTO = tokenProvider.generateToken(loginDto.getEmail());

        Member member = memberRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new BadRequestException(ErrorCode.MEMBER_NOT_FOUND));
        httpHeaders.add("Authorization", "Bearer " + tokenResponseDTO.getAccessToken());

        return new ResponseEntity<>(CommonApiResponse.of(MemberResponseDto.of(member, tokenResponseDTO)), httpHeaders, HttpStatus.OK);
    }

    // 토큰 재발급
    @Transactional
    public ResponseEntity<CommonApiResponse<TokenResponseDto>> reissue(String accessToken, String refreshToken) {
        String email;

        if (!tokenProvider.validateTokenExceptExpiration(accessToken)){
            throw new BadRequestException(ErrorCode.INVALID_ACCESS_TOKEN);
        }

        try {
            email = tokenProvider.parseClaims(accessToken).getSubject();
        } catch (Exception e) {
            throw new BadRequestException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        tokenProvider.validateRefreshToken(email, refreshToken);

        TokenResponseDto tokenResponseDto = tokenProvider.generateToken(email);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + tokenResponseDto.getAccessToken());

        return new ResponseEntity<>(CommonApiResponse.of(tokenResponseDto), httpHeaders, HttpStatus.OK);
    }

    public Member findOne(Long id) {
        return memberRepository.findById(id)
            .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
    }
}
