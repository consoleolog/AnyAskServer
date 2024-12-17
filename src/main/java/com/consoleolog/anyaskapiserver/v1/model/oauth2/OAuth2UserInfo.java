package com.consoleolog.anyaskapiserver.v1.model.oauth2;

public interface OAuth2UserInfo {

    String getSocialCode();

    String getUserId();

    String getUserEmail();

    String getNickname();


}
