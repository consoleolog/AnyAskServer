package com.consoleolog.anyaskapiserver.v1.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChatRequest {

    private String content;

    private MultipartFile file;

    private String userId;

    public static ChatRequest create(String content, MultipartFile file, String userId) {
        return new ChatRequest(content, file, userId);
    }

}
