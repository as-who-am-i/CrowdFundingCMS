package com.controller;

import com.exception.CrowdFundingException;
import com.until.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Prigram: com.controller
 * @Description: TODO
 * @Author: DongFang
 * @CreaeteTime: 2018-09-22 17:55
 */
@Slf4j
public class ExceptionController {
    @ExceptionHandler
    @ResponseBody
    public String handleException(Exception e,HttpServletRequest request, HttpServletResponse response){
        log.error("error:",e);
        if (e instanceof CrowdFundingException){
            CrowdFundingException crowdFundingException = (CrowdFundingException) e;
            return ResponseUtils.responseError(crowdFundingException.getCode(),crowdFundingException.getError());
        }else {
            return ResponseUtils.responseError(2,"请求失败");
        }
    }
}
