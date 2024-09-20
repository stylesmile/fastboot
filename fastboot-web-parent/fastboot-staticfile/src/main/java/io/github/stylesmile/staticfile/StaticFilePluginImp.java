package io.github.stylesmile.staticfile;

import io.github.stylesmile.filter.FilterManager;
import io.github.stylesmile.plugin.Plugin;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Stylesmile
 */
public class StaticFilePluginImp implements Plugin {
    /**
     * 默认静态目录
     */
    public static final String DEFAULT_STATIC_LOCATION = "static/";
    /**
     * 文件映射关系
     */
    public final static Map<String, String> FILE_MAPPING = new HashMap<>();

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
            List<URL> urlList = ResourceUtil.getAllResourceFiles(DEFAULT_STATIC_LOCATION);
            if (urlList.size() == 0) {
                return;
            }
            findFile(urlList.get(0).toString().substring(6));
            FilterManager.addFilter(StaticFileFilter.class);
        } catch (IllegalAccessException e) {
            System.err.println(String.format("init static error %s ", e.getMessage()));
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            System.err.println(String.format("init static error %s ", e.getMessage()));
            throw new RuntimeException(e);
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

    /**
     * 查找文件，
     *
     * @param folderPath 文件路径
     */
    public static void findFile(String folderPath) {
        // 指定文件夹路径
        // 创建File对象
        File folder = new File(folderPath);
        // 获取文件夹下的所有文件和文件夹
        printFileNames(folder, folderPath.length() - 1);
    }

    /**
     * 递归查询文件
     *
     * @param folder 文件夹
     * @param length 字符串长度
     */
    public static void printFileNames(File folder, int length) {
        // 获取文件夹下的所有文件和文件夹
        File[] listOfFiles = folder.listFiles();
        // 遍历并打印文件名
        for (File file : listOfFiles) {
            if (file.isDirectory()) {
                // 递归调用该方法，继续获取子文件夹中的文件名
                printFileNames(file, length);
            } else if (file.isFile()) {
                FILE_MAPPING.put(file.getPath().substring(length)
                        , file.getPath());
            }
        }
    }
}
