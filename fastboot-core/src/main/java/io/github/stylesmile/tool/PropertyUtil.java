package io.github.stylesmile.tool;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Desc:properties文件获取工具类
 */
public class PropertyUtil {
    //	private static final Logger logger = LoggerFactory.getLogger(PropertyUtil.class);
    public static Properties props;

    synchronized static public void loadProps(Class clazz, String path, String[] args) {
//		logger.info("开始加载properties文件内容.......");
        props = new Properties();
        InputStream in = null;
        try {
            if (clazz == null) {
                clazz = PropertyUtil.class;
            }
            //<!--第一种，通过类加载器进行获取properties文件流-->
            String propertiesPath = clazz.getProtectionDomain().getCodeSource().getLocation().getFile();
            if (propertiesPath.contains("/target/classes")) {
                in = new FileInputStream(propertiesPath + path);
            } else {
                in = clazz.getClassLoader().getResourceAsStream(path);
            }
            if (in == null) {
                System.err.println("application.properties " + "文件未找到");
                props.setProperty("server.port=8080", "8080");
            } else {
                props.load(in);
            }
            String port = null;
            String env = PropertyUtil.getProperty("fastboot.active");
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                if (arg.contains("--server.port=")) {
                    arg = arg.replace("--server.port=", "");
                    if (StringUtil.isNotBlank(arg)) {
                        port = arg.trim();
                    }
                }
                if (arg.contains("--fastboot.active=")) {
                    arg = arg.replace("--fastboot.active=", "");
                    if (StringUtil.isNotBlank(arg)) {
                        env = arg.trim();
                    }
                }
            }
            if (StringUtil.isNotBlank(env)) {
                path = "application-" + env + ".properties";
                if (propertiesPath.contains("/target/classes")) {
                    in = new FileInputStream(propertiesPath + path);
                } else {
                    in = clazz.getClassLoader().getResourceAsStream(path);
                }
                if (in == null && StringUtil.isEmpty(port)) {
                    System.err.println("application.properties " + "文件未找到");
                    port = "8080";
                } else {
                    props.load(in);
                }
                System.out.println("fastboot current environment is " + env);
            }
            props.setProperty("server.port", port);
        } catch (FileNotFoundException e) {
            System.err.println(path + "文件未找到");
        } catch (IOException e) {
            System.err.println("出现IOException");
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
            } catch (IOException e) {
//				logger.error("jdbc.properties文件流关闭出现异常");
            }
        }
//		logger.info("加载properties文件内容完成...........");
//		logger.info("properties文件内容：" + props);
    }

    public static String getProperty(String key) {
        if (null == props) {
            loadProps(null, null, new String[0]);
        }
        return props.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        if (null == props) {
            loadProps(null, null, new String[0]);
        }
        return props.getProperty(key, defaultValue);
    }

//	public static void main(String[] args) {
//		loadProps();
//	}
}
