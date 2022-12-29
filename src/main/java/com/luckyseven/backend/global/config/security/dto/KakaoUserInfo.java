package com.luckyseven.backend.global.config.security.dto;

import java.util.Map;

public class KakaoUserInfo {
    private Map<String, Object> attributes;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getProviderId() {
        return String.valueOf(attributes.get("id"));
    }

    public String getProvider() {
        return "kakao";
    }

    public String getEmail() {
        return (String) getKakaoAccount().get("email");
    }

    public String getNickName() {
        return (String) getProfile().get("nickname");
    }

    public String getImageUrl() {
        return (String)getProfile().get("profile_image_url");
    }

    public Map<String, Object> getKakaoAccount(){
        return(Map<String, Object>) attributes.get("kakao_account");
    }

    public Map<String, Object> getProfile(){
        return (Map<String, Object>) getKakaoAccount().get("profile");
    }
}
