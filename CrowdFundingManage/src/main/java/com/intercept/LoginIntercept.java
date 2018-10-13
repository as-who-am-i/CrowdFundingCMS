package com.intercept;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginIntercept implements HandlerInterceptor {

    private final Logger log = LoggerFactory.getLogger(LoggerFactory.class);

    //在请求到达controller层之前调用
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //在程序运行之前设置字符编码
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        return true;

        /*log.info("==============顺序执行：1、preHandle======================");
        log.info("requestUri:" + request.getRequestURI());
        log.info("contextPath:" + request.getContextPath());
        log.info("url:" + request.getRequestURI().substring(request.getContextPath().length()));

        String username = (String) request.getSession().getAttribute("user");

        request.getRequestDispatcher("crowdfunding/login.html").forward(request, response);
        if (username == null) {
            log.info("Interceptor:跳转到login页面！");
            return false;
        } else {
            return true;
        }*/

        /*HttpSession session = request.getSession();
        if (session.getAttribute("user")!=null){
            return true;
        }else{
            //请求重定向至登陆页面
            response.sendRedirect("/login.html");
        }
        return false;*/
    }

    //进入Handle方法后，返回ModelAndView之前执行
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        log.info("==============顺序执行：1、postHandle======================");
        if (modelAndView != null) {
            modelAndView.addObject("user", "user.html");
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("==============顺序执行：1、afterCompletion======================");
    }
}
