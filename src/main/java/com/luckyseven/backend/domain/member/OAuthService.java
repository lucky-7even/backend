package com.luckyseven.backend.domain.member;

import com.luckyseven.backend.domain.member.dto.UserProfile;
import com.luckyseven.backend.global.config.security.oauth.InMemoryProviderRepository;
import com.luckyseven.backend.global.config.security.oauth.OauthAttributes;
import com.luckyseven.backend.global.config.security.oauth.OauthProvider;
import com.luckyseven.backend.global.config.security.oauth.dto.KakaoUserInfo;
import com.luckyseven.backend.domain.member.dto.MemberResponseDto;
import com.luckyseven.backend.domain.member.entity.Member;
import com.luckyseven.backend.global.config.security.dto.OauthTokenResponse;
import com.luckyseven.backend.global.config.security.dto.TokenResponseDto;
import com.luckyseven.backend.global.config.security.jwt.TokenProvider;
import com.luckyseven.backend.global.error.ErrorCode;
import com.luckyseven.backend.global.error.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class OAuthService {
    private final InMemoryClientRegistrationRepository inMemoryRepository;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final InMemoryProviderRepository inMemoryProviderRepository;

    @Transactional
    public ResponseEntity<MemberResponseDto> kakaoLogin(String code) {
        String providerName = "kakao";
        ClientRegistration provider = inMemoryRepository.findByRegistrationId(providerName);
        OauthTokenResponse tokenResponse = getToken(code, provider);
        Member member = getUserProfile(tokenResponse, provider);

        HttpHeaders httpHeaders = new HttpHeaders();

        // 회원 정보가 있고 -> 소셜로 가입한 이력이 있다면 -> 로그인(토큰 발급)
        // 회원 정보가 없고 -> 소셜로 가입하려면 -> 회원가입 -> 로그인(토큰 발급)
        // 낼 중복처리(11/15)
        if ((!memberRepository.existsByEmail(member.getEmail()))) {
            memberRepository.save(member);
        } else if (memberRepository.existsByEmailAndIsSocialFalse(member.getEmail())
                && !memberRepository.existsByEmailAndIsSocialTrue(member.getEmail())) {
            memberRepository.save(member);
        } else if (memberRepository.existsByEmailAndIsSocialTrue(member.getEmail())
                && memberRepository.existsByEmailAndIsSocialFalse(member.getEmail())) {
            log.info("바로 로그인으로 넘어갑니다.");
        }
        Member finalMember = memberRepository.findByEmail(member.getEmail()).orElseThrow(
                () -> new BadRequestException(ErrorCode.MEMBER_NOT_FOUND));

        TokenResponseDto tokenResponseDto = tokenProvider.generateToken(finalMember.getEmail());
        httpHeaders.add("Authorization", "Bearer " + tokenResponseDto.getAccessToken());
        
        return new ResponseEntity<>(MemberResponseDto.of(finalMember, tokenResponseDto), httpHeaders, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<MemberResponseDto> loginNaverGoogle(String providerName, String code) {
        // 프론트에서 넘어온 provider 이름을 통해 InMemoryProviderRepository에서 OauthProvider 가져오기
        OauthProvider provider = inMemoryProviderRepository.findByProviderName(providerName);

        // access token 가져오기
        OauthTokenResponse tokenResponse = getToken(code, provider);

        // 유저 정보 가져오기
        UserProfile userProfile = getUserProfile(providerName, tokenResponse, provider);

        Member member = Member.builder()
                .nickname(userProfile.getName())
                .email(userProfile.getEmail())
                .profileImage(userProfile.getImageUrl())
                .isSocial(true)
                .build();

        // 유저 DB에 저장
        if ((!memberRepository.existsByEmail(member.getEmail()))) {
            memberRepository.save(member);
        } else if (memberRepository.existsByEmailAndIsSocialFalse(member.getEmail())
                && !memberRepository.existsByEmailAndIsSocialTrue(member.getEmail())) {
            memberRepository.save(member);
        } else if (memberRepository.existsByEmailAndIsSocialTrue(member.getEmail())
                && memberRepository.existsByEmailAndIsSocialFalse(member.getEmail())) {
            log.info("바로 로그인으로 넘어갑니다.");
        }

        TokenResponseDto tokenResponseDto = tokenProvider.generateToken(member.getEmail());
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("Authorization", "Bearer " + tokenResponseDto.getAccessToken());

        return new ResponseEntity<>(MemberResponseDto.of(member, tokenResponseDto), httpHeaders, HttpStatus.OK);
    }

    // 네이버, 구글
    private OauthTokenResponse getToken(String code, OauthProvider provider) {
        return WebClient.create()
                .post()
                .uri(provider.getTokenUrl())
                .headers(header -> {
                    header.setBasicAuth(provider.getClientId(), provider.getClientSecret());
                    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    header.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                    header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                })
                .bodyValue(tokenRequest(code, provider))
                .retrieve()
                .bodyToMono(OauthTokenResponse.class)
                .block();
    }

    // 카카오
    private OauthTokenResponse getToken(String code, ClientRegistration provider) {
        return WebClient.create()
                .post()
                .uri(provider.getProviderDetails().getTokenUri())
                .headers(header -> {
                    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                })
                .bodyValue(tokenRequest(code, provider))
                .retrieve()
                .bodyToMono(OauthTokenResponse.class)
                .block();
    }

    // 네이버, 구글
    private MultiValueMap<String, String> tokenRequest(String code, OauthProvider provider) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("grant_type", "authorization_code");
        formData.add("redirect_uri", provider.getRedirectUrl());
        return formData;
    }

    // 카카오
    private MultiValueMap<String, String> tokenRequest(String code, ClientRegistration provider) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("grant_type", "authorization_code");
        formData.add("redirect_uri", provider.getRedirectUri());
        formData.add("client_secret", provider.getClientSecret());
        formData.add("client_id", provider.getClientId());
        return formData;
    }
    
    // 카카오
    private Member getUserProfile(OauthTokenResponse tokenResponse, ClientRegistration provider) {
        Map<String, Object> userAttributes = getUserAttributes(provider, tokenResponse);
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(userAttributes);

        String nickName = kakaoUserInfo.getNickName();
        String email = kakaoUserInfo.getEmail();
        String imageUrl = kakaoUserInfo.getImageUrl();

        return Member.builder()
                .nickname(nickName)
                .email(email)
                .profileImage(imageUrl)
                .isSocial(true)
                .build();
    }

    // 네이버, 구글
    private UserProfile getUserProfile(String providerName, OauthTokenResponse tokenResponse, OauthProvider provider) {
        Map<String, Object> userAttributes = getUserAttributes(provider, tokenResponse);

        return OauthAttributes.extract(providerName, userAttributes);
    }

    // 카카오
    private Map<String, Object> getUserAttributes(ClientRegistration provider, OauthTokenResponse tokenResponse) {
        return WebClient.create()
                .get()
                .uri(provider.getProviderDetails().getUserInfoEndpoint().getUri())
                .headers(header -> header.setBearerAuth(tokenResponse.getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }

    // OAuth 서버에서 유저 정보 map으로 가져오기(네이버, 구글)
    private Map<String, Object> getUserAttributes(OauthProvider provider, OauthTokenResponse tokenResponse) {
        return WebClient.create()
                .get()
                .uri(provider.getUserInfoUrl())
                .headers(header -> header.setBearerAuth(tokenResponse.getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }
}
