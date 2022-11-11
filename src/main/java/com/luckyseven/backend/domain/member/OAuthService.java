package com.luckyseven.backend.domain.member;

import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import com.luckyseven.backend.domain.member.dto.KakaoUserInfo;
import com.luckyseven.backend.domain.member.dto.MemberRequestDto;
import com.luckyseven.backend.domain.member.dto.MemberResponseDto;
import com.luckyseven.backend.domain.member.entity.Member;
import com.luckyseven.backend.global.config.security.dto.OauthTokenResponse;
import com.luckyseven.backend.global.config.security.jwt.TokenProvider;
import com.luckyseven.backend.global.error.ErrorCode;
import com.luckyseven.backend.global.error.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OAuthService {
    private static final String BEARER_TYPE = "Bearer";

    private final InMemoryClientRegistrationRepository inMemoryRepository;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    @Transactional
    public MemberResponseDto login(String code) {
        String providerName = "kakao";
        ClientRegistration provider = inMemoryRepository.findByRegistrationId(providerName);
        OauthTokenResponse tokenResponse = getToken(code, provider);
        MemberResponseDto memberResponseDto = getUserProfile(providerName, tokenResponse, provider);

        tokenProvider.generateToken(memberResponseDto.getEmail());

        return memberResponseDto;
    }

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

    private MultiValueMap<String, String> tokenRequest(String code, ClientRegistration provider) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("grant_type", "authorization_code");
        formData.add("redirect_uri", provider.getRedirectUri());
        formData.add("client_secret", provider.getClientSecret());
        formData.add("client_id", provider.getClientId());
        return formData;
    }

    private MemberResponseDto getUserProfile(String providerName, OauthTokenResponse tokenResponse, ClientRegistration provider) {
        Map<String, Object> userAttributes = getUserAttributes(provider, tokenResponse);
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(userAttributes);

        String provide = kakaoUserInfo.getProvider();
        String providerId = kakaoUserInfo.getProviderId();
        String nickName = kakaoUserInfo.getNickName();
        String email = kakaoUserInfo.getEmail();
        String imageUrl = kakaoUserInfo.getImageUrl();

        Optional<Member> checkMember = memberRepository.findByEmail(email);
        if (checkMember.isPresent()) {
            throw new BadRequestException(ErrorCode.MEMBER_ALREADY_EXIST);
        }
        Member member = Member.builder()
                .nickname(nickName)
                .email(email)
                .profileImage(imageUrl)
                .build();
        memberRepository.save(member);

        return MemberResponseDto.of(member);
    }

    private Map<String, Object> getUserAttributes(ClientRegistration provider, OauthTokenResponse tokenResponse) {
        return WebClient.create()
                .get()
                .uri(provider.getProviderDetails().getUserInfoEndpoint().getUri())
                .headers(header -> header.setBearerAuth(tokenResponse.getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }
}
