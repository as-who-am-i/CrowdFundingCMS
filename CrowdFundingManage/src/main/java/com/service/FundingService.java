package com.service;

import com.entity.UPP;
import com.entity.User;
import com.exception.CrowdFundingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Prigram: com.service
 * @Description: TODO
 * @Author: DongFang
 * @CreaeteTime: 2018-09-21 23:04
 */
@Service
public class FundingService {
    @Autowired
    UPPService uppService;

    //根据项目的id来查参与众筹的用户
    public List<UPP> findProjectParticipator(Integer psId) throws CrowdFundingException {
        //todo 查询到的结果集要进行验证是否为空
        List<UPP> upps = uppService.selectUserByPsId(psId);
        if (upps.size()==0) {
            throw new CrowdFundingException(4,"目前该到期项目没有用户参与众筹");
        }
        return upps;
    }
}
