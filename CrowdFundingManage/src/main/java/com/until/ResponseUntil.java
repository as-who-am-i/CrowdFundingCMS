package com.until;

import com.google.gson.Gson;
import com.response.ResponseMessage;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseUntil {
    public static void response(HttpServletResponse response,String content,String error,boolean isSuccess,int code) throws IOException {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setContent(content);
        responseMessage.setSuccess(isSuccess);
        responseMessage.setError(error);
        responseMessage.setCode(code);
        String json = new Gson().toJson(responseMessage);
        response.getWriter().println(json);
    }
    public static void responseSuccess(HttpServletResponse response,String content,int code) throws IOException {
        response(response,content,null,true,code);

    }
    public static void responseFailure(HttpServletResponse response,String error,int code) throws IOException {
        response(response,null,error,false,code);

    }
}
