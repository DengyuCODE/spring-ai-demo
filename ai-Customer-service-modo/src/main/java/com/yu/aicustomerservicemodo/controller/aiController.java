package com.yu.aicustomerservicemodo.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@RestController
@RequestMapping("/api/v1/ai")
public class aiController {

    @Autowired
    private ChatClient customerServiceChatClient;

    @GetMapping(value = "/generateStreamAsString", produces = "text/stream;charset=UTF8")
    public Flux<String> generateStreamAsString(@RequestParam(value = "message", defaultValue = "讲个笑话") String message){
        //Sinks.Many<String> skin = Sinks.many().unicast().onBackpressureBuffer();

        Flux<String> content = customerServiceChatClient.prompt()
                .user(message)
                .stream()
                .content();
        return content;
    }
}
