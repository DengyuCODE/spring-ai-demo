package com.yu.springaibasemodo.memory;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.memory.redis.JedisRedisChatMemoryRepository;
import com.yu.advisor.ReReadingAdvisor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootTest
public class redisChatMemoryTest {

    @Autowired
    private DashScopeChatModel chatModel;

    @Autowired
    private ChatMemory chatMemory;

    private ChatClient chatClient;

    @BeforeEach
    private void initChatClient(){
        chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(PromptChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }


    @Test
    private void RedisChatMemoryTest(){
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

    @Configuration
    public static class RedisMemoryConfig {

        @Value("${spring.ai.memory.redis.host}")
        private String redisHost;
        @Value("${spring.ai.memory.redis.port}")
        private int redisPort;
        @Value("${spring.ai.memory.redis.password}")
        private String redisPassword;
        @Value("${spring.ai.memory.redis.timeout}")
        private int redisTimeout;

        @Bean
        public JedisRedisChatMemoryRepository redisChatMemoryRepository() {
            return JedisRedisChatMemoryRepository.builder()
                    .host(redisHost)
                    .port(redisPort)
                    // 若没有设置密码则注释该项
//           .password(redisPassword)
                    .timeout(redisTimeout)
                    .build();
        }

        @Bean
        public ChatMemory chatMemory(JedisRedisChatMemoryRepository redisChatMemoryRepository){
            return MessageWindowChatMemory.builder()
                    .maxMessages(1)
                    .chatMemoryRepository(redisChatMemoryRepository)
                    .build();
        }
    }
}
