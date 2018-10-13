package com.service;

import com.dao.AdminDao;
import com.dao.RealCheckMapper;
import com.entity.RealCheck;
import com.github.pagehelper.PageHelper;
import com.until.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Prigram: com.service
 * @Description: TODO
 * @Author: DongFang
 * @CreaeteTime: 2018-09-19 12:19
 */
@Service
public class AdminService {
    @Autowired
    AdminDao adminDao;

    @Autowired
    LoginService loginService;

    @Autowired
    RealCheckMapper realCheckMapper;

    public List<String> findRolesById(int id) {
        return adminDao.findRolesById(id);

    }

    //由于涉及多次更新数据库，此项逻辑处理是一个事务，要加事务注解
    @Transactional
    public void manualRealCheck(String result, int realCheckId) {
        int status;
        //当结果显示为成功
        if(result.equals("success")){
            status=1;
            //更新real_check中的status值
            updateStatusById(status,realCheckId);
            //更新实名认证表，并给用户发消息 告诉用户实名认证成功
            RealCheck realCheck = realCheckMapper.findById(realCheckId);
            String email=loginService.findEmailByPhone(realCheck.getPhone());
            MailUtil mailUtil = new MailUtil(email, "实名认证通过");
            //TODO 上线时用线程
            mailUtil.run();
        }
        if (result.equals("failure")) {
            status=2;
            //更新数据库
            updateStatusById(status,realCheckId);
            //更行实名认证表，并告诉用户 实名认证失败
            RealCheck realCheck = realCheckMapper.findById(realCheckId);
            String email = loginService.findEmailByPhone(realCheck.getPhone());
            MailUtil mailUtil = new MailUtil(email, "实名认证失败，请重新填写个人资料");
            //TODO 上线时用线程
            mailUtil.run();
        }
    }

    private void updateStatusById(int status, int realCheckId) {
        realCheckMapper.updateStatusById(status, realCheckId);
    }

    public List<RealCheck> unRealCheckList(int pageNum) {
        PageHelper.startPage(pageNum,1,true);
        return realCheckMapper.unRealCheckedList();
    }
}
