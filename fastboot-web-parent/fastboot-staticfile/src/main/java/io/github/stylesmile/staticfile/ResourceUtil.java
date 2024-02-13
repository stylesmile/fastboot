package io.github.stylesmile.staticfile;

import com.sun.org.apache.bcel.internal.util.ClassLoader;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author Stylesmile
 */
public class ResourceUtil {
    /**
     * 是否有资源
     *
     * @param name 内部资源名称
     */
    public static boolean hasResource(String name) {
        return getResource(name) != null;
    }

    /**
     * 获取资源URL
     *
     * @param name 内部资源名称
     */
    public static URL getResource(String name) {
        return getResource(null, name);
    }

    /**
     * 获取资源URL
     *
     * @param classLoader 类加载器
     * @param name        内部资源名称
     */
    public static URL getResource(ClassLoader classLoader, String name) {
        if (classLoader == null) {
            classLoader = new ClassLoader();
            return classLoader.getResource(name);
        } else {
            return classLoader.getResource(name);
        }
    }

    public static List<URL> getAllResourceFiles(String path) {
        List<URL> urlList = new ArrayList<>();
        try {
            Enumeration<URL> resources = ResourceUtil.class.getClassLoader().getResources(path);
            while (resources.hasMoreElements()) {
                URL resourceUrl = resources.nextElement();
                urlList.add(resourceUrl);
            }
            return urlList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return urlList;
    }
}
