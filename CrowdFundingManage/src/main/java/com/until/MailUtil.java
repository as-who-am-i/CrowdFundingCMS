package com.until;

import com.sun.mail.util.MailSSLSocketFactory;
import lombok.extern.slf4j.Slf4j;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
@Slf4j
public class MailUtil implements Runnable {
    private String email;
    private String code;


    public MailUtil(String email, String code) {
        this.email = email;
        this.code = code;
    }
    public MailUtil(){

    }

    @Override
    public void run() {

        //声明发邮件的电子邮箱
        String from = "565852635@qq.com";
        String host = "smtp.qq.com";
        String title="众筹邮件";

        //设置系统属性
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host",host);//设置邮件服务器
        properties.setProperty("mail.smtp.auth","true");//打开认证
        try{
            //QQ邮箱需要下面这段代码
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            properties.put("mail.smtp.ssl.enable","true");
            properties.put("mail.smtp.ssl.socketFactory",sf);

            //获取默认session对象
            Session session = Session.getDefaultInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("565852635@qq.com", "vtyndbgjbuwybffh");
                }
            });

            //创建邮件对象
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            // 2.3设置邮件主题
            message.setSubject(title);
            // 2.4设置邮件内容
            String content = "<html><head></head><body><h1>这是一封激活邮件,激活请点击以下链接</h1><h3><a href='http://localhost:8071/RegisterDemo/ActiveServlet?active_code="
                    + code + "'>http://localhost:8071/RegisterDemo/ActiveServlet?--------" +code
                    + "</href></h3></body></html>";
            message.setContent(content, "text/html;charset=UTF-8");
            // 3.发送邮件
            Transport.send(message);
            System.out.println("邮件成功发送!");
        } catch (Exception e) {
           log.error("error:"+e);
        }

    }
}
