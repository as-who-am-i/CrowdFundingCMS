package com.controller;

import com.annotation.Permission;
import com.dao.UserMapper;
import com.entity.RealCheck;
import com.service.AdminService;
import com.service.FaceCompareService;
import com.task.RealCheckTask;
import com.until.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    UserMapper UserMapper;

    @Autowired
    AdminService adminService;

    @Autowired
    FaceCompareService faceCompareService;

    @Autowired
    RealCheckTask realCheckTask;


    @RequestMapping("/manualRealCheck.do")
    @ResponseBody
    @Permission(role = {"super"})
    private String manualRealCheck(String result,int realCheckId){
        adminService.manualRealCheck(result,realCheckId);
        return ResponseUtils.responseSuccess(1,"审核管理员正在进行审核");
    }
    //从数据库中读取数据 进行审核
    @RequestMapping("/unRealCheckList.do")
    @ResponseBody
    public String unRealCheckList(String page){
        int parseInt = Integer.parseInt(page);
        int pageNum;
        if (parseInt>0) {
            pageNum=parseInt;
            List<RealCheck> realCheckList=adminService.unRealCheckList(pageNum);
            return ResponseUtils.responseSuccess(1,realCheckList);

        }else{
            return null;
        }
    }

    @RequestMapping("/realCheckByAI.do")
    @ResponseBody
    public void realCheckByAI(){
        realCheckTask.realCheckByAI();
    }

}
