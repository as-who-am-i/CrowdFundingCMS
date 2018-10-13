package com.dao;

import com.entity.UPP;

import java.util.List;

public interface UPPMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UPP record);

    int insertSelective(UPP record);

    UPP selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UPP record);

    int updateByPrimaryKey(UPP record);

    List<UPP> selectUserByPsId(Integer psId);
}