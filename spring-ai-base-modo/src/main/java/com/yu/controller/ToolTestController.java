package com.yu.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.yu.pojo.AiJob;
import com.yu.service.ToolService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@RestController
@RequestMapping("/ai/tools")
public class ToolTestController {
    @Autowired
    ChatClient planningChatClient;

    @Autowired
    ChatClient botChatClient;

    @Autowired
    DashScopeChatModel chatModel;

    @Autowired
    ChatMemory chatMemory;

    @Autowired
    ToolService toolService;

    @GetMapping(value = "/stream", produces = "text/stream;charset=UTF8")
    Flux<String> stream(@RequestParam(required = false, defaultValue = "你好") String message) {
        // 创建一个用于接收多条消息的 Sink
        Sinks.Many<String> sink = Sinks.many().unicast().onBackpressureBuffer();
        // 推送消息
        sink.tryEmitNext("正在计划任务...<br/>");


        new Thread(() -> {
            AiJob job = planningChatClient.prompt().user(message)
                    .call().entity(AiJob.class);

            switch (job.getJobType()){
                case CANCEL ->{
                    System.out.println(job);

                    if(job.getKeyInfos().size()==0){
                        sink.tryEmitNext("请输入姓名和订单号.");
                    }
                    else {
                        // todo.. 执行业务
                        sink.tryEmitNext("退票成功!");
                    }
                }
                case QUERY -> {
                    System.out.println(job);
                    // todo.. 执行业务
                    sink.tryEmitNext("查询预定信息：xxxx");
                }
                case OTHER -> {
                    Flux<String> content = botChatClient.prompt().user(message).stream().content();
                    content.doOnNext(sink::tryEmitNext) // 推送每条AI流内容
                            .doOnComplete(() -> sink.tryEmitComplete())
                            .subscribe();
                }
                default -> {
                    System.out.println(job);
                    sink.tryEmitNext("解析失败");
                }
            }
        }).start();

        return sink.asFlux();
    }

    @GetMapping(value = "/getWeather", produces = "text/stream;charset=UTF8")
    Flux<String> getWeather(@RequestParam(required = false, defaultValue = "北京今天的天气如何") String message){
        DashScopeChatOptions chatOptions = DashScopeChatOptions.builder().withTemperature(0.7).withEnableThinking(false).build();

        ChatClient chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(PromptChatMemoryAdvisor.builder(chatMemory).build())
                .defaultOptions(chatOptions)
                .defaultTools(toolService)
                .build();

        Flux<String> content = chatClient.prompt()
                .user(message)
                .stream()
                .content();

        //创建一个可以接收多条消息的sink
        /**
         * - Sinks.Many<String> : 创建一个可以发送多条消息的 Sink（接收器），类型为 String
         * - .unicast() : 单播模式，表示只有一个订阅者可以接收消息
         * - .onBackpressureBuffer() : 背压缓冲策略，当消费者处理速度跟不上生产者时，会将消息缓冲到队列中
         */
        Sinks.Many<String> sink = Sinks.many().unicast().onBackpressureBuffer();
        /**
         * - .doOnComplete() : 当流完成（AI 回复结束）时执行的操作
         * - sink.tryEmitComplete() : 发送完成信号，通知所有订阅者数据已发送完
         * - .subscribe() : 订阅流，触发实际的执行。没有订阅，流不会开始消费数据。
         */
        content.doOnNext(sink::tryEmitNext)
                .doOnComplete(() -> sink.tryEmitComplete())
                .subscribe();
        return sink.asFlux();
    }
}
