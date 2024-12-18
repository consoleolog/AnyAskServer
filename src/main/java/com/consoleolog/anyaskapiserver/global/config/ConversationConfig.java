package com.consoleolog.anyaskapiserver.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class ConversationConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
        ChatMemory chatMemory = new InMemoryChatMemory();
        return chatClientBuilder.defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory)).build();
    }

    @Bean
    public ChatMemory chatMemory(){
        return new InMemoryChatMemory();
    }



}
