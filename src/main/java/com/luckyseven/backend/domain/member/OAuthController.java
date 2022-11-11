package com.luckyseven.backend.domain.member;

import com.luckyseven.backend.domain.member.dto.MemberResponseDto;
import com.luckyseven.backend.global.config.CommonApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;

@RestController
@AllArgsConstructor
@RequestMapping("kakao")
@Api(tags = "소셜 로그인")
public class OAuthController {
    private final OAuthService oAuthService;

    @GetMapping("oauth")
    @ApiOperation(value = "인가 코드 받기")
    public ResponseEntity<?> kakaoConnect() {
        String url = "kauth.kakao.com/oauth/authorize?" +
                "client_id=" + "46ec58d196bd0e72f56e34641ceec564" +
                "&redirect_uri=http://localhost:8080/kakao/callback" +
                "&response_type=code";
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(url));

        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @GetMapping("callback")
    public ResponseEntity<MemberResponseDto> login(@RequestParam String code) {
        MemberResponseDto memberResponseDto = oAuthService.login(code);

        return ResponseEntity.ok(memberResponseDto);
    }


}
