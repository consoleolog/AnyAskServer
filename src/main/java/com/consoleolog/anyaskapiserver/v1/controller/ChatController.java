package com.consoleolog.anyaskapiserver.v1.controller;


import com.consoleolog.anyaskapiserver.v1.model.dto.ChatRequest;
import com.consoleolog.anyaskapiserver.v1.model.dto.ChatRoomDto;
import com.consoleolog.anyaskapiserver.v1.model.dto.UserPrincipal;
import com.consoleolog.anyaskapiserver.v1.model.entity.ChatRoom;
import com.consoleolog.anyaskapiserver.v1.service.ChatService;
import com.consoleolog.anyaskapiserver.v1.service.RagService;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Objects;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
@RestController
public class ChatController {

    private final RagService ragService;

    private final ChatClient chatClient;

    private final ChatService chatService;

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> getUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        log.debug(auth.getName());

        UserPrincipal user = (UserPrincipal) auth.getPrincipal();

        log.debug(user.getUserId());

        return ResponseEntity.ok(Map.of("user", user));
    }


    @PostMapping("/")
    public Map<String, Object> chatRooms(@RequestBody Map<String, Object> data){
        log.debug(data.toString());

        ChatRoomDto chatRoomDto = new ChatRoomDto();
        chatRoomDto.setRoomUserId(data.get("roomUserId").toString());

        log.debug(chatRoomDto.toString());
        List<ChatRoom> chatRooms = chatService.getAllChatRooms(chatRoomDto);
        return Map.of("chatRooms", chatRooms);
    }

    @PostMapping("/save-ai-msg")
    public void aiResponse(@RequestBody ChatRequest chatRequest){

        chatService.saveAiResponse(chatRequest);

    }

    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoom>> getAllChatRooms(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        log.debug(auth.getName());

        UserPrincipal user = (UserPrincipal) auth.getPrincipal();

        log.debug(user.toString());
        ChatRoomDto chatRoomDto = ChatRoomDto.builder()
                .roomUserId(user.getUserId())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(chatService.getAllChatRooms(chatRoomDto));
    }

    @PostMapping("/stream")
    public ResponseEntity<?> chatWithStream(
            @RequestPart("content") @Nullable String content,
            @RequestPart("file") @Nullable MultipartFile file,
            @RequestPart("userId") @Nullable String userId){

        ChatRequest chatRequestDto = ChatRequest.create(content, file, userId);

        String result = chatService.getResponse(chatRequestDto);


        return ResponseEntity.status(HttpStatus.OK).body(Map.of("msg", result));
    }


}
