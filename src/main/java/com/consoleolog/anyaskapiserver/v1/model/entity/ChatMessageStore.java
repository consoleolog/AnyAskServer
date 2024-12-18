package com.consoleolog.anyaskapiserver.v1.model.entity;

import com.consoleolog.anyaskapiserver.v1.model.dto.ChatMessageStoreDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@ToString
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChatMessageStore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cId;

    private String type;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Long roomId;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public ChatMessageStoreDto toDto(ChatMessageStore chatMessageStore) {
        return ChatMessageStoreDto
                .builder()
                .cId(chatMessageStore.getCId())
                .type(chatMessageStore.getType())
                .content(chatMessageStore.getContent())
                .roomId(chatMessageStore.getRoomId())
                .createdAt(chatMessageStore.getCreatedAt())
                .build();
    }

}
