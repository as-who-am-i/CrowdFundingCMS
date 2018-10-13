package com.service;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.dao.RealCheckMapper;
import com.dao.UserMapper;
import com.entity.RealCheck;
import com.entity.User;
import com.until.CodeUtil;
import com.until.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Service
public class LoginService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    RealCheckMapper realCheckMapper;

    public User queryTUserByUsernameAndPassword(String phone, String password) {
        User user = userMapper.selectByUsernameAndPassword(phone, password);
        if (user == null) {
            return null;
        }
        return user;
    }

    public int insert(String phone, String password) {
        User user = new User();
        user.setPhone(phone);
        user.setPassword(password);
        int row = userMapper.insert(user);
        return row;
    }


    //产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";

    // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
    static final String accessKeyId = "LTAIyrDJ8IbGvsln";
    static final String accessKeySecret = "hnSMoKj41dhGZd64WVmXmN3G3Iv2wc";

    public SendSmsResponse sendVerifyCode(String phone, String verifyCode) throws ClientException {
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(phone);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName("鲲鹏技术");
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode("SMS_144851943");
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        request.setTemplateParam("{\"code\":\"" + verifyCode + "\"}");

        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId("yourOutId");

        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

        return sendSmsResponse;
    }


    public boolean sendEmail(String email, String password) {
        // 这里可以验证各字段是否为空

        //利用正则表达式（可改进）验证邮箱是否符合邮箱的格式
        if (!email.matches("^\\w+@(\\w+\\.)+\\w+$")) {
            return false;
        }
        //生成激活码
        String code = CodeUtil.getUUID();
        User tUser = new User();
        tUser.setEmail(email);
        tUser.setPassword(password);
        //将用户保存到数据库
        int row = userMapper.insert(tUser);
        System.out.println("成功保存数据库");
        //int row=1;
        //保存成功则通过线程的方式给用户发送一封邮件
        if (row > 0) {
            new Thread(new MailUtil(email, code)).start();
            return true;
        }
        return false;
    }

    String endpoint = "http://oss-cn-beijing.aliyuncs.com";
    // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
    static final String myAccessKeyId = "LTAIBeiXS1zkgCt1";
    static final String myAccessKeySecret = "C2PUvQqZrmAgTlSVV2S4h0g4YDBEK0";

    public PutObjectResult upload(String bucketName, String fileName, InputStream inputStream) throws FileNotFoundException {
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, myAccessKeyId, myAccessKeySecret);
        // 上传文件流。
        PutObjectResult putObjectResult = ossClient.putObject(bucketName, fileName, inputStream);
        // 关闭OSSClient。
        ossClient.shutdown();
        return putObjectResult;
    }

    public int insertRealCheck(RealCheck realCheck){
        int row = realCheckMapper.insert(realCheck);
        return row;
    }

    public void downLoad(String bucketName, String objectName, String target) {
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, myAccessKeyId, myAccessKeySecret);
        // 下载OSS文件到本地文件。如果指定的本地文件存在会覆盖，不存在则新建。
        ossClient.getObject(new GetObjectRequest(bucketName, objectName), new File(target));
        // 关闭OSSClient。
        ossClient.shutdown();
    }


    public String findEmailByPhone(String phone) {
        return userMapper.findEmailByPhone(phone);
    }

    public User queryUserByEmailAndPassword(String email, String password) {

        return userMapper.findEmailAndPasswordByUser(email,password);

    }
}
