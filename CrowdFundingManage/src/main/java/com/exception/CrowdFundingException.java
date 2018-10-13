package com.exception;

/**
 * @Prigram: com.exception
 * @Description: TODO
 * @Author: DongFang
 * @CreaeteTime: 2018-09-22 17:50
 */
public class CrowdFundingException extends Exception {
    private int code;
    private String error;

    public CrowdFundingException(int code,String error ) {
        this.code=code;
        this.error=error;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
