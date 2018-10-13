package com.dao;

import com.entity.RealCheck;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RealCheckMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RealCheck record);

    int insertSelective(RealCheck record);

    RealCheck selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RealCheck record);

    int updateByPrimaryKey(RealCheck record);

    List<RealCheck> unRealCheckedList();

    int updateStatusById(@Param("status")int status,@Param("id")int id );

    RealCheck findById(int id);
}