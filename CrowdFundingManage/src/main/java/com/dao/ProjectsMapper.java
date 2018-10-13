package com.dao;

import com.entity.Projects;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectsMapper {
    int deleteByPrimaryKey(Integer psId);

    int insert(Projects record);

    int insertSelective(Projects record);

    Projects selectByPrimaryKey(Integer psId);

    int updateByPrimaryKeySelective(Projects record);

    int updateByPrimaryKey(Projects record);

    List<Projects> selectByPsType(int page);
}