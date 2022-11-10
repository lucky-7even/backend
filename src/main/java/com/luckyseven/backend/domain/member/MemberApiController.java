package com.luckyseven.backend.domain.member;

import com.luckyseven.backend.domain.member.dto.LoginDto;
import com.luckyseven.backend.domain.member.dto.MemberRequestDto;
import com.luckyseven.backend.domain.member.dto.MemberResponseDto;
import com.luckyseven.backend.global.config.CommonApiResponse;
import com.luckyseven.backend.global.config.security.dto.TokenRequestDto;
import com.luckyseven.backend.global.config.security.dto.TokenResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/members")
@Api(tags = "회원가입")
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
    public ResponseEntity<CommonApiResponse<TokenResponseDto>> refreshToken(
            @RequestBody TokenRequestDto tokenRequestDto) {
        return memberService.refreshMember(tokenRequestDto);
    }
}
