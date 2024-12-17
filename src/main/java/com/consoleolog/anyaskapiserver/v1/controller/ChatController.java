package com.consoleolog.anyaskapiserver.v1.controller;


import com.consoleolog.anyaskapiserver.v1.service.RagService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/api/chat")
@RestController
public class ChatController {

    private final RagService ragService;

    private final ChatClient chatClient;

    @PostMapping("/")
    public ChatClient.StreamResponseSpec simpleChat(
            @RequestPart("content") @Nullable String content,
            @RequestPart("file") @Nullable MultipartFile file) {

        log.debug(content);

        return chatClient.prompt().user(Objects.requireNonNull(content)).stream();
    }

    @PostMapping("/stream")
    public Flux<ChatResponse> chatWithStream(@RequestPart("content") @Nullable String content,
                                             @RequestPart("file") @Nullable MultipartFile file){

        return chatClient.prompt()
                .user(Objects.requireNonNull(content))
                .stream()
                .chatResponse();
    }

    @PostMapping("/sse")
    public SseEmitter seeChat(
            @RequestPart("content") String content,
            @RequestPart("file") @Nullable MultipartFile file) {


        SseEmitter emitter = new SseEmitter();

        // 비동기 처리로 이벤트를 전송
        new Thread(() -> {
            try {
                ragService.createVectorStore(file);
                emitter.send(Map.of("message", "success"));
                emitter.complete(); // 전송 완료 후 emitter를 종료
            } catch (IOException e) {
                log.warn("UnstructuredLoader File Error : {} ", e.getMessage());
                try {
                    emitter.send(Map.of("message", e.getMessage()));
                } catch (IOException ex) {
                    emitter.completeWithError(ex); // 오류 처리
                }
            }
        }).start();

        return emitter;
    }
}
