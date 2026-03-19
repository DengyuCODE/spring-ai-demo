package com.yu.aicustomerservicemodo.Mapper;

import com.yu.aicustomerservicemodo.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    User selectById(@Param("id") Long id);

    String selectUserNameById(@Param("id") Long id);
}
