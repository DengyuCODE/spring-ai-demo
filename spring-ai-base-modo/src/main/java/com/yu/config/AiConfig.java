package com.yu.config;

import com.alibaba.cloud.ai.autoconfigure.dashscope.DashScopeChatProperties;
import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.memory.redis.JedisRedisChatMemoryRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {


    //实现多个模型的转换使用
    /**
     * 定义三个模型
     */
//    @Bean
//    public ChatClient deepseekR1(DeepSeekChatProperties chatProperties) {
//
//        DeepSeekApi deepSeekApi = DeepSeekApi.builder()
//                .apiKey(System.getenv("DEEP_SEEK_KEY"))
//                .build();
//
//
//        DeepSeekChatModel deepSeekChatModel = DeepSeekChatModel.builder()
//                .deepSeekApi(deepSeekApi)
//                .defaultOptions(DeepSeekChatOptions.builder().model(DeepSeekApi.ChatModel.DEEPSEEK_REASONER).build())
//                .build();
//
//        return ChatClient.builder(deepSeekChatModel).build();
//    }

//    @Bean
//    public ChatClient deepseekV3() {
//
//        DeepSeekApi deepSeekApi = DeepSeekApi.builder()
//                .apiKey(System.getenv("DEEP_SEEK_KEY"))
//                .build();
//
//
//        DeepSeekChatModel deepSeekChatModel = DeepSeekChatModel.builder()
//                .deepSeekApi(deepSeekApi)
//                .defaultOptions(
//                        DeepSeekChatOptions.builder()
//                                .model(DeepSeekApi.ChatModel.DEEPSEEK_CHAT)
//                                .build()
//                )
//                .build();
//
//        return ChatClient.builder(deepSeekChatModel).build();
//    }


//    @Bean
//    public ChatClient ollama(@Autowired OllamaApi ollamaApi, @Autowired OllamaChatProperties options) {
//        OllamaChatModel ollamaChatModel = OllamaChatModel.builder()
//                .ollamaApi(ollamaApi)
//                .defaultOptions(OllamaOptions.builder().model(options.getModel()).build())
//
//        return ChatClient.builder(ollamaChatModel).build();
//    }

//    @Value("${spring.ai.dashscope.api-key:}")
//    private String apiKey;
//
//    @Bean
//    DashScopeApi dashScopeApi() {
//        return DashScopeApi.builder()
//                .apiKey(apiKey)
//                .build();
//    }
//
//    @Bean
//    DashScopeChatModel chatModel(DashScopeApi dashScopeApi) {
//        return DashScopeChatModel.builder()
//                .dashScopeApi(dashScopeApi)
//                .build();
//    }

    @Bean
    DashScopeChatOptions chatOptions() {
        return DashScopeChatOptions.builder().build();
    }

    @Bean
    ChatMemoryRepository chatMemoryRepository() {
        return new InMemoryChatMemoryRepository();
    }

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
                .password(redisPassword)
                .timeout(redisTimeout)
                .build();
    }

    @Bean
    ChatMemory chatMemory(JedisRedisChatMemoryRepository redisChatMemoryRepository) {
        return MessageWindowChatMemory.builder()
                .maxMessages(10)
                .chatMemoryRepository(redisChatMemoryRepository)
                .build();
    }

    @Bean
    public ChatClient planningChatClient(DashScopeChatModel chatModel,
                                         DashScopeChatOptions chatOptions,
                                         ChatMemory chatMemory) {
        chatOptions.setTemperature(0.7);

        return ChatClient.builder(chatModel)
                .defaultSystem("""
                            # 票务助手任务拆分规则
                            ## 1.要求
                            ### 1.1 根据用户内容识别任务
                            
                            ## 2. 任务
                            ### 2.1 JobType:退票(CANCEL) 要求用户提供姓名和预定号， 或者从对话中提取；
                            ### 2.2 JobType:查票(QUERY) 要求用户提供预定号， 或者从对话中提取；
                            ### 2.3 JobType:其他(OTHER)
                            """)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .defaultOptions(chatOptions)
                .build();
    }

    @Bean
    //@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public ChatClient botChatClient(DashScopeChatModel chatModel,
                                    DashScopeChatProperties options,
                                    ChatMemory chatMemory) {

        DashScopeChatOptions dashScopeChatOptions = DashScopeChatOptions.fromOptions(options.getOptions());
        dashScopeChatOptions.setTemperature(1.2);
        return ChatClient.builder(chatModel)
                .defaultSystem("""
                           你是XS航空智能客服代理， 请以友好的语气服务用户。
                            """)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .defaultOptions(dashScopeChatOptions)
                .build();
    }
}
