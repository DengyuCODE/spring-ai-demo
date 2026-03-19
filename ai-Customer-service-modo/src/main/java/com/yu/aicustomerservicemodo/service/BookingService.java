package com.yu.aicustomerservicemodo.service;

import com.yu.aicustomerservicemodo.DTO.BookingDto;
import com.yu.aicustomerservicemodo.Mapper.BookingMapper;
import com.yu.aicustomerservicemodo.Mapper.UserMapper;
import com.yu.aicustomerservicemodo.pojo.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingMapper resInfoMapper;

    @Autowired
    private UserMapper userMapper;

    public List<BookingDto> queryBookingList(){
        List<Booking> resInfoList = resInfoMapper.selectAll();
        List<BookingDto> bookingDtoList = new ArrayList<>();
        for (Booking resInfo : resInfoList) {
            BookingDto bookingDto = new BookingDto();
            bookingDto.setResInfo(resInfo);
            String userName = userMapper.selectUserNameById(resInfo.getUserId());
            bookingDto.setUserName(userName);
            bookingDtoList.add(bookingDto);
        }
        return bookingDtoList;
    }

    public String addOrUpdateBooking(Booking resInfo){
        if (resInfo.getId_() == null) {
            resInfo.setCreateTime(LocalDateTime.now());
            resInfo.setUpdateTime(LocalDateTime.now());
            resInfo.setEnable_(1);
            resInfo.setStatus_(1);
            resInfoMapper.insert(resInfo);
            return "新增预定成功";
        } else {
            resInfo.setUpdateTime(LocalDateTime.now());
            resInfoMapper.update(resInfo);
            return "修改预定成功";
        }
    }

    public String cancelBooking(Long id){
        resInfoMapper.updateStatusById(id, 0);
        return "取消预定成功";
    }

    // 根据resNum和userName取消预定
    public String cancelBooking(String resNum, String userName){
        Booking resInfo = resInfoMapper.selectByResNum(resNum);
        if (resInfo == null) {
            return "未找到预定号为 " + resNum + " 的预定信息";
        }
        
        String actualUserName = userMapper.selectUserNameById(resInfo.getUserId());
        if (!userName.equals(actualUserName)) {
            return "用户名不匹配，无法取消预定";
        }
        
        if (resInfo.getStatus_() == 0) {
            return "该预定已取消";
        }
        
        resInfoMapper.updateStatusById(resInfo.getId_(), 0);
        return "取消预定成功";
    }
}
