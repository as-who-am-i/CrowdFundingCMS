package com.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {
    //定义为字符数组，可以有多个元素，在调用该注解时属性值格式:role={}
    String[] role();
}
