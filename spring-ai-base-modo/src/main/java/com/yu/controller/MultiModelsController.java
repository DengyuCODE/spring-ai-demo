package com.yu.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MultiModelsController {

    @Autowired
    private Map<String, ChatClient> chatClientMap;

    @GetMapping("/chat")
    String generation(@RequestParam String message,
                      @RequestParam String model) {
        ChatClient chatClient = chatClientMap.get(model);
        String content = chatClient.prompt().user(message).call().content();
        return content;
    }
}