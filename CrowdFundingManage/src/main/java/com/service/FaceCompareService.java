package com.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.until.ResponseUtils;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * @Prigram: com.service
 * @Description: TODO
 * @Author: DongFang
 * @CreaeteTime: 2018-09-20 15:03
 */

@Service
public class FaceCompareService {
    static final String ak_id = "LTAIBeiXS1zkgCt1"; //用户ak
    static final String ak_secret = "C2PUvQqZrmAgTlSVV2S4h0g4YDBEK0"; // 用户ak_secret
    static final String url = "https://dtplus-cn-shanghai.data.aliyuncs.com/face/verify";


    //比较图片
    public boolean compare(String url1,String url2){
        //在项目开发中业务逻辑 尽量避免字符串的凭借
        // String body = "{\"type\":0,\"image_url_1\":\""+url1+"\",\"image_url_2\":\""+url2+"\"}";
        //System.out.println("response body:" + sendPost(url, body, ak_id, ak_secret));
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("type",0);
        hashMap.put("image_url_1",url1);
        hashMap.put("image_url_2",url2);
        //进行map转json
        String body = new Gson().toJson(hashMap);
        try {
            String jsonResult = sendPost(url, body, ak_id, ak_secret);
            //获取返回结果集中jsonResult中confidence,进行字符串转json
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = jsonParser.parse(jsonResult).getAsJsonObject();
            double confidence = jsonObject.get("confidence").getAsDouble();
            if (confidence>80.00){
                return true;
            }else {
                return false;
            }

        } catch (Exception e) {
            ResponseUtils.responseError(403,"图片识别失败");
            e.printStackTrace();
        }

        return true;
    }
    //计算MD5+BASE64
    public static String MD5Base64(String s){
        if (s == null) {
            return null;
        }
        String encodeStr;
        byte[] bytes = s.getBytes();
        MessageDigest messageDigest;
        try{
            messageDigest=MessageDigest.getInstance("MD5");
            messageDigest.update(bytes);
            byte[] md5Bytes = messageDigest.digest();
            BASE64Encoder base64Encoder = new BASE64Encoder();
            encodeStr = base64Encoder.encode(md5Bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new Error("Failed to generate MD5 : " + e.getMessage());
        }
        return encodeStr;
    }
    /*
     * 计算 HMAC-SHA1
     */
    public static String HMACSha1(String data, String key) {
        String result;
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data.getBytes());
            result = (new BASE64Encoder()).encode(rawHmac);
        } catch (Exception e) {
            throw new Error("Failed to generate HMAC : " + e.getMessage());
        }
        return result;
    }
    /*
     * 等同于javaScript中的 new Date().toUTCString();
     */
    public static String toGMTString(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.UK);
        df.setTimeZone(new java.util.SimpleTimeZone(0, "GMT"));
        return df.format(date);
    }
    /*
     * 发送POST请求
     */
    public static String sendPost(String url, String body, String ak_id, String ak_secret) throws Exception {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        int statusCode = 200;
        try {
            URL realUrl = new URL(url);
            /*
             * http header 参数
             */
            String method = "POST";
            String accept = "application/json";
            String content_type = "application/json";
            String path = realUrl.getFile();
            String date = toGMTString(new Date());
            // 1.对body做MD5+BASE64加密
            String bodyMd5 = MD5Base64(body);
            String stringToSign = method + "\n" + accept + "\n" + bodyMd5 + "\n" + content_type + "\n" + date + "\n"
                    + path;
            // 2.计算 HMAC-SHA1
            String signature = HMACSha1(stringToSign, ak_secret);
            // 3.得到 authorization header
            String authHeader = "Dataplus " + ak_id + ":" + signature;
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", accept);
            conn.setRequestProperty("content-type", content_type);
            conn.setRequestProperty("date", date);
            conn.setRequestProperty("Authorization", authHeader);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(body);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            statusCode = ((HttpURLConnection)conn).getResponseCode();
            if(statusCode != 200) {
                in = new BufferedReader(new InputStreamReader(((HttpURLConnection)conn).getErrorStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            }
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (statusCode != 200) {
            throw new IOException("\nHttp StatusCode: "+ statusCode + "\nErrorMessage: " + result);
        }
        return result;
    }
    /*
     * GET请求
     */
    public static String sendGet(String url, String ak_id, String ak_secret) throws Exception {
        String result = "";
        BufferedReader in = null;
        int statusCode = 200;
        try {
            URL realUrl = new URL(url);
            /*
             * http header 参数
             */
            String method = "GET";
            String accept = "application/json";
            String content_type = "application/json";
            String path = realUrl.getFile();
            String date = toGMTString(new Date());
            // 1.对body做MD5+BASE64加密
            // String bodyMd5 = MD5Base64(body);
            String stringToSign = method + "\n" + accept + "\n" + "" + "\n" + content_type + "\n" + date + "\n" + path;
            // 2.计算 HMAC-SHA1
            String signature = HMACSha1(stringToSign, ak_secret);
            // 3.得到 authorization header
            String authHeader = "Dataplus " + ak_id + ":" + signature;
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", accept);
            connection.setRequestProperty("content-type", content_type);
            connection.setRequestProperty("date", date);
            connection.setRequestProperty("Authorization", authHeader);
            connection.setRequestProperty("Connection", "keep-alive");
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            statusCode = ((HttpURLConnection)connection).getResponseCode();
            if(statusCode != 200) {
                in = new BufferedReader(new InputStreamReader(((HttpURLConnection)connection).getErrorStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            }
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (statusCode != 200) {
            throw new IOException("\nHttp StatusCode: "+ statusCode + "\nErrorMessage: " + result);
        }
        return result;
    }
    /*public static void main(String[] args) throws Exception {
        // 发送POST请求示例
        String ak_id = "LTAIyrDJ8IbGvsln"; //用户ak
        String ak_secret = "hnSMoKj41dhGZd64WVmXmN3G3Iv2wc"; // 用户ak_secret
        String url = "https://dtplus-cn-shanghai.data.aliyuncs.com/face/verify";
        String body = "{\"type\":0,\"image_url_1\":\"https://crowdfundingweb.oss-cn-beijing.aliyuncs.com/cxc1.png\",\"image_url_2\":\"https://crowdfundingweb.oss-cn-beijing.aliyuncs.com/f55ea5f9-047c-4d53-9abd-83f93d0643a7.jpeg\"}";
        System.out.println("response body:" + sendPost(url, body, ak_id, ak_secret));

    }*/

}
