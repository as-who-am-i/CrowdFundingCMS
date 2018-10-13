package com.controller;

import com.google.gson.Gson;
import com.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
//表示测试的是controller层
@WebAppConfiguration
//向JUnit4声明spring.xml配置文件的地址
@ContextConfiguration(locations = {"classpath:spring.xml", "classpath:spring-mvc.xml"})
public class LoginControllerTest {

    //web反射bean工厂
    @Autowired
    WebApplicationContext wac;

    //controller测试核心类，模拟MVC进行测试
    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void login() throws Exception {
        mockMvc.perform(post("/login/login.do").param("email", "12341234@qq.com").param("password",
                "123456"))
                .andDo(print())
                .andExpect(status().isOk())
                //.andExpect(view().name("crowdfunding/user.html"))
                .andReturn();

    }

    @Test
    public void logout() throws Exception {
        mockMvc.perform(get("/login/logout.do"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getVerifyCode() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/login/verifyCode.do")
                .param("phone", "15737645305"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        //获取响应中的字符信息
        String responseString = mvcResult.getResponse().getContentAsString();
        Response fromJson = new Gson().fromJson(responseString, Response.class);
        System.out.println(fromJson);
        //assertTrue(fromJson.getCode()==2);
    }

    @Test
    public void checkVerifyCode() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/login/checkVerifyCode.do")
                .param("phone", "13137730639")
                .param("password", "123456")
                .param("code", "596082")
                .sessionAttr("code", "596082")
                .sessionAttr("codeExpireTime", System.currentTimeMillis() + 1000000))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String responseContentAsString = response.getContentAsString();
        Response fromJson = new Gson().fromJson(responseContentAsString, Response.class);

        System.out.println(fromJson.getCode());

        assertTrue(fromJson.getCode() == 11);
        System.out.println(fromJson.toString());
    }

    @Test
    public void sendEmail() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/login/sendEmail.do")
                .param("email", "565852635@qq.com").param("password", "&password=123456"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        //获取响应中的字符信息
        String responseString = mvcResult.getResponse().getContentAsString();
        Response fromJson = new Gson().fromJson(responseString, Response.class);
        System.out.println(fromJson);
    }

    @Test
    public void checkEmailVerifyCode() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/login/checkEmailVerifyCode.do")
                .param("email", "565852635@qq.com")
                .param("password", "123456")
                .param("code", "664048")
                .sessionAttr("code", "664048")
                .sessionAttr("codeExpireTime", System.currentTimeMillis() + 1000000))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String responseContentAsString = response.getContentAsString();
        Response fromJson = new Gson().fromJson(responseContentAsString, Response.class);

        System.out.println(fromJson.getCode());

        assertTrue(fromJson.getCode() == 20);
        System.out.println(fromJson.toString());

    }

    @Test
    public void realCheck() throws Exception {
        MockMultipartFile file1 = new MockMultipartFile("idCardNegative.jpeg", "do.jpeg", "image/jpeg", new FileInputStream("E:\\daydayup\\java\\image\\idCardNegative.jpeg"));
        MockMultipartFile file2 = new MockMultipartFile("idCardPositive.jpeg", "do.jpeg", "image/jpeg", new FileInputStream("E:\\daydayup\\java\\image\\idCardPositive.jpeg"));
        MockMultipartFile file3 = new MockMultipartFile("idCardHand.jpeg", "do.jpeg", "image/jpeg", new FileInputStream("E:\\daydayup\\java\\image\\idCardHand.jpeg"));

        MvcResult mvcResult = mockMvc.perform(fileUpload("/login/realCheck.do")
                .file(file1)
                .file(file2)
                .file(file3)
                .param("idCard", "41132119940555503695")
                .param("phone", "123123123145"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(mvcResult);


    }
}