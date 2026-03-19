package com.yu.aicustomerservicemodo.Mapper;

import com.yu.aicustomerservicemodo.pojo.Booking;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BookingMapper {

    List<Booking> selectAll();

    int insert(Booking resInfo);

    int update(Booking resInfo);

    int updateStatusById(@Param("id") Long id, @Param("status") Integer status);

    Booking selectById(Long id);

    Booking selectByResNum(@Param("resNum") String resNum);
}
