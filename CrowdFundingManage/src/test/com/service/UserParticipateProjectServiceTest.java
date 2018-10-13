package com.service;

import com.entity.UPP;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
//向JUnit4声明spring.xml配置文件的地址
@ContextConfiguration(locations = {"classpath:spring.xml","classpath:spring-mvc.xml"})
public class UserParticipateProjectServiceTest {

    @Autowired
    UPPService UPPService;

    @Test
    public void selectUserByPsId() {
        List<UPP> uppList = UPPService.selectUserByPsId(1);
        System.out.println(uppList.size());
    }
}