package com.task;

import com.entity.RealCheck;
import com.service.AdminService;
import com.service.FaceCompareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Prigram: com.task
 * @Description: TODO
 * @Author: DongFang
 * @CreaeteTime: 2018-09-20 15:58
 */
@Component
public class TimedTask {

    @Autowired
    AdminService adminService;
    @Autowired
    FaceCompareService faceCompareService;

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("spring-task.xml");
    }

    //定时调度任务先进性数据查询
    public void hello() {
        //System.out.println("hello");
        List<RealCheck> realCheckList = adminService.unRealCheckList(5);
        for (int i = 0; i < realCheckList.size(); i++) {

        }
    }

    //再进行信息比对
    public void world() {
        System.out.println("world");
    }
}
