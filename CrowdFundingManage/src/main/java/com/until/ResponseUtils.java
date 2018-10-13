package com.until;

import com.google.gson.Gson;
import com.response.Response;

public class ResponseUtils {
    public static<T> String responseError(int code, String error) {
        Response<T> response = new Response<>(null, code, error);
        return new Gson().toJson(response);
    }

    public static<T> String responseSuccess(int code, T data) {
        Response<T> response = new Response<>(data, code, null);
        return new Gson().toJson(response);
    }
}
