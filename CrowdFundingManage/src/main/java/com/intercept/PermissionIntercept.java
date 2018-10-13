package com.intercept;

import com.annotation.Permission;
import com.entity.Admin;
import com.service.AdminService;
import com.until.ResponseUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @Prigram: com.intercept
 * @Description: TODO
 * @Author: DongFang
 * @CreaeteTime: 2018-09-19 12:21
 */
public class PermissionIntercept implements HandlerInterceptor {

    @Autowired
    AdminService adminService;

    //在进行调用程序之前，对程序的运行环境进行定义拦截
    //return false表示拦截，不向下执行
    //return true表示放行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Admin admin = (Admin) request.getSession().getAttribute("admin");
        if (admin==null){
            return false;
        }
        HandlerMethod method = (HandlerMethod) handler;
        //判断该方法上是否有自定义的注解
        boolean present = method.getMethod().isAnnotationPresent(Permission.class);
        //当不存在注解时进行拦截，存在注解时进行判断拦截
        if (!present) {
            return false;
        }else {
            //TODO ok!
            if (admin.getRoles()==null){
                admin.setRoles(adminService.findRolesById(admin.getId()));
            }
            List<String> roleList = admin.getRoles();
            //获取方法上的 权限注解
            String[] roles = method.getMethod().getAnnotation(Permission.class).role();
            Collection intersection = CollectionUtils.intersection(roleList, Arrays.asList(roles));
            if (intersection.size()>0) {
                return true;
            }else {
                //返回一个无权处理
                response.getWriter().println(ResponseUtils.responseError(123,"无权处理"));
                return false;
            }
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
