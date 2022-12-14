package com.luckyseven.backend.domain.member;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import com.luckyseven.backend.domain.member.entity.Member;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import com.luckyseven.backend.domain.member.dto.MemberResponseDto;
import com.luckyseven.backend.domain.member.dto.UserProfile;
import com.luckyseven.backend.global.config.security.dto.OauthTokenResponse;
import com.luckyseven.backend.global.config.security.dto.TokenResponseDto;
import com.luckyseven.backend.global.config.security.jwt.TokenProvider;
import com.luckyseven.backend.global.config.security.oauth.InMemoryProviderRepository;
import com.luckyseven.backend.global.config.security.oauth.OauthAttributes;
import com.luckyseven.backend.global.config.security.oauth.OauthProvider;
import com.luckyseven.backend.global.config.security.oauth.dto.KakaoUserInfo;
import com.luckyseven.backend.global.error.ErrorCode;
import com.luckyseven.backend.global.error.exception.BadRequestException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class OAuthService {
    private final InMemoryClientRegistrationRepository inMemoryRepository;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final InMemoryProviderRepository inMemoryProviderRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ResponseEntity<MemberResponseDto> kakaoLogin(String code) {
        String providerName = "kakao";
        ClientRegistration provider = inMemoryRepository.findByRegistrationId(providerName);
        OauthTokenResponse tokenResponse = getToken(code, provider);
        Member member = getUserProfile(tokenResponse, provider);

        Optional<Member> checkMember = memberRepository.findByEmail(member.getEmail());
        if (checkMember.isPresent() && !member.isSocial()) {
            throw new BadRequestException(ErrorCode.COMMON_ALREADY_EXIST);
        }

        // ?????? ????????? ?????????? -> ?????????(?????? ??????)
        // ?????? ????????? ?????????? -> ???????????? -> ?????????(?????? ??????)
        if ((!memberRepository.existsByEmail(member.getEmail()))) {
            memberRepository.save(member);
        } else if (memberRepository.existsByEmail(member.getEmail())) {
            log.info("?????? ??????????????? ???????????????.");
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(member.getEmail(), "social");

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Member finalMember = memberRepository.findByEmail(member.getEmail())
                .orElseThrow(() -> new BadRequestException(ErrorCode.MEMBER_NOT_FOUND));

        HttpHeaders httpHeaders = new HttpHeaders();
        TokenResponseDto tokenResponseDto = tokenProvider.generateToken(finalMember.getEmail());
        httpHeaders.add("Authorization", "Bearer " + tokenResponseDto.getAccessToken());
        
        return new ResponseEntity<>(MemberResponseDto.of(finalMember, tokenResponseDto), httpHeaders, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<MemberResponseDto> loginNaverGoogle(String providerName, String code) {
        // ??????????????? ????????? provider ????????? ?????? InMemoryProviderRepository?????? OauthProvider ????????????
        OauthProvider provider = inMemoryProviderRepository.findByProviderName(providerName);

        // access token ????????????
        OauthTokenResponse tokenResponse = getToken(code, provider);

        // ?????? ?????? ????????????
        UserProfile userProfile = getUserProfile(providerName, tokenResponse, provider);

        Member member = Member.builder()
                .nickname(userProfile.getName())
                .email(userProfile.getEmail())
                .password(passwordEncoder.encode("social"))
                .profileImage(userProfile.getImageUrl())
                .isSocial(true)
                .build();

        Optional<Member> checkMember = memberRepository.findByEmail(userProfile.getEmail());
        if (checkMember.isPresent() && !checkMember.get().isSocial()) {
            throw new BadRequestException(ErrorCode.COMMON_ALREADY_EXIST);
        }

        // ?????? DB??? ??????
        if ((!memberRepository.existsByEmail(member.getEmail()))) {
            memberRepository.save(member);
        } else if (memberRepository.existsByEmail(member.getEmail())) {
            log.info("?????? ??????????????? ???????????????.");
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(member.getEmail(), "social");

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenResponseDto tokenResponseDto = tokenProvider.generateToken(member.getEmail());
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("Authorization", "Bearer " + tokenResponseDto.getAccessToken());

        return new ResponseEntity<>(MemberResponseDto.of(member, tokenResponseDto), httpHeaders, HttpStatus.OK);
    }

    // ?????????, ??????
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

    // ?????????
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

    // ?????????, ??????
    private MultiValueMap<String, String> tokenRequest(String code, OauthProvider provider) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("grant_type", "authorization_code");
        formData.add("redirect_uri", provider.getRedirectUrl());
        return formData;
    }

    // ?????????
    private MultiValueMap<String, String> tokenRequest(String code, ClientRegistration provider) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("grant_type", "authorization_code");
        formData.add("redirect_uri", provider.getRedirectUri());
        formData.add("client_secret", provider.getClientSecret());
        formData.add("client_id", provider.getClientId());
        return formData;
    }
    
    // ?????????
    private Member getUserProfile(OauthTokenResponse tokenResponse, ClientRegistration provider) {
        Map<String, Object> userAttributes = getUserAttributes(provider, tokenResponse);
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(userAttributes);

        String nickName = kakaoUserInfo.getNickName();
        String email = kakaoUserInfo.getEmail();
        String imageUrl = kakaoUserInfo.getImageUrl();

        return Member.builder()
                .nickname(nickName)
                .email(email)
                .password(passwordEncoder.encode("social"))
                .profileImage(imageUrl)
                .isSocial(true)
                .build();
    }

    // ?????????, ??????
    private UserProfile getUserProfile(String providerName, OauthTokenResponse tokenResponse, OauthProvider provider) {
        Map<String, Object> userAttributes = getUserAttributes(provider, tokenResponse);

        return OauthAttributes.extract(providerName, userAttributes);
    }

    // ?????????
    private Map<String, Object> getUserAttributes(ClientRegistration provider, OauthTokenResponse tokenResponse) {
        return WebClient.create()
                .get()
                .uri(provider.getProviderDetails().getUserInfoEndpoint().getUri())
                .headers(header -> header.setBearerAuth(tokenResponse.getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }

    // OAuth ???????????? ?????? ?????? map?????? ????????????(?????????, ??????)
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
