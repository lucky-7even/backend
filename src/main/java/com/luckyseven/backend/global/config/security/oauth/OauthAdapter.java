package com.luckyseven.backend.global.config.security.oauth;

import java.util.HashMap;
import java.util.Map;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OauthAdapter {

    // OauthProperties를 OauthProvider로 변환해준다.
    public static Map<String, OauthProvider> getOauthProviders(OauthProperties properties) {
        Map<String, OauthProvider> oauthProvider = new HashMap<>();

        properties.getUser().forEach((key, value) -> oauthProvider.put(key,
                new OauthProvider(value, properties.getProvider().get(key))));
        return oauthProvider;
    }
}
