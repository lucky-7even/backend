package com.luckyseven.backend.domain.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.luckyseven.backend.domain.member.dto.MemberResponseDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Api(tags = "소셜 로그인")
public class OAuthController {
    private final OAuthService oAuthService;

    @ApiOperation(value = "카카오 로그인")
    @GetMapping("login/oauth/{provider}")
    public ResponseEntity<MemberResponseDto> login(@PathVariable String provider, @RequestParam String code) {
        return oAuthService.kakaoLogin(provider, code);
    }
}
