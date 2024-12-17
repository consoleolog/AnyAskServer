package com.consoleolog.anyaskapiserver.global.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
//        OpenAiChatOptions.builder()/
//                .withModel(OpenAiApi.ChatModel.GPT_4_O_MINI)
//                .withResponseFormat(new ResponseFormat())

        return chatClientBuilder.build();
    }



}
