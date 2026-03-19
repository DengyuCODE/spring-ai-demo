package com.yu.aicustomerservicemodo.controller;

import com.yu.aicustomerservicemodo.DTO.BookingDto;
import com.yu.aicustomerservicemodo.DTO.Result;
import com.yu.aicustomerservicemodo.pojo.Booking;
import com.yu.aicustomerservicemodo.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
public class bookingController {

    @Autowired
    private BookingService bookingService;

    // 获取预订列表
    @GetMapping("")
    public Result<List<BookingDto>> queryList(){
        List<BookingDto> bookingList = bookingService.queryBookingList();
        return Result.success(bookingList);
    }

    // 修改预订
    @PostMapping("")
    public Result<String> addOrUpdateBooking(@RequestBody Booking resInfo){
        String result = bookingService.addOrUpdateBooking(resInfo);
        return Result.success(result);
    }

    // 取消预定
    @DeleteMapping("")
    public Result<String> cancelBooking(@RequestBody Long id){
        String result = bookingService.cancelBooking(id);
        return Result.success(result);
    }


}
