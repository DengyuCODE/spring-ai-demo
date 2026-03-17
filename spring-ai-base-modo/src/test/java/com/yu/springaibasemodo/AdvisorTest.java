package com.yu.springaibasemodo;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AdvisorTest {

    @Autowired
    DashScopeChatModel chatModel;

    ChatClient chatClient;

    @BeforeEach
    public void init(){
        chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor()) //设置默认拦截器为打印日志拦截器
                .build();


    }
    //输出信息会包含日志信息
    // 日志中就记录了
    //request:  请求的日志信息
    //response: 响应的信息
    @Test
    public void testAdvisorTest(){
        String content = chatClient.prompt()
                .user("Hello")
                .call()
                .content();
        System.out.println(content);
    }


}
