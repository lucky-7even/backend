package com.luckyseven.backend.domain.member;

import com.luckyseven.backend.domain.member.dto.MemberResponseDto;
import com.luckyseven.backend.global.config.security.oauth.dto.AuthCodeRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
@RequestMapping("oauth")
@Api(tags = "소셜 로그인")
public class OAuthController {
    private final OAuthService oAuthService;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoRedirectUrl;

    @Value("${oauth2.user.naver.redirect-uri}")
    private String naverRedirectUrl;

    @Value("${oauth2.user.google.redirect-uri}")
    private String googleRedirectUrl;

    @GetMapping("kakao")
    @ApiOperation(value = "카카오 인가 코드 받기")
    public ResponseEntity<?> kakaoConnect() throws URISyntaxException {
        String url = "https://kauth.kakao.com/oauth/authorize?" +
                "client_id=" + "46ec58d196bd0e72f56e34641ceec564" +
                "&redirect_uri=" + kakaoRedirectUrl +
                "&response_type=code";
        URI redirectUri = new URI(url);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(redirectUri);
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }

    @GetMapping("naver")
    @ApiOperation(value = "네이버 인가 코드 받기")
    public ResponseEntity<?> naverConnect() throws URISyntaxException {
        String url = "https://nid.naver.com/oauth2.0/authorize?" +
                "response_type=code" +
                "&client_id=" + "W3Flx3FAW3HXvuFho44N" +
                "&redirect_uri=" + naverRedirectUrl;
        URI redirectUri = new URI(url);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(redirectUri);
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }

    @GetMapping("google")
    @ApiOperation(value = "구글 인가 코드 받기")
    public ResponseEntity<?> googleConnect() throws URISyntaxException {
        String url = "https://accounts.google.com/o/oauth2/v2/auth?" +
                "&scope=https://www.googleapis.com/auth/userinfo.email" +
                "&access_type=offline" +
                "&include_granted_scopes=true" +
                "&response_type=code" +
                "&client_id=" + "737164190018-n4pfqlkmbkg6faijmof10ek07alp7utj.apps.googleusercontent.com" +
                "&redirect_uri=" + googleRedirectUrl;
        URI redirectUri = new URI(url);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(redirectUri);
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }

    /*@GetMapping("kakao/callback")
    @ApiOperation(value = "카카오 access, refreshToken 발급")
    public ResponseEntity<MemberResponseDto> login(@RequestParam String code) {
        return oAuthService.kakaoLogin(code);
    }*/

    @GetMapping("naver/callback")
    @ApiOperation(value = "네이버, 구글 access, refreshToken 발급")
    public ResponseEntity<MemberResponseDto> loginNaver(@RequestParam String code) {
        return oAuthService.loginNaverGoogle("naver", code);
    }

    @GetMapping("google/callback")
    @ApiOperation(value = "네이버, 구글 access, refreshToken 발급")
    public ResponseEntity<MemberResponseDto> loginGoogle(@RequestParam String code) {
        return oAuthService.loginNaverGoogle("google", code);
    }

    @GetMapping("kakao/findId")
    public ResponseEntity<String> findId(Authentication authentication) {
        return ResponseEntity.ok(authentication.getName());
    }

    @PostMapping("kakao/callback")
    @ApiOperation(value = "카카오 access, refreshToken 발급")
    public ResponseEntity<MemberResponseDto> loginKakao(
            @RequestBody AuthCodeRequest authCodeRequest
    ) {
        System.out.println(authCodeRequest.getCode());
        return oAuthService.kakaoLogin(authCodeRequest.getCode());
    }
}
