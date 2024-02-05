package com.jiyan.captcha.util;

import java.util.Random;

public class RandomCharUtil {
    public static final String ALL_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateRandomString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(ALL_CHARS.length());
            sb.append(ALL_CHARS.charAt(randomIndex));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String randomChars = generateRandomString(6);
        System.out.println("随机字符串：" + randomChars);
    }
}
