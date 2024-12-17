package com.consoleolog.anyaskapiserver.v1.model.entity;

import com.consoleolog.anyaskapiserver.v1.model.dto.MemberDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@ToString
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String mId;

    private String userId;

    private String userPwd;

    private String userEmail;

    private String displayName;

    @Builder.Default
    private String useYn = "Y";

    private String socialYn;

    private LocalDateTime lastLoginAt;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public MemberDto toDto(Member member) {
        return MemberDto.builder()
                .mId(member.getMId())
                .userId(member.getUserId())
                .userPwd(member.getUserPwd())
                .displayName(member.getDisplayName())
                .useYn(member.getUseYn())
                .socialYn(member.getSocialYn())
                .lastLoginAt(member.getLastLoginAt())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }

}
