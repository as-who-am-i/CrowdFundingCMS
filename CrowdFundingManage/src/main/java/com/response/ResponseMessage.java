package com.response;

import lombok.Data;

@Data
public class ResponseMessage {
    //响应码
    private int code;
    //响应内容
    private String content;
    //响应会否成功
    private boolean isSuccess;
    //响应错误的原因
    private String error;
}
