package com.consoleolog.anyaskapiserver.repository;

import com.consoleolog.anyaskapiserver.v1.model.dto.ChatRoomDto;
import com.consoleolog.anyaskapiserver.v1.model.entity.ChatRoom;
import com.consoleolog.anyaskapiserver.v1.repository.ChatRoomRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class ChatRoomRepositoryTest {


    private final ChatRoomRepository chatRoomRepository;

    @Autowired
    public ChatRoomRepositoryTest(
            ChatRoomRepository chatRoomRepository
    ) {
        this.chatRoomRepository = chatRoomRepository;
    }

    @Test
    public void testFindAllByRoomUserId(){
        String mId = "674ca196-5c42-4f87-a3c7-f867d1d0079d";

        var result = chatRoomRepository.findAllByRoomUserId(mId);

        System.out.println(result);


    }

    @Test
    public void testCreateChatRoom(){
        String mId = "674ca196-5c42-4f87-a3c7-f867d1d0079d";
        String roomName = "Hello World";

        ChatRoom chatRoom = ChatRoom.create(roomName, mId);

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        System.out.println(savedChatRoom);

    }

    @Test
    public void testFindByRoomNameAndRoomUserId(){
        String mId = "674ca196-5c42-4f87-a3c7-f867d1d0079d";
        String roomName = "Hello World";

        Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findByRoomNameAndRoomUserId(roomName, mId);

        if (chatRoomOptional.isPresent()){
            ChatRoom chatRoom = chatRoomOptional.get();
            System.out.println(chatRoom);
        }

    }



}
