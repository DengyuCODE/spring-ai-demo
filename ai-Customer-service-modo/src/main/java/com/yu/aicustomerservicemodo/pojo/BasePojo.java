package com.yu.aicustomerservicemodo.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BasePojo {

    private Long id_;

    private String remark_;

    private Integer enable_;

    private Long createBy;

    private LocalDateTime createTime;

    private Long updateBy;

    private LocalDateTime updateTime;
}
