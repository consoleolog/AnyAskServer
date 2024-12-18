package com.consoleolog.anyaskapiserver.v1.service;

import com.consoleolog.anyaskapiserver.v1.model.dto.ChatRequest;
import com.consoleolog.anyaskapiserver.v1.model.dto.ChatRoomDto;
import com.consoleolog.anyaskapiserver.v1.model.entity.ChatRoom;

import java.util.List;

public interface ChatService {

    List<ChatRoom> getAllChatRooms(ChatRoomDto chatRoomDto);

    String getResponse(ChatRequest chatRequest);

    void saveAiResponse(ChatRequest chatRequest);

}
