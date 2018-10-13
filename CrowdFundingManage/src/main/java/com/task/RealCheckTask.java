package com.task;

import com.entity.RealCheck;
import com.service.AdminService;
import com.service.FaceCompareService;
import com.until.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

/**
 * @Prigram: com.task
 * @Description: TODO
 * @Author: DongFang
 * @CreaeteTime: 2018-09-20 19:37
 */
@Component
@Slf4j
public class RealCheckTask {
    /*public static void main(String[] args) {
        new ClassPathXmlApplicationContext("spring.xml");
    }*/
    @Autowired
    AdminService adminService;
    @Autowired
    FaceCompareService faceCompareService;

    //定时调度进行业务逻的照片 自动核对
    public void realCheckByAI(){
        int page=1;
        //从数据库中国获得所有未审核的实名认请求
        while (true){
            List<RealCheck> realCheckList = adminService.unRealCheckList(page);
            log.info("从数据库中提取{}条记录",realCheckList.size());
            if (realCheckList.size() == 0) {
                ResponseUtils.responseError(30,"未发现未审核的数据");
                break;
            }else {
                Iterator<RealCheck> iterator = realCheckList.iterator();
                while (iterator.hasNext()){
                    RealCheck realCheck = iterator.next();
                    String idCardPositive = realCheck.getIdCardPositive();
                    String idCardHand = realCheck.getIdCardHand();
                    boolean flag = faceCompareService.compare(idCardHand, idCardPositive);
                    System.out.println(flag);
                    if (flag) {
                        log.info("=========照片核对认证成功==========");
                        //更新数据库，给用户发信息认证成功
                        adminService.manualRealCheck("success",realCheck.getId());
                    }else {
                        log.info("=========照片核对认证失败==========");
                        //更新数据库，给用户发消息认证失败
                        adminService.manualRealCheck("failure",realCheck.getId());
                    }
                    //TODO 需要进行自定义异常处理，当中间发生异常时进行continue操作

                }
            }
            page++;
        }


    }
}
