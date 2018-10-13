package com.until;

import org.apache.commons.codec.binary.Base64;

import java.io.*;

/**
 * @Prigram: com.until
 * @Description: TODO
 * @Author: DongFang
 * @CreaeteTime: 2018-09-20 14:11
 */
public class ImgBase64Util {
    public static void main(String[] args) {
        String imgFile = "E:\\daydayup\\java\\image\\aixi.jpeg";
        String imgBase = getImgStr(imgFile);
        System.out.println(imgBase.length());
        System.out.println(imgBase);
        boolean res = showImage(imgBase, "E:\\daydayup\\java\\image\\0920.jpeg");
        System.out.println(res);
    }

    //将图片转换成Base64编码
    public static String getImgStr(String imgFile) {
        //将图片文件转换成字节数组字符串，并对其进行Base64编码处理
        InputStream inputStream;
        byte[] data = null;
        //读取图片字节数组
        try {
            inputStream = new FileInputStream(imgFile);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(Base64.encodeBase64(data));
    }

    //对字节数组字符串进行Base64解码并生成图片
    public static boolean showImage(String imgStr, String imgFilePath) {
        if (imgStr == null) {
            return false;
        }
        try {
            //Base64解码
            byte[] bytes = Base64.decodeBase64(imgStr);
            for (int i = 0; i < bytes.length; i++) {
                if (bytes[i] < 0) {
                    //调整异常数据
                    bytes[i] += 256;
                }
            }
            //生成jpeg图片
            FileOutputStream outputStream = new FileOutputStream(imgFilePath);
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
}
