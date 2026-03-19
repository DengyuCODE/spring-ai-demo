package com.yu.aicustomerservicemodo.config;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.memory.redis.JedisRedisChatMemoryRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    /**
     * chatMemory， redis存储
     */

    @Value("${spring.ai.memory.redis.host}")
    private String redisHost;
    @Value("${spring.ai.memory.redis.port}")
    private int redisPort;
    @Value("${spring.ai.memory.redis.password}")
    private String redisPassword;
    @Value("${spring.ai.memory.redis.timeout}")
    private int redisTimeout;

    @Bean
    public JedisRedisChatMemoryRepository redisChatMemoryRepository(){
        return JedisRedisChatMemoryRepository.builder()
                .host(redisHost)
                .port(redisPort)
                .password(redisPassword)
                .timeout(redisTimeout)
                .build();
    }

    @Bean(name = "redisChatMemory")
    public ChatMemory redisChatMemory(JedisRedisChatMemoryRepository redisChatMemoryRepository){
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(redisChatMemoryRepository)
                .maxMessages(20)
                .build();
    }

    /**
     * 基于内存的ChatMemory
     */
    @Bean
    public ChatMemoryRepository chatMemoryRepository(){
        return new InMemoryChatMemoryRepository();
    }

    @Bean(name = "inChatMemory")
    public ChatMemory inChatMemory(ChatMemoryRepository chatMemoryRepository){
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .maxMessages(20)
                .build();
    }

}
