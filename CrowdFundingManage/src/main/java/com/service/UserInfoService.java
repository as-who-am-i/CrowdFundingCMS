package com.service;

import com.dao.ProjectsMapper;
import com.dao.UserInfoMapper;
import com.dao.UserMapper;
import com.dao.UPPMapper;
import com.entity.Projects;
import com.entity.UPP;
import com.entity.UserInfo;
import com.exception.CrowdFundingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @Prigram: com.service
 * @Description: TODO
 * @Author: DongFang
 * @CreaeteTime: 2018-09-21 23:57
 */
@Service
@Slf4j
public class UserInfoService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    ProjectsMapper projectsMapper;

    @Autowired
    UPPMapper userParticipateProjectMapper;

    @Autowired
    UserInfoMapper userInfoMapper;


    //先获取该众筹项目的资金

    public BigDecimal getMoneyByUserAndProject( Integer psId) {
        Projects projects = projectsMapper.selectByPrimaryKey(psId);
        return projects.getPsMoney();
    }

    //先将项目的筹款进行相应的扣除，再获取用户参与项目的资金，将钱退回，两次修改表，这是一个事务
    @Transactional()
    public boolean updateMoneyByUPP(UPP upp, BigDecimal money) throws CrowdFundingException {


        BigDecimal participateMoney = upp.getParticipateMoney();
        // 众筹项目的资金进行退款
        BigDecimal showBigDecimal = money.subtract(participateMoney);
        Projects projects = projectsMapper.selectByPrimaryKey(upp.getProjectId());
        projects.setPsMoney(showBigDecimal);
        int rowProject = projectsMapper.updateByPrimaryKey(projects);
        if (rowProject != 1) {
            log.info("===退款失败===: 项目id:"+upp.getProjectId()+"中，对用户id:"+upp.getUserId()+"退款失败。");
            throw new CrowdFundingException(1,"===退款失败===: 项目id:"+upp.getProjectId()+"中，对用户id:"+upp.getUserId()+"退款失败。");
        }
        // 参与的用户进行接收
        UserInfo userInfo = userInfoMapper.selectByUserId(upp.getUserId());
        BigDecimal userInfoMoney = userInfo.getMoney();
        BigDecimal showRefund = userInfoMoney.add(participateMoney);
        userInfo.setMoney(showRefund);
        int rowUserInfo = userInfoMapper.updateByPrimaryKey(userInfo);
        if (rowUserInfo != 1) {
            log.info("===退款失败===: 项目id:"+upp.getProjectId()+"中，对用户id:"+upp.getUserId()+"退款失败。");
            throw new CrowdFundingException(1,"===退款失败===: 项目id:"+upp.getProjectId()+"中，对用户id:"+upp.getUserId()+"退款失败。");
        }
        return true;
    }
}
