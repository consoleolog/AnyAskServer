package com.consoleolog.anyaskapiserver.v1.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class MemberDto {

    private String mId;

    private String userId;

    private String userPwd;

    private String userEmail;

    private String displayName;

    private String useYn;

    private String socialYn;

    private LocalDateTime lastLoginAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public MemberDto toEntity(MemberDto memberDto) {
        return MemberDto.builder()
                .mId(memberDto.getMId())
                .userId(memberDto.getUserId())
                .userPwd(memberDto.getUserPwd())
                .displayName(memberDto.getDisplayName())
                .useYn(memberDto.getUseYn())
                .socialYn(memberDto.getSocialYn())
                .lastLoginAt(memberDto.getLastLoginAt())
                .createdAt(memberDto.getCreatedAt())
                .updatedAt(memberDto.getUpdatedAt())
                .build();
    }

}
