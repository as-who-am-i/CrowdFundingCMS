package com.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
//向JUnit4声明spring.xml配置文件的地址
@ContextConfiguration(locations = {"classpath:spring.xml","classpath:spring-mvc.xml"})
public class FaceCompareServiceTest {

    @Autowired
    FaceCompareService faceCompareService;
    @Test
    public void compare() throws Exception {
        boolean compare = faceCompareService.compare("https://crowdfundingdf.oss-cn-beijing.aliyuncs.com/ldh.jpeg?Expires=1537432621&OSSAccessKeyId=TMP.AQE3egn05yTtrkBY9a037a2VnT5ZC5LHUhomS8krg0n81611JOUDqqOAvjV4ADAtAhUAgWul_pNVeairakEVGrxZ8I5xU8QCFBsAfLL2uiwI91vMV4VJCe1yYCeL&Signature=6Ur6VA4rb8Ai58RNP7fP6ekbAD8%3D",
                "https://crowdfundingdf.oss-cn-beijing.aliyuncs.com/liudehua.jpeg?Expires=1537432669&OSSAccessKeyId=TMP.AQE3egn05yTtrkBY9a037a2VnT5ZC5LHUhomS8krg0n81611JOUDqqOAvjV4ADAtAhUAgWul_pNVeairakEVGrxZ8I5xU8QCFBsAfLL2uiwI91vMV4VJCe1yYCeL&Signature=FYvbgzK4rMQb6lmC8wdKuadUO9g%3D");
        System.out.println(compare);

        boolean compare1 = faceCompareService.compare("https://crowdfundingdf.oss-cn-beijing.aliyuncs.com/ldh.jpeg?Expires=1537432621&OSSAccessKeyId=TMP.AQE3egn05yTtrkBY9a037a2VnT5ZC5LHUhomS8krg0n81611JOUDqqOAvjV4ADAtAhUAgWul_pNVeairakEVGrxZ8I5xU8QCFBsAfLL2uiwI91vMV4VJCe1yYCeL&Signature=6Ur6VA4rb8Ai58RNP7fP6ekbAD8%3D",
                "https://crowdfundingdf.oss-cn-beijing.aliyuncs.com/zhouixnchi.jpeg?Expires=1537432912&OSSAccessKeyId=TMP.AQE3egn05yTtrkBY9a037a2VnT5ZC5LHUhomS8krg0n81611JOUDqqOAvjV4ADAtAhUAgWul_pNVeairakEVGrxZ8I5xU8QCFBsAfLL2uiwI91vMV4VJCe1yYCeL&Signature=OfelkZlqH9zoNB1wJxKgl7VgMKo%3D");
        System.out.println(compare1);
    }
}