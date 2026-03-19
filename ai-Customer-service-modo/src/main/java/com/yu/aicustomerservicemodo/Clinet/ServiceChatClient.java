package com.yu.aicustomerservicemodo.Clinet;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.yu.aicustomerservicemodo.service.ToolService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ServiceChatClient {
    /**
     * 客服client
     */

    @Autowired
    ToolService toolService;

    @Bean
    public ChatClient customerServiceChatClient(DashScopeChatModel chatModel,
                                              ChatMemory redisChatMemory){
        return ChatClient.builder(chatModel)
                .defaultSystem("""
                        ##角色
                          您是"AI"航空公司的客户聊天支持代理。请以友好、乐于助人且愉快的方式来回复。
                          您正在通过在线聊天系统与客户互动。
                        ##要求
                          1.在涉及增删改（除了查询）function-call前，必须等用户回复"确认"后再调用tool。
                          2.请讲中文。
                                                
                        今天的日期是 {current_date}.
                        """)
                .defaultAdvisors(new SimpleLoggerAdvisor(), PromptChatMemoryAdvisor.builder(redisChatMemory).build())
                .defaultTools(toolService)
                .build();
    }
}
