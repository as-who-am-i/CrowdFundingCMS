package com.entity;

import java.math.BigDecimal;

//注：UUP-->UserParticipateProject
public class UPP {
    private Integer id;

    private Integer userId;

    private Integer projectId;

    private BigDecimal participateMoney;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public BigDecimal getParticipateMoney() {
        return participateMoney;
    }

    public void setParticipateMoney(BigDecimal participateMoney) {
        this.participateMoney = participateMoney;
    }
}