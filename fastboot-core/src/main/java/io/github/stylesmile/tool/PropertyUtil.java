package io.github.stylesmile.tool;

import java.io.*;
import java.util.Properties;

/**
 * Desc:properties文件获取工具类
 */
public class PropertyUtil {
    //	private static final Logger logger = LoggerFactory.getLogger(PropertyUtil.class);
    public static Properties props;

    synchronized static public void loadProps(Class clazz, String path) {
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
            //<!--第二种，通过类进行获取properties文件流-->
//            in = clazz.getResourceAsStream("/application.properties");
            props.load(in);
            System.out.println(props.getProperty("server.port"));
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
            loadProps(null, null);
        }
        return props.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        if (null == props) {
            loadProps(null, null);
        }
        return props.getProperty(key, defaultValue);
    }

//	public static void main(String[] args) {
//		loadProps();
//	}
}
