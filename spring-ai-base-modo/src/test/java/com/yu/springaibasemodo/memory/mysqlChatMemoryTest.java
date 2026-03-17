package com.yu.springaibasemodo.memory;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.yu.advisor.ReReadingAdvisor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootTest
public class mysqlChatMemoryTest {

    @Autowired
    private DashScopeChatModel chatModel;

    @Autowired
    private ChatMemory chatMemory;

    private ChatClient chatClient;

    @BeforeEach
    private void initChatClient(){
        chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(new ReReadingAdvisor(),
                        PromptChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    @Test
    private void MysqlChatMemoryTest(){
        String content = chatClient.prompt()
                .user("你好，我叫**！")
                .advisors(new ReReadingAdvisor())
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID,"1"))
                .call()
                .content();
        System.out.println(content);
        System.out.println("--------------------------------------------------------------------------");

        content = chatClient.prompt()
                .user("我叫什么 ？")
                .advisors(new ReReadingAdvisor())
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID,"1"))
                .call()
                .content();
        System.out.println(content);
    }

    //定义一个chatMemory，使用Jdbc
    @Configuration
    public static class ChatMemoryConfig {

        @Bean
        ChatMemory chatMemory(JdbcChatMemoryRepository chatMemoryRepository) {
            return MessageWindowChatMemory
                    .builder()
                    .maxMessages(1)
                    .chatMemoryRepository(chatMemoryRepository).build();
        }

    }
}
