package com.luckyseven.backend.domain.member;

import com.luckyseven.backend.domain.member.dto.MemberResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
@RequestMapping("kakao")
@Api(tags = "소셜 로그인")
public class OAuthController {
    private final OAuthService oAuthService;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUrl;

    @GetMapping("oauth")
    @ApiOperation(value = "인가 코드 받기")
    public ResponseEntity<?> kakaoConnect() throws URISyntaxException {
        String url = "https://kauth.kakao.com/oauth/authorize?" +
                "client_id=" + "46ec58d196bd0e72f56e34641ceec564" +
                "&redirect_uri=" + redirectUrl +
                "&response_type=code";
        URI redirectUri = new URI(url);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(redirectUri);
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }

    @GetMapping("callback")
    public ResponseEntity<MemberResponseDto> login(@RequestParam String code) {
        return oAuthService.login(code);
    }
}
