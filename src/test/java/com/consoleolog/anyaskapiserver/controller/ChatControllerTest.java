package com.consoleolog.anyaskapiserver.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.support.DefaultConversionService;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class ChatControllerTest {

    private final ChatModel chatModel;

    private final VectorStore vectorStore;

    @Autowired
    public ChatControllerTest(
            ChatModel chatModel,
            VectorStore vectorStore
    ) {
        this.chatModel = chatModel;
        this.vectorStore = vectorStore;
    }

    @Test
    public void chatModelTest(){
        var r = chatModel.call("hi");
        System.out.println(r);
    }

    @Test
    public void testMapOutputConverter(){
        MapOutputConverter mapOutputConverter = new MapOutputConverter();

        String format = mapOutputConverter.getFormat();

        String template = """
                Provide me a List of {subject}
                {format}
                """;
        Prompt prompt = new PromptTemplate(
                template,
                Map.of("subject", "an array of numbers from 1 to 9 nunder they key name 'numbers'",
                        "format",format)
        ).create();

        Generation generation = chatModel.call(prompt).getResult();

        Map<String, Object> result = mapOutputConverter.convert(generation.getOutput().getContent());

        System.out.println(result);

    }

    @Test
    public void testListOutputConverter(){
        List<String> flavors = ChatClient.create(chatModel).prompt()
                .user(u -> u.text("List five {subject}")
                        .param("subject", "ice cream flavors")
                ).call()
                .entity(new ListOutputConverter(new DefaultConversionService()));

        System.out.println(flavors);
    }

    @Test
    public void testListOutputConverterV2(){
        ListOutputConverter listOutputConverter = new ListOutputConverter(new DefaultConversionService());

        String format = listOutputConverter.getFormat();

        String template = """
                List five {subject}
                {format}
                """;
        Prompt prompt = new PromptTemplate(
                template,
                Map.of(
                        "subject", "ice cream flavors",
                        "format", format
                )
        ).create();

        Generation generation = chatModel.call(prompt).getResult();

        List<String> list = listOutputConverter.convert(generation.getOutput().getContent());

        System.out.println(list);
    }

    @Test
    public void testJsonSchemaV1(){
        String jsonSchema = """
                {
                    "type": "object",
                    "properties": {
                        "steps": {
                            "type": "array",
                            "items": {
                                "type": "object",
                                "properties": {
                                    "explanation": { "type": "string" },
                                    "output": { "type": "string" }
                                },
                                "required": [ "explanation", "output" ],
                                "additionalProperties": false
                            }
                        },
                        "final_answer": { "type": "string" }
                    },
                    "required": ["steps", "final_answer"],
                    "additionalProperties": false
                }
                """;
        Prompt prompt = new Prompt(
                "how can I solve 8x + 7 = -23",
                OpenAiChatOptions.builder()
                        .withModel(OpenAiApi.ChatModel.GPT_4_O_MINI)
                        .withResponseFormat(new ResponseFormat(ResponseFormat.Type.JSON_SCHEMA, jsonSchema)).build()
        );

        ChatResponse response = chatModel.call(prompt);

        System.out.println(response.getResult());

    }

    @Test
    public void testBeanOutputConverterV1(){
        record MathReasoning(
                @JsonProperty(required = true, value = "steps") Steps steps,
                @JsonProperty(required = true, value = "final_answer") String finalAnswer
        ) {
            record Steps(
                    @JsonProperty(required = true, value = "items") Items[] items){

                record Items (
                        @JsonProperty(required = true, value = "explanation") String explanation,
                        @JsonProperty(required = true, value = "output") String output
                ) {}
            }
        }

        var outputConverter = new BeanOutputConverter<>(MathReasoning.class);

        var jsonSchema = outputConverter.getJsonSchema();

        Prompt prompt = new Prompt(
                "how can I solve 8x + 7 = -23",
                OpenAiChatOptions.builder()
                        .withModel(OpenAiApi.ChatModel.GPT_4_O_MINI)
                        .withResponseFormat(new ResponseFormat(ResponseFormat.Type.JSON_SCHEMA, jsonSchema)).build()
        );

        ChatResponse response = chatModel.call(prompt);

        String content = response.getResult().getOutput().getContent();

        MathReasoning mathReasoning = outputConverter.convert(content);

        System.out.println(mathReasoning);
    }




}
