package com.xpp.util;

import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer;
import org.springframework.util.DigestUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * MD5加密
 * 对用户输入的密码进行加密
 */
public class MD5Utils {
    /**
     * MD5加密算法2
     *
     * @return 加密之后的密码
     * @Param str 要加密的密码
     */
    public static String code(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] byteDigest = md.digest();
            int i;
            StringBuffer buffer = new StringBuffer();
            for (int offset = 0; offset < byteDigest.length; offset++) {
                i = byteDigest[offset];
                if (i < 0) i += 256;
                if (i < 16) buffer.append("0");
                //java.lang.Integer.toHexString() 此方法返回为无符号整数基数为16的整数参数的字符串表示形式
                buffer.append(Integer.toHexString(i));
            }
            //32位加密
            return buffer.toString();
            //16位加密
            // return buffer.toString().substring(8, 24);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * UUID+MD5加密>>>32位加密
     *
     * @param oldPassword 要加密的密码
     * @return 加密之后的新密码
     */
    public static String getMD5Password(String oldPassword) {
        //基于UUID设置盐值
        String salt = UUID.randomUUID().toString();
        //基于盐值对密码进行三次加密
        String newPassword = salt + oldPassword + salt;
        for (int i = 0; i < 3; i++) {
            //md5DigestAsHex()返回给定字节的MD5摘要的十六进制字符串表示形式。
            newPassword = DigestUtils.md5DigestAsHex(newPassword.getBytes());
        }
        System.err.println("newPassword:" + newPassword);
        return newPassword;
    }

    public static void main(String[] args) {
        System.err.println(code("123569") + "......." + code("123569").length());
//        System.err.println(0x0ffffff);//16777215
//        System.err.println(getMD5Password("123569")+"......."+getMD5Password("123569").length());
    }
}
