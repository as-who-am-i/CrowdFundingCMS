package com.until;

import org.junit.Test;

import static org.junit.Assert.*;

public class MailUtilTest {

    @Test
    public void run() {
        MailUtil mailUtil = new MailUtil("565852635@qq.com", "123");
        mailUtil.run();
        //new Thread(mailUtil).start();
    }
}