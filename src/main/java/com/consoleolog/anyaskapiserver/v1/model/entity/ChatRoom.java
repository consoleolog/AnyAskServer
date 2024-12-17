package com.consoleolog.anyaskapiserver.v1.model.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    private ChatRoom(String roomName, String roomUserId){
        this.roomName = roomName;
        this.roomUserId = roomUserId;
    }

    public static ChatRoom create(String roomName, String roomUserId){
        return new ChatRoom(roomName,roomUserId);
    }

}
