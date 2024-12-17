package com.consoleolog.anyaskapiserver.v1.service.impl;

import com.consoleolog.anyaskapiserver.v1.repository.ChatRoomRepository;
import com.consoleolog.anyaskapiserver.v1.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatServiceImpl implements ChatService {

    private final ChatRoomRepository chatRoomRepository;






}
