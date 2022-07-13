package io.github.stylesmile.tool.resource;

import java.net.URL;

public class ResourceUtil {

    /**
     * 获取资源URL
     *
     * @param name 资源名称
     */
    public static URL getResource(String name) {
        return getResource(JarClassLoader.global(), name); //XUtil.class.getResource(name);
    }
    /**
     * 获取资源URL
     *
     * @param classLoader 类加载器
     * @param name        资源名称
     */
    public static URL getResource(ClassLoader classLoader, String name) {
        return classLoader.getResource(name); //XUtil.class.getResource(name);
    }
}
