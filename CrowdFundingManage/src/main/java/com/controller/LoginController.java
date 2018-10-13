package com.controller;

import com.alibaba.druid.util.StringUtils;
import com.aliyuncs.exceptions.ClientException;
import com.entity.RealCheck;
import com.entity.User;

import com.service.LoginService;
import com.until.MailUtil;
import com.until.ResponseUntil;
import com.until.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@Controller
@Slf4j
@RequestMapping("/login")
public class LoginController {

    @Autowired
    LoginService loginService;

    @RequestMapping(value = "/login.do",produces = "text/html;charset=UTF-8")
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String username=null;
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        //TODO 用正则表达式判断时手机号还是邮箱，再进行登陆，这样做更好
        //邮箱登陆
        if (email != null&&phone==null) {
            username=email;
            if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
                ResponseUntil.responseFailure(response, "用户名 密码不能为空", 100);
                return;
            }else {
                User user = loginService.queryUserByEmailAndPassword(username,password);
                if (user == null) {
                    ResponseUntil.responseFailure(response, "用户名密码错误", 100);
                    return;
                } else {
                    //用户名密码正确
                    request.getSession().setAttribute("user", user);
                    ResponseUntil.responseSuccess(response, "登陆成功", 200);
                }
            }
        }
        //手机号码登陆
        if (phone!=null) {
            username = phone;
            //后台防黑客破解注入
            if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
                ResponseUntil.responseFailure(response, "用户名 密码不能为空", 100);
                return;
            }
            //数据库进行查询
            User user = loginService.queryTUserByUsernameAndPassword(username, password);
            if (user == null) {
                ResponseUntil.responseFailure(response, "用户名密码错误", 100);
                return;
            } else {
                //用户名密码正确
                request.getSession().setAttribute("user", user);
                ResponseUntil.responseSuccess(response, "登陆成功", 200);
            }
        }
    }

    //退出登录   测试未通过
    @RequestMapping("/logout.do")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            Object user = request.getSession().getAttribute("user");
            if (user == null) {
                ResponseUntil.responseFailure(response, "当前未登录", 100);
            } else {
                ResponseUntil.responseSuccess(response, "成功退出登陆", 302);
                request.getSession().removeAttribute("user");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //短信验证码注册
    @ResponseBody
    @RequestMapping(value = "/verifyCode.do", produces = "text/html;charset=UTF-8")
    public String getVerifyCode(String phone, HttpServletRequest request) {

        if (StringUtils.isEmpty(phone)) {
            return ResponseUtils.responseError(1, "手机号码不能为空");
        }
        String code = generateVerifyCode(6);
        //发送信息
        try {
            loginService.sendVerifyCode(phone, code);
        } catch (ClientException e) {
            log.info("============info==========" + e);
            return ResponseUtils.responseError(2, "发送失败");
        }
        //保存验证码
        request.getSession().setAttribute("code", code);
        //设置验证码保存在session中的时长
        long current = System.currentTimeMillis();
        long expireTime = current + 1000 * 60 * 10;
        request.getSession().setAttribute("codeExpireTime", expireTime);
        return ResponseUtils.responseSuccess(1, code);
    }

    //校验 验证码
    @ResponseBody
    @RequestMapping(value = "/checkVerifyCode.do", produces = "text/html;charset=UTF-8")
    public String checkVerifyCode(String code, String phone, String password, HttpServletRequest request, HttpServletResponse response) {

        String phone1 = request.getParameter("phone");
        String password1 = request.getParameter("password");

        HttpSession session = request.getSession();
        long codeExpireTime = (long) session.getAttribute("codeExpireTime");
        long currentTimeMillis = System.currentTimeMillis();
        //session是否过期
        if (currentTimeMillis > codeExpireTime) {
            return ResponseUtils.responseError(3, "验证码已过期");
        }
        //获取session中的code验证码
        String sessionCode = (String) session.getAttribute("code");
        //校验验证码是否正确
        if (sessionCode.equals(code)) {
            //用户注册的具体逻辑
            if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(password)) {
                return ResponseUtils.responseError(4, "请输入手机号和密码");
            }
            //将用户信息插入数据库
            int row = loginService.insert(phone1, password1);
            System.out.println(row == 1);
            System.out.println("insert in to database");

            return ResponseUtils.responseSuccess(11, "恭喜，成功注册！");
        } else {
            return ResponseUtils.responseError(5, "验证码错误，请重新输入验证码");
        }
    }


    private String generateVerifyCode(int length) {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int num = random.nextInt(10);
            builder.append(num);
        }
        return builder.toString();
    }

    //邮箱发送注册
    @ResponseBody
    @RequestMapping(value = "/sendEmail.do", produces = "text/html;charset=UTF-8")
    public String sendEmail(String email, String password, HttpServletRequest request) {

        /*String email = request.getParameter("email");
        String password = request.getParameter("password");*/

        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
            return ResponseUtils.responseError(21, "邮箱 密码不能为空");
        }
        String code = generateVerifyCode(6);
        MailUtil mailUtil = new MailUtil(email, code);
        //new Thread(mailUtil).start();
        mailUtil.run();


        //保存验证码
        request.getSession().setAttribute("code", code);
        //设置验证码保存在session中的时长
        long current = System.currentTimeMillis();
        long expireTime = current + 1000 * 60 * 10;
        request.getSession().setAttribute("codeExpireTime", expireTime);
        return ResponseUtils.responseSuccess(1, code);


    }

    //校验 邮箱
    @ResponseBody
    @RequestMapping(value = "/checkEmailVerifyCode.do", produces = "text/html;charset=UTF-8")
    public String checkEmailVerifyCode(String email, String password, String code, HttpServletRequest request) {

        /*String email = request.getParameter("email");
        String password = request.getParameter("password");*/

        HttpSession session = request.getSession();
        long codeExpireTime = (long) session.getAttribute("codeExpireTime");
        long currentTimeMillis = System.currentTimeMillis();
        //session是否过期
        if (currentTimeMillis >= codeExpireTime) {
            return ResponseUtils.responseError(3, "验证码已过期");
        }
        //获取session中的code验证码
        String sessionCode = (String) session.getAttribute("code");
        //校验验证码是否正确
        if (sessionCode.equals(code)) {
            //用户注册的具体逻辑
            if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
                return ResponseUtils.responseError(4, "请输入邮箱和密码");
            }
            //将用户信息插入数据库
            boolean sendEmailStatus = loginService.sendEmail(email, password);
            System.out.println("insert in to database");
            if (sendEmailStatus) {
                System.out.println("恭喜，成功注册");
                return ResponseUtils.responseSuccess(20, "恭喜，成功注册");
            } else {
                System.out.println("抱歉，注册失败");
                return ResponseUtils.responseError(22, "抱歉，邮箱注册失败");
            }
        } else {
            return ResponseUtils.responseError(5, "验证码错误，请重新输入验证码");
        }
    }


    //实名认证
    @ResponseBody
    @RequestMapping(value = "realCheck.do",produces = "text/html;charset=UTF-8")
    public String realCheck(String phone, String idCard, HttpServletRequest request) {
        RealCheck realCheck = new RealCheck();
        realCheck.setPhone(phone);
        realCheck.setIdCard(idCard);
        realCheck.setStatus(0);

        //将当前上下文初始化 CommonsMultipartResolver(多部分解析器)
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());

        //检查form中是否有enctype="multipart/form-data"
        if (multipartResolver.isMultipart(request)) {
            //将request变成多部分request
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            //TODO 文件名还是写的固定的，在现实生活中，照片的名字是不能确定的，这里的方法还需要进一步利用反射来解决
            //获取multipartHttpServletRequest中的所有文件名
            Iterator<String> fileNames = multipartHttpServletRequest.getFileNames();
            List<String> list = new ArrayList<>();
            list.add("idCardPositive");
            list.add("idCardNegative");
            list.add("idCardHand");

            while (fileNames.hasNext()) {
                MultipartFile file = multipartHttpServletRequest.getFile(fileNames.next().toString());

                if (file != null) {
                    System.out.println(file.getName());
                    for (int i = 0; i < list.size(); i++) {
                        if (file.getName().contains(list.get(i))) {
                            String setMethod = "set" + list.get(i).substring(0, 1).toUpperCase() + list.get(i).substring(1);
                            //setMethod.invoke(realCheck,list.get(i));

                            String newFileName = UUID.randomUUID().toString() + ".jpeg";
                            if (setMethod.equals("setIdCardPositive")) {
                                realCheck.setIdCardPositive(newFileName);
                            }
                            if (setMethod.equals("setIdCardNegative")) {
                                realCheck.setIdCardNegative(newFileName);
                            }
                            if (setMethod.equals("setIdCardHand")) {
                                realCheck.setIdCardHand(newFileName);
                            }
                            try {
                                //上传文件
                                loginService.upload("crowdfundingdf", newFileName, file.getInputStream());
                            } catch (IOException e) {
                                log.error("error:", e);
                                return ResponseUtils.responseError(55, "上传失败");
                            }
                        }
                    }
                }
            }
        }
        //TODO 将数据写入数据库
        int row = loginService.insertRealCheck(realCheck);
        if (row == 1) {
            return ResponseUtils.responseSuccess(1,"申请成功，等待审核");
        }else {
            return ResponseUtils.responseError(2,"抱歉，实名认证申请失败");
        }

    }
    /*@Autowired
     HttpServletRequest request;

    public static void main(String[] args) {

        LoginController loginController = new LoginController();

        String realCheck = loginController.realCheck("12312341234", "1231231999121212120000", loginController.request);
        System.out.println(realCheck);
    }*/
}
