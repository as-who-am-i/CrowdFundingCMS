package com.service;



import com.dao.UPPMapper;
import com.entity.UPP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Prigram: com.service
 * @Description: TODO
 * @Author: DongFang
 * @CreaeteTime: 2018-09-22 15:48
 */
@Service
public class UPPService {

    @Autowired
    UPPMapper userParticipateProjectMapper;

    public List<UPP> selectUserByPsId(int psId) {
        return userParticipateProjectMapper.selectUserByPsId(psId);
    }
}
