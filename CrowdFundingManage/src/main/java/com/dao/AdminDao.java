package com.dao;

import java.util.List;

/**
 * @Prigram: com.dao
 * @Description: TODO
 * @Author: DongFang
 * @CreaeteTime: 2018-09-19 12:09
 */
public interface AdminDao {
    List<String> findRolesById(int id);
}
