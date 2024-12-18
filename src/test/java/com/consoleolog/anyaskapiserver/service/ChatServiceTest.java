package com.consoleolog.anyaskapiserver.service;

import com.consoleolog.anyaskapiserver.v1.model.dto.ChatMessageStoreDto;
import com.consoleolog.anyaskapiserver.v1.model.dto.ChatRoomDto;
import com.consoleolog.anyaskapiserver.v1.model.entity.ChatMessageStore;
import com.consoleolog.anyaskapiserver.v1.model.entity.ChatRoom;
import com.consoleolog.anyaskapiserver.v1.repository.ChatMessageStoreRepository;
import com.consoleolog.anyaskapiserver.v1.repository.ChatRoomRepository;
import com.consoleolog.anyaskapiserver.v1.service.ChatService;
import com.consoleolog.anyaskapiserver.v1.util.WebBaseLoader;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class ChatServiceTest {

    private final ChatService chatService;

    private final ChatRoomRepository chatRoomRepository;

    private final ChatMessageStoreRepository chatMessageStoreRepository;

    private final ChatModel chatModel;

    private final VectorStore vectorStore;

    private WebBaseLoader webBaseLoader;

    @Autowired
    public ChatServiceTest(
            ChatService chatService,
            ChatRoomRepository chatRoomRepository,
            ChatMessageStoreRepository chatMessageStoreRepository,
            ChatModel chatModel,
            VectorStore vectorStore,
            WebBaseLoader webBaseLoader
    ) {
        this.chatService = chatService;
        this.chatRoomRepository = chatRoomRepository;
        this.chatMessageStoreRepository = chatMessageStoreRepository;
        this.chatModel = chatModel;
        this.vectorStore = vectorStore;
        this.webBaseLoader = webBaseLoader;
    }

    @Test
    public void testRag() throws IOException {
//        String url = "https://docs.spring.io/spring-ai/reference/api/function-callback.html";
//
//        List<Document> documents = webBaseLoader.load(url);
//
//        vectorStore.add(documents);

        var chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults())).build();

        String response = chatClient.prompt()
                .user("Spring AI 에서 Callback 으로 응답이 완료되면 DB에 저장하는 코드를 알려줘")
                .call()
                .content();


        System.out.println(response);



    }

    @Test
    public void testInitialize(){
        String content = "";
        String mId = "";
        MultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", content.getBytes());

        Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findByRoomNameAndRoomUserId(content, mId);

        ChatRoom chatRoom;
        if (chatRoomOptional.isEmpty()) {
            ChatRoomDto chatRoomDto = ChatRoomDto
                    .builder()
                    .roomName(content)
                    .roomUserId(mId)
                    .build();
            chatRoom = chatRoomRepository.save(chatRoomDto.toEntity(chatRoomDto));
        } else {
            chatRoom = chatRoomOptional.get();
        }

        ChatMessageStoreDto humanMessageDto = ChatMessageStoreDto
                .builder()
                .type("human")
                .content(content)
                .roomId(chatRoom.getCrId())
                .build();

        ChatMessageStore humanMessage = humanMessageDto.toEntity(humanMessageDto);
        ChatMessageStore savedHumanMessage = chatMessageStoreRepository.save(humanMessage);

        /* AI 응답 */
        String response = "";

        ChatMessageStoreDto aiMessageDto = ChatMessageStoreDto
                .builder()
                .type("ai")
                .content(response)
                .roomId(chatRoom.getCrId())
                .build();

        ChatMessageStore aiMessage = aiMessageDto.toEntity(aiMessageDto);
        ChatMessageStore savedAIMessage = chatMessageStoreRepository.save(aiMessage);


    }

    @Test
    public void testChatMemory(){

        ChatMemory chatMemory = new InMemoryChatMemory();

        ChatClient chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        new SimpleLoggerAdvisor()
                )
                .build();

        chatClient.prompt().user("")
                .advisors(a-> a.param("chat_memory_conversation_id", "chat_id")
                        .param("chat_memory_response_size", 100)
                );

        UserMessage userMessage = new UserMessage("");

        AssistantMessage assistantMessage = new AssistantMessage("");

    }

    @Test
    public void testPrompt(){

        var chatMemory = new InMemoryChatMemory();

        var chatClient = ChatClient.builder(chatModel)
                .defaultSystem("""
                너는 응답을 할 때 Streamlit 의 컴포넌트들을 통해서 대답할 수 있어 
                응답 중간 중간에 컴포넌트들을 사용하고 싶으면 응답에 ```streamlit
                                               st.title("text")
                                               ```
                를 사용해서 대답하면 돼                     
                - `st.title()`: 애플리케이션의 제목을 설정합니다.
                - `st.header()`: 섹션의 헤더를 설정합니다.
                - `st.text()`: 일반 텍스트를 표시합니다.
                - `st.markdown()`: 마크다운 형식의 텍스트를 표시합니다.
                - `st.button()`: 버튼을 생성합니다.
                - `st.text_input()`: 텍스트 입력 필드를 생성합니다.
                - `st.text_area()`: 멀티라인 텍스트 입력 필드를 생성합니다.
                - `st.selectbox()`: 드롭다운 선택 상자를 생성합니다.
                - `st.slider()`: 슬라이더를 생성합니다.
                - `st.file_uploader()`: 파일 업로드 컴포넌트를 생성합니다.
                - `st.image()`: 이미지를 표시합니다.
                - `st.line_chart()`: 선형 차트를 생성합니다.
                - `st.map()`: 지도를 표시합니다.
                
                ** example **
                
                human: "소설 주제 추천해줘"
                ai : "소설 주제를 추천해 드리겠습니다. 
                ```streamlit
                 st.title("오즈와 마법사")
                ```
                ```streamlit
                st.title("해리포터")
                ```
                등이 있습니다.
                """)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults())
                ).build();
        var result = chatClient.prompt().user("그래프 좀 추천해줘").call().content();
        System.out.println(result);
    }

    @Test
    public void testSaveOrUpdate(){
        String mId = "674ca196-5c42-4f87-a3c7-f867d1d0079d";
        String roomName = "Hello World";

        Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findByRoomNameAndRoomUserId(roomName, mId);

        /* 이미 채팅방이 존재해 */
        if (chatRoomOptional.isPresent()) {
            ChatRoom chatRoom = chatRoomOptional.get();
            Optional<ChatMessageStore> chatMessageStoreOptional = chatMessageStoreRepository.findByRoomId(chatRoom.getCrId());

            /* 채팅방이 있으면 안만들고 없으면 만들어야 되는거 아닐까 */
            if (chatMessageStoreOptional.isPresent()) {

            } else {

            }

        } else {

        }
    }


}
