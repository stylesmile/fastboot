package com.example.demo.tool;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 类扫描器，将指定包下的所有Class收集起来
 */
public class ClassScanner {
    /**
     * 扫描指定包下的所有Class
     */
    public static List<Class<?>> scanClasses(String packageName) throws IOException, ClassNotFoundException {
        List<Class<?>> classList = new ArrayList<>();
        //将类的全路径名转换为文件路径
        String path = packageName.replace(".", "/");
        //获取类加载器
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources(path);
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            //如果是jar包，则获取jar包绝对路径
            if (resource.getProtocol().contains("jar")) {
                JarURLConnection jarURLConnection = (JarURLConnection) resource.openConnection();
                String jarFilePath = jarURLConnection.getJarFile().getName();
                //通过jar包的路径，获取jar包下所有的类
                classList.addAll(getClassesFromJar(jarFilePath, path));
            } else {
                //非jar包类型
                Set<Class<?>>  classSet = ClassUtil.getClasses(packageName);
                System.out.println(classSet);
                classList.addAll(classSet);
            }
        }
        return classList;
    }

    /**
     * 通过jar包的路径，获取jar包下所有的类
     *
     * @param jarFilePath jar包的绝对路径
     * @param path        需要获取的类的相对路径，用来过滤
     */
    private static List<Class<?>> getClassesFromJar(String jarFilePath, String path) throws IOException, ClassNotFoundException {
        ArrayList<Class<?>> classes = new ArrayList<>();
        JarFile jarFile = new JarFile(jarFilePath);
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            //com/mooc/zbs/test/Test.class
            String entryName = jarEntry.getName();
            if (entryName.startsWith(path) && entryName.endsWith(".class")) {
                //获取类的全类名
                String classFullName = entryName.replace("/", ".")
                        .substring(0, entryName.length() - 6);
                classes.add(Class.forName(classFullName));
            }
        }
        return classes;
    }
}