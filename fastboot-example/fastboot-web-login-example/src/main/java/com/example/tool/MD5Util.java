package com.example.tool;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

    /**
     * 计算字符串的MD5哈希值
     * @param input 要计算哈希值的字符串
     * @return MD5哈希值的十六进制表示
     */
    public static String calculateMD5(String input) {
        try {
            // 获取MD5 MessageDigest实例
            MessageDigest md = MessageDigest.getInstance("MD5");
            
            // 对输入字符串进行哈希计算
            byte[] digest = md.digest(input.getBytes());
            
            // 将字节数组转换为十六进制字符串
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("无法找到MD5算法", e);
        }
    }

    public static void main(String[] args) {
        String testString = "Hello, World!";
        String md5Hash = calculateMD5(testString);
        System.out.println("MD5 Hash of \"" + testString + "\" is: " + md5Hash);
    }
}
