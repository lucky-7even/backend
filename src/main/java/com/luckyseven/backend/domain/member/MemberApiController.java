package com.luckyseven.backend.domain.member;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.luckyseven.backend.domain.member.dto.LoginDto;
import com.luckyseven.backend.domain.member.dto.MemberRequestDto;
import com.luckyseven.backend.domain.member.dto.MemberResponseDto;
import com.luckyseven.backend.global.config.CommonApiResponse;
import com.luckyseven.backend.global.config.security.dto.TokenResponseDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/members")
@Api(tags = "일반 회원가입, 로그인")
public class MemberApiController {
    private final MemberService memberService;
    
    @PostMapping("signup")
    @ApiOperation(value = "일반 회원가입")
    public ResponseEntity<CommonApiResponse<MemberResponseDto>> makeMember(
            @Valid @RequestBody MemberRequestDto memberRequestDto) {
        return ResponseEntity.ok(CommonApiResponse.of(memberService.makeMember(memberRequestDto)));
    }
    
    @PostMapping("login")
    @ApiOperation(value = "일반 로그인")
    public ResponseEntity<CommonApiResponse<MemberResponseDto>> loginMember(
            @Valid @RequestBody LoginDto loginDto) {
        return memberService.loginMember(loginDto);
    }
    
    @PostMapping("reissue")
    @ApiOperation(value = "토큰 재발급")
    public ResponseEntity<CommonApiResponse<TokenResponseDto>> reissue(
            @RequestHeader("Authorization") String accessToken,
            @RequestHeader("Refresh") String refreshToken) {
        return memberService.reissue(accessToken, refreshToken);
    }
}
