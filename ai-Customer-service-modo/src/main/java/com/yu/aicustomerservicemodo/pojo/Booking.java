package com.yu.aicustomerservicemodo.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class Booking extends BasePojo {

    private Long userId;

    private String resNum;

    private LocalDateTime resDate;

    private String from_;

    private String to_;

    // 0 取消预定 1.预定成功
    private Integer status_;

    // 舱位等级 1.经济舱 2.头等舱 3.商务舱
    private Integer level_;
}
