package io.github.stylesmile.jedis;

import com.google.gson.Gson;

public class GsonByteUtils {
    private static final Gson gson = new Gson();

    /**
     * 将 Java 对象转换为 byte[]
     * @param object 需要转换的 Java 对象
     * @return 转换后的 byte[]
     */
    public static byte[] toByteArray(Object object) {
        String jsonString = gson.toJson(object);
        return jsonString.getBytes();
    }

    /**
     * 将 byte[] 转换为 Java 对象
     * @param bytes   需要转换的 byte[]
     * @param clazz   目标 Java 对象的类型
     * @param <T>     泛型参数，指定返回对象的类型
     * @return 转换后的 Java 对象
     */
    public static <T> T fromByteArray(byte[] bytes, Class<T> clazz) {
        String jsonString = new String(bytes);
        return gson.fromJson(jsonString, clazz);
    }
}