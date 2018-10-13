package com.service;

import com.until.MailUtil;
import org.springframework.stereotype.Service;

/**
 * @Prigram: com.service
 * @Description: TODO
 * @Author: DongFang
 * @CreaeteTime: 2018-09-23 22:40
 */

@Service
public class MailService {


    public void sendEmail(String email,String code){
        MailUtil mailUtil = new MailUtil(email, code);
        new Thread(mailUtil).start();
    }
}
