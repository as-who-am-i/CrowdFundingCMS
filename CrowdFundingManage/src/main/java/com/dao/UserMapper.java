package com.dao;

import com.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectByUsernameAndPassword(@Param("phone")String phone, @Param("password")String password);

    int updateStatusByPhone(String phone);

    String findEmailByPhone(String phone);

    User findEmailAndPasswordByUser(@Param("email") String email, @Param("password") String password);
}