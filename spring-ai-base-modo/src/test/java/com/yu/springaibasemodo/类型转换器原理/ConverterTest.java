package com.yu.springaibasemodo.类型转换器原理;

import com.yu.springaibasemodo.pojo.ActorsFilms;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.boot.test.context.SpringBootTest;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@SpringBootTest
public class ConverterTest {

    @Autowired
    DashScopeChatModel chatModel;

    @Test
    public void testLowEntityOut() {
        BeanOutputConverter<ActorsFilms> beanOutputConverter =
                new BeanOutputConverter<>(ActorsFilms.class);

        //转换器输出format会得到一段提示词，意思是响应必须按照这个格式输出，输出结果会得到一个json
        String format = beanOutputConverter.getFormat();

        String actor = "周星驰";

        String template = """
        提供5部{actor}导演的电影，给出电影的真实名称.
        {format}
        """;

        PromptTemplate promptTemplate = PromptTemplate.builder().template(template).variables(Map.of("actor", actor, "format", format)).build();
        ChatResponse response = chatModel.call(
                promptTemplate.create()
        );

        //getText()得到的是一个json数据,由json数据转换成ActorsFilms
        ActorsFilms actorsFilms = beanOutputConverter.convert(response.getResult().getOutput().getText());
        System.out.println(actorsFilms);
    }
}
