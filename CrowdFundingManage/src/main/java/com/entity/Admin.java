package com.entity;

import lombok.Data;

import java.util.List;

/**
 * @Prigram: com.entity
 * @Description: TODO
 * @Author: DongFang
 * @CreaeteTime: 2018-09-19 11:54
 */

@Data
public class Admin {
    private int id;
    private String name;
    private List<String> roles;
}
