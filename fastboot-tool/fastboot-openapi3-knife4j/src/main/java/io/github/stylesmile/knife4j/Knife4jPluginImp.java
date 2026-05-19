package io.github.stylesmile.knife4j;

import io.github.stylesmile.filter.FilterManager;
import io.github.stylesmile.plugin.Plugin;
import io.github.stylesmile.staticfile.ResourceUtil;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Knife4j 静态资源插件实现
 * 负责加载和映射 knife4j-openapi3-ui 的静态资源
 * 
 * @author Stylesmile
 */
public class Knife4jPluginImp implements Plugin {
    /**
     * 默认静态目录
     */
    public static final String DEFAULT_STATIC_LOCATION = "static/";
    /**
     * 文件映射关系 (URL路径 -> 资源路径)
     */
    public final static Map<String, String> FILE_MAPPING = new HashMap<>();

    /**
     * 根据 URL 路径获取资源路径
     * 
     * @param key URL 路径（如 /doc.html）
     * @return 资源路径
     */
    public static String get(String key) {
        String[] chars = key.split("");
        StringBuffer stringBuffer = new StringBuffer();
        for (String s : chars) {
            if (s.equals("/")) {
                stringBuffer.append("\\");
            } else {
                stringBuffer.append(s);
            }
        }
        return FILE_MAPPING.get(stringBuffer.toString());
    }

    @Override
    public void start() {
        try {
            System.out.println("Loading knife4j static resources...");
            
            // 检查是否有 knife4j 资源
            List<URL> urlList = ResourceUtil.getAllResourceFiles("META-INF/resources/doc.html");
            
            if (urlList.size() == 0) {
                System.out.println("Warning: knife4j static resources not found in classpath");
                return;
            }
            
            System.out.println("Found knife4j resources: " + urlList.size() + " location(s)");
            for (URL url : urlList) {
                System.out.println("  - " + url.toString());
            }
            
            // 添加 Knife4j 资源过滤器（支持从 JAR 包加载资源）
            FilterManager.addFilter(Knife4jResourceFilter.class);
            System.out.println("Knife4j resource filter registered successfully");
        } catch (Exception e) {
            System.err.println(String.format("Failed to initialize knife4j resources: %s", e.getMessage()));
            e.printStackTrace();
        }
    }

    /**
     * 初始化
     */
    @Override
    public void init() {
    }

    @Override
    public void end() {

    }
}
