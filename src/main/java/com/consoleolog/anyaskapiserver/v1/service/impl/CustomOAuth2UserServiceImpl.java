package com.consoleolog.anyaskapiserver.v1.service.impl;

import com.consoleolog.anyaskapiserver.v1.model.dto.UserPrincipal;
import com.consoleolog.anyaskapiserver.v1.model.entity.Member;
import com.consoleolog.anyaskapiserver.v1.model.oauth2.KakaoUserInfo;
import com.consoleolog.anyaskapiserver.v1.model.oauth2.OAuth2UserInfo;
import com.consoleolog.anyaskapiserver.v1.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserServiceImpl extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.debug("--------------------- oauth service -----------------------");

        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuth2UserInfo userInfo = new KakaoUserInfo(oAuth2User.getAttributes());

        log.debug(userInfo.toString());

        Optional<Member> memberOptional = memberRepository.findByUserEmail(userInfo.getUserEmail());

        Member member;

        if (memberOptional.isPresent()) {
            member = memberOptional.get();
            return UserPrincipal.create(member, oAuth2User.getAttributes());
        } else {
            member = Member.builder()
                    .userId(userInfo.getUserId())
                    .userEmail(userInfo.getUserEmail())
                    .displayName(userInfo.getNickname())
                    .socialYn("Y")
                    .lastLoginAt(LocalDateTime.now())
                    .build();
            Member savedMember = memberRepository.save(member);
            return UserPrincipal.create(savedMember, oAuth2User.getAttributes());
        }
    }
}
