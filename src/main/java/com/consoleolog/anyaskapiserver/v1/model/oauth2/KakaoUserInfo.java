package com.consoleolog.anyaskapiserver.v1.model.oauth2;

import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.Map;

@ToString
@AllArgsConstructor
public class KakaoUserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;


    @Override
    public String getSocialCode() {
        return "KAKAO";
    }

    @Override
    public String getUserId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getUserEmail() {

        @SuppressWarnings("unchecked")
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");

        return account.get("email").toString();
    }

    @Override
    public String getNickname() {

        @SuppressWarnings("unchecked")
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        @SuppressWarnings("unchecked")
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        return profile.get("nickname").toString();
    }
}