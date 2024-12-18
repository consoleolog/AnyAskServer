package com.consoleolog.anyaskapiserver.v1.model.dto;

import com.consoleolog.anyaskapiserver.v1.model.entity.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDateTime;
import java.util.*;

@ToString
@Getter
public class UserPrincipal implements OAuth2User, UserDetails {

    private final String mId;
    private final String userId;
    private final String userEmail;
    private final String displayName;
    private final String useYn;
    private final String socialYn;
    private final LocalDateTime lastLoginAt;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final List<GrantedAuthority> authorities;
    @Setter
    private Map<String, Object> attributes;

    private UserPrincipal(String mId,
                          String userId,
                          String userEmail,
                          String displayName,
                          String userYn,
                          String socialYn,
                          LocalDateTime lastLoginAt,
                          LocalDateTime createdAt,
                          LocalDateTime updatedAt,
                          List<GrantedAuthority> authorities) {
        this.mId = mId;
        this.userId = userId;
        this.userEmail = userEmail;
        this.displayName = displayName;
        this.useYn = userYn;
        this.socialYn = socialYn;
        this.lastLoginAt = lastLoginAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.authorities = authorities;
    }

    public static UserPrincipal create(Member member){
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        return new UserPrincipal(
                member.getMId(),
                member.getUserId(),
                member.getUserEmail(),
                member.getDisplayName(),
                member.getUseYn(),
                member.getSocialYn(),
                member.getLastLoginAt(),
                member.getCreatedAt(),
                member.getUpdatedAt(),
                authorities
        );
    }

    public static UserPrincipal create(Member member, Map<String, Object> attributes){
        UserPrincipal userPrincipal = UserPrincipal.create(member);
        Objects.requireNonNull(userPrincipal).setAttributes(attributes);
        return userPrincipal;
    }


    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return displayName;
    }

}
