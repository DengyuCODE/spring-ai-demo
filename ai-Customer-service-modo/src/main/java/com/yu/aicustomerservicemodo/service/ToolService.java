package com.yu.aicustomerservicemodo.service;

import io.micrometer.common.util.StringUtils;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ToolService {

    @Autowired
    private BookingService bookingService;

    @Tool(description = "取消预定")
    public String cancel(@ToolParam(description = "预定号") String resNum,
                         @ToolParam(description = "预定用户姓名") String userName){

        String s = bookingService.cancelBooking(resNum, userName);
        if (StringUtils.isNotBlank(s)){
            return "取消预定失败:" + s;
        }
        return "取消预定成功";
    }
}
