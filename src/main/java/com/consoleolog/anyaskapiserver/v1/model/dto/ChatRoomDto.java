package com.consoleolog.anyaskapiserver.v1.model.dto;

import com.consoleolog.anyaskapiserver.v1.model.entity.ChatRoom;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class ChatRoomDto {

    private Long crId;

    private String roomName;

    private String roomUserId;

    private String useYn;

    private LocalDateTime createdAt;

    public ChatRoom toEntity(ChatRoomDto chatRoomDto) {
        return ChatRoom.builder()
                .crId(chatRoomDto.getCrId())
                .roomName(chatRoomDto.getRoomName())
                .roomUserId(chatRoomDto.getRoomUserId())
                .useYn(chatRoomDto.getUseYn())
                .createdAt(chatRoomDto.getCreatedAt())
                .build();
    }

}
