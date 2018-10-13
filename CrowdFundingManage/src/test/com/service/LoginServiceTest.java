package com.service;

import com.aliyun.oss.model.PutObjectResult;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.entity.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
//向JUnit4声明spring.xml配置文件的地址
@ContextConfiguration(locations = {"classpath:spring.xml","classpath:spring-mvc.xml"})
public class LoginServiceTest {

    @Autowired
    LoginService loginService;
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void queryTUserByUsernameAndPassword() {
        User tom = loginService.queryTUserByUsernameAndPassword("12312341234", "123456");
        System.out.println(tom);
        Assert.assertTrue(tom!=null);
    }

    @Test
    public void sendVerifyCode() throws ClientException {
        SendSmsResponse response =
                loginService.sendVerifyCode("13230181876", "666666");
        System.out.println(response.getMessage());
    }

    @Test
    public void insert() {
        int row = loginService.insert("18792937052", "666666");
        assertTrue(row==1);
    }

    @Test
    public void sendEmail() {
        boolean doRegister = loginService.sendEmail("565852635@qq.com", "123456");

        System.out.println(doRegister);

        assertTrue(doRegister==true);

    }

    @Test
    public void upload() throws FileNotFoundException {
        PutObjectResult result = loginService.upload("crowdfundingdf", "aixi.jpg",
                new FileInputStream(new File("E:\\daydayup\\java\\image\\aixi.jpg")));
        System.out.println(result);
    }

    @Test
    public void downLoad() {
    }
}