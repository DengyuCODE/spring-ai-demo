package com.yu.aicustomerservicemodo.DTO;

import com.yu.aicustomerservicemodo.pojo.Booking;
import lombok.Data;

@Data
public class BookingDto {

    // ResInfo实体类信息
    private Booking resInfo;

    //resInfo中userId对应的User实体类中的id_，取user中的userName
    private String userName;
}
