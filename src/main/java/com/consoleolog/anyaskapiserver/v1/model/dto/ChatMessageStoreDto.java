package com.consoleolog.anyaskapiserver.v1.model.dto;

import com.consoleolog.anyaskapiserver.v1.model.entity.ChatMessageStore;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class ChatMessageStoreDto {

    private Long cId;

    private String type;

    private String content;

    private Long roomId;

    private LocalDateTime createdAt;

    public ChatMessageStore toEntity(ChatMessageStoreDto chatMessageStoreDto) {
        return ChatMessageStore.builder()
                .cId(chatMessageStoreDto.getCId())
                .type(chatMessageStoreDto.getType())
                .content(chatMessageStoreDto.getContent())
                .roomId(chatMessageStoreDto.getRoomId())
                .build();
    }

}
