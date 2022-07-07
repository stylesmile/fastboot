package com.example.demo.tool;

import java.io.File;
import java.net.URL;
import java.util.Enumeration;

/**
 * @author Stylesmile
 */
public class ClassUtils {
	// getClassName("top.lingkang.demohibernate.entity")
    public static Class[] getClassByPackage(String packageName) {
        try {
            Enumeration<URL> resources = ClassUtils.class.getClassLoader().getResources(packageName.replaceAll("\\.", "/"));
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                String[] file = new File(url.getFile()).list();
                Class[] classList = new Class[file.length];
                for (int i = 0; i < file.length; i++) {
                    classList[i] = Class.forName(packageName + "." + file[i].replaceAll("\\.class", ""));
                }
                return classList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
