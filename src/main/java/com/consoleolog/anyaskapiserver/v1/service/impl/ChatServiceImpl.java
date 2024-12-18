package com.consoleolog.anyaskapiserver.v1.service.impl;

import com.consoleolog.anyaskapiserver.v1.model.dto.ChatMessageStoreDto;
import com.consoleolog.anyaskapiserver.v1.model.dto.ChatRequest;
import com.consoleolog.anyaskapiserver.v1.model.dto.ChatRoomDto;
import com.consoleolog.anyaskapiserver.v1.model.entity.ChatMessageStore;
import com.consoleolog.anyaskapiserver.v1.model.entity.ChatRoom;
import com.consoleolog.anyaskapiserver.v1.repository.ChatMessageStoreRepository;
import com.consoleolog.anyaskapiserver.v1.repository.ChatRoomRepository;
import com.consoleolog.anyaskapiserver.v1.service.ChatService;
import com.consoleolog.anyaskapiserver.v1.util.SerperSearchTool;
import com.consoleolog.anyaskapiserver.v1.util.WebBaseLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatServiceImpl implements ChatService {

    private final SerperSearchTool searchTool;
    private final WebBaseLoader webBaseLoader;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageStoreRepository chatMessageStoreRepository;
    private final ChatModel chatModel;
    private final VectorStore vectorStore;
    private final ChatMemory chatMemory;

    @Override
    public List<ChatRoom> getAllChatRooms(ChatRoomDto chatRoomDto) {
        return chatRoomRepository.findAllByRoomUserId(chatRoomDto.getRoomUserId());
    }

    @Override
    public String getResponse(ChatRequest chatRequest){
        saveHumanMessage(chatRequest);
        return chatModel.call(chatRequest.getContent());
    }

    private void saveHumanMessage(ChatRequest chatRequest){
        Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findByRoomNameAndRoomUserId(chatRequest.getContent(), chatRequest.getUserId());
        ChatRoom chatRoom;
        if (chatRoomOptional.isPresent()){
            chatRoom = chatRoomOptional.get();
        } else {
            ChatRoomDto chatRoomDto = ChatRoomDto.builder()
                    .roomName(chatRequest.getContent())
                    .roomUserId(chatRequest.getUserId())
                    .build();
            chatRoom = chatRoomRepository.save(chatRoomDto.toEntity(chatRoomDto));
        }

        ChatMessageStoreDto humanMessageDto = ChatMessageStoreDto.builder()
                .type(MessageType.ASSISTANT.getValue())
                .content(chatRequest.getContent())
                .roomId(chatRoom.getCrId())
                .build();
        ChatMessageStore humanMessage = humanMessageDto.toEntity(humanMessageDto);
        chatMessageStoreRepository.save(humanMessage);
        UserMessage userMessage = new UserMessage(chatRequest.getContent());
        chatMemory.add(chatRequest.getUserId(), userMessage);
    }
    public void saveAiMessage(ChatRequest chatRequest){
        Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findByRoomNameAndRoomUserId(chatRequest.getContent(), chatRequest.getUserId());


    }

    @Override
    public void saveAiResponse(ChatRequest chatRequest) {

        Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findByRoomNameAndRoomUserId(chatRequest.getContent(), chatRequest.getUserId());

        ChatRoom chatRoom;
        if (chatRoomOptional.isPresent()){
            chatRoom = chatRoomOptional.get();

            ChatMessageStoreDto aiMessageDto = ChatMessageStoreDto.builder()
                    .type(MessageType.USER.getValue())
                    .content(chatRequest.getContent())
                    .roomId(chatRoom.getCrId())
                    .build();
            ChatMessageStore aiMessage = aiMessageDto.toEntity(aiMessageDto);
            chatMessageStoreRepository.save(aiMessage);

        }
    }

    public String responsePro(ChatRequest chatRequest) throws IOException {
        Map<String, Object> result = searchTool.search(chatRequest.getContent());
        List<String> links = searchTool.getLinks(result);

        for (String link : links){
            List<Document> documents = webBaseLoader.load(link);
            vectorStore.add(documents);
        }

        ChatClient chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults()))
                .build();

        String response = chatClient.prompt()
                .user(chatRequest.getContent())
                .call()
                .content();

        return response;
    }

    public void callChatClient(){

        ChatMemory chatMemory = new InMemoryChatMemory();

        ChatClient chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory))
                .build();

    }

}
