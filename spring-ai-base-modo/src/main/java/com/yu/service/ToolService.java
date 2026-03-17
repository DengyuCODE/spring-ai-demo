package com.yu.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class ToolService {

    @Tool(description = "获取城市今天的天气情况")
    public String getWeatherTool(@ToolParam(description = "经度") String longitude,
                               @ToolParam(description = "维度") String dimension){
        // toDo:获取天气

        return "晴天";
    }
}
