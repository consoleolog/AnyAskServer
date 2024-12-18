package com.consoleolog.anyaskapiserver.v1.model.entity;


import com.consoleolog.anyaskapiserver.v1.model.dto.ChatRoomDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@ToString
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long crId;

    private String roomName;

    private String roomUserId;

    @Builder.Default
    private String useYn = "Y";

    @CreationTimestamp
    private LocalDateTime createdAt;

    private ChatRoomDto toDto(ChatRoom chatRoom){
        return ChatRoomDto.builder()
                .crId(chatRoom.getCrId())
                .roomName(chatRoom.getRoomName())
                .roomUserId(chatRoom.getRoomUserId())
                .useYn(chatRoom.getUseYn())
                .createdAt(chatRoom.getCreatedAt())
                .build();
    }

    private ChatRoom(String roomName, String roomUserId){
        this.roomName = roomName;
        this.roomUserId = roomUserId;
    }

    public static ChatRoom create(String roomName, String roomUserId){
        return new ChatRoom(roomName, roomUserId);
    }

}
