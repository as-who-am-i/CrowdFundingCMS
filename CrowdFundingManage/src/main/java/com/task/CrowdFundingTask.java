package com.task;

import com.dao.UserMapper;
import com.entity.Projects;
import com.entity.User;
import com.entity.UPP;
import com.exception.CrowdFundingException;
import com.service.*;
import com.until.MailUtil;
import com.until.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

/**
 * @Prigram: com.task
 * @Description: TODO
 * @Author: DongFang
 * @CreaeteTime: 2018-09-21 16:07
 */
@Slf4j
@Component
public class CrowdFundingTask {

    @Autowired
    ProjectService projectService;

    @Autowired
    FundingService fundingService;

    @Autowired
    LoginService loginService;

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    UPPService uppService;

    @Autowired
    UserMapper userMapper;

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("spring.xml");
    }

    @Transactional
    public void dealFailureProjects() throws CrowdFundingException {
        //遍历数据库中今天到期的众筹项目
        while (true) {
            int page = 0;
            //分页查询数据库中未审核的项目
            List<Projects> projectsList = projectService.findExpireProject(page);
            if (projectsList.size() == 0) {
                ResponseUtils.responseError(3, "目前没有筹款项目");
                break;
            } else {
                Iterator<Projects> iterator = projectsList.iterator();
                while (iterator.hasNext()) {
                    Projects projects = iterator.next();
                    //判断已到的筹款金额是否小于预期的筹款金额
                    dealOneProjects(projects);
                }
            }
            page++;
        }
    }

    @Transactional
    public void dealOneProjects(Projects projects) throws CrowdFundingException {
        //BigDecimal转long

        BigDecimal psMoney = projects.getPsMoney();
        BigDecimal psGetMoney = projects.getPsGetmoney();
        BigDecimal subtract = psMoney.subtract(psGetMoney);
        //当已筹款金额小于预期筹款金额时
        if (subtract.intValue() < 0) {
            // 标记项目众筹失败
            dealOneFailureProjects(projects);
        } else {
            dealOneSuccessProjects(projects);
        }
    }

    private void dealOneSuccessProjects(Projects projects) {
        int row = projectService.markFailure(3, projects.getPsId());

    }

    //todo 经过简单的测试代码存在一个问题，当网络不稳定的时，相应的邮件发送不出，数据库的数据发生了更改，事务逻辑中异常处理不够完善，加事务注释也没有用
    @Transactional
    public void dealOneFailureProjects(Projects projects) throws CrowdFundingException {
        int row = projectService.markFailure(4, projects.getPsId());
        if (row == 1) {
            ResponseUtils.responseSuccess(1, "项目众筹失败 数据库更新成功");
            List<UPP> uPPList;
            try {
                uPPList = fundingService.findProjectParticipator(projects.getPsId());

                for (UPP upp :
                        uPPList) {
                    User user = userMapper.selectByPrimaryKey(upp.getUserId());
                    //todo 这里发邮件接近底层，未做邮件发送失败处理
                    MailUtil crowdFundingFailure = new MailUtil(user.getEmail(), "你参与的" + projects.getPsName() +
                            "众筹项目，由于未能筹够预期资金导致众筹失败，您的筹款将会在3个工作日内返还到你的账户，请注意查收。");
                    new Thread(crowdFundingFailure).start();

                    //todo 将该项目中的筹款资金进行 用户的资金进行退款,获取到参与的资金，返还给用户
                    BigDecimal money = userInfoService.getMoneyByUserAndProject(projects.getPsId());

                    /*List<UPP> participateUserList = uppService.selectUserByPsId(projects.getPsId());

                    for (UPP UPP :
                            participateUserList) {*/
                    try {
                        //todo 这是一个事务，先将项目的筹款进行相应的扣除，再对相应的参见该项目的用户的资金进行返还
                        boolean flag = userInfoService.updateMoneyByUPP(upp, money);
                        //todo  此项事务完成成功以后给参与筹款的该用户发邮件已退款,
                        if (flag) {
                            MailUtil mailUtil = new MailUtil(user.getEmail(), "你参与的" + projects.getPsName() + "众筹项目，由于未能筹够预期资金导致众筹失败，退还金额" + upp.getParticipateMoney() + "已到账，请注意查收。");
                            new Thread(mailUtil).start();
                        } else {
                            log.info("用户资金退回出现问题:", upp.getUserId());
                            ResponseUtils.responseError(2, "退还id:" + user.getId() + "======email:" + user.getEmail() + "用户参与" + projects.getPsName() + "项目的筹款资金失败");
                            //throw  new CrowdFundingException(2, "退还id:" + user.getId() + "======email:" + user.getEmail() + "用户参与" + projects.getPsName() + "项目的筹款资金失败");
                        }
                    } catch (CrowdFundingException e) {
                        log.error("===退款失败===: 项目id:" + upp.getProjectId() + "中，对用户id:" + upp.getUserId() + "退款失败。");
                        //throw  new CrowdFundingException(2,"===退款失败===: 项目id:" + upp.getProjectId() + "中，对用户id:" + upp.getUserId() + "退款失败。");
                    }
                    /*}*/
                }
                //告知项目发起人项目筹款失败
                String email = loginService.findEmailByPhone(projects.getPsCustPhone());
                MailUtil mailUtil = new MailUtil(email, "你发起的" + projects.getPsName() + "众筹项目，由于未能筹够预期资金导致众筹失败，筹款金额" + projects.getPsMoney() + "已退回。");
                new Thread(mailUtil).start();
            } catch (CrowdFundingException e) {
                throw new CrowdFundingException(4, "目前该到期项目没有用户参与众筹");
            }

        } else {
            ResponseUtils.responseError(2, "项目众筹失败 数据库更新数据失败");
            throw new CrowdFundingException(2, "项目众筹失败 数据库更新数据失败");

        }
    }


}

