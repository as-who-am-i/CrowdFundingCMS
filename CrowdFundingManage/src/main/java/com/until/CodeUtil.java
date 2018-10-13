package com.until;

import java.util.UUID;

public class CodeUtil {
    //获取UUID激活码
    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
