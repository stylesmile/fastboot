package io.github.stylesmile;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisSqlSessionFactoryBuilder;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import io.github.stylesmile.bean.MybatisConfig;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author hxm
 */
public class MybatisInit {

    private static final Logger log = LoggerFactory.getLogger(MybatisInit.class);

    public static Map<String, SqlSessionFactory> initMybatis(MybatisConfig mybatisConfig, Set<Class> classes) throws IllegalAccessException, InstantiationException {
        if (mybatisConfig == null) {
            log.error("MybatisConfig 未配置");
            return null;
        }
        if (mybatisConfig.getDataSources() == null) {
            log.error("数据源为null");
            return null;
        }
        Map<String, SqlSessionFactory> stringSqlSessionFactoryHashMap = new HashMap<>();
        Set<String> dataSourceNames = mybatisConfig.getDataSources().keySet();
        Iterator<String> iterator = dataSourceNames.iterator();
        while (iterator.hasNext()) {
            String dataSourceName = iterator.next();
            MybatisConfiguration configuration = new MybatisConfiguration();
            if (mybatisConfig.getMapUnderscoreToCamelCase()) {
                configuration.setMapUnderscoreToCamelCase(true);
            }
            configuration.setUseGeneratedKeys(true);
            //扫描mapper接口所在包
            loadMapper(configuration, classes, dataSourceName);
            loadInterceptor(configuration, mybatisConfig.getPlugins());
            //构建mybatis-plus需要的globalconfig
            GlobalConfig globalConfig = new GlobalConfig();
            //此参数会自动生成实现baseMapper的基础方法映射
            globalConfig.setSqlInjector(new DefaultSqlInjector());
            //设置id生成器
            if (mybatisConfig.getIdentifierGenerator() != null) {
                globalConfig.setIdentifierGenerator(mybatisConfig.getIdentifierGenerator());
            }
            //设置超类mapper
            globalConfig.setSuperMapperClass(BaseMapper.class);
            //给configuration注入GlobalConfig里面的配置
            GlobalConfigUtils.setGlobalConfig(configuration, globalConfig);
            //设置数据源
            Environment environment = new Environment(dataSourceName, new JdbcTransactionFactory(), mybatisConfig.getDataSources().get(dataSourceName));
            configuration.setEnvironment(environment);
            loadMapperXml(configuration, mybatisConfig.getMapperLocations());
            //构建sqlSessionFactory
            SqlSessionFactory build = new MybatisSqlSessionFactoryBuilder().build(configuration);
            stringSqlSessionFactoryHashMap.put(dataSourceName, build);
        }
        return stringSqlSessionFactoryHashMap;
    }

    private static void loadInterceptor(Configuration configuration, Interceptor[] plugins) {
        /**
         * 拦截器
         */
        if (plugins != null) {
            for (Interceptor plugin : plugins) {
                configuration.addInterceptor(plugin);
            }
        }
    }

    private static void loadMapper(Configuration configuration, Set<Class> classes, String dataSourceName) {
        /**
         * Mapper
         */
        for (Class<?> aClass : classes) {
            try {
                configuration.addMapper(aClass);
                log.debug("dataSourceName {}  addMapper:{}", dataSourceName, aClass.getName());
            } catch (Throwable e) {
                System.out.println(aClass);
                e.printStackTrace();
            }
        }

    }

    private static void loadMapperXml(Configuration configuration, String path) {
        Map<String, InputStream> xmlInput = new HashMap<>();
        if (false) {
//            onlineFile(ConstConfig.CLASSPATH, path, xmlInput);
            onlineFile(null, path, xmlInput);
        } else {
            developFile("/" + path, xmlInput);
//            developFile(ConstConfig.CLASSPATH + "/" + path, xmlInput);
        }
        xmlInput.forEach((k, v) -> {
            try {
                XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(v, configuration, k, configuration.getSqlFragments());
                xmlMapperBuilder.parse();
            } finally {
                try {
                    v.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private static void developFile(String path, Map<String, InputStream> xmlInput) {
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (null != files) {
                File[] var4 = files;
                int var5 = files.length;
                for (int var6 = 0; var6 < var5; ++var6) {
                    File file2 = var4[var6];
                    if (file2.isDirectory()) {
                        developFile(file2.getAbsolutePath(), xmlInput);
                    } else {
                        try {
                            if (file2.getAbsolutePath().endsWith(".xml")) {
                                xmlInput.put(file2.getAbsolutePath(), new FileInputStream(file2.getAbsolutePath()));
                            }
                        } catch (Exception var9) {
                        }
                    }
                }
            }
        }

    }

    private static void onlineFile(String path, String mapperPath, Map<String, InputStream> xmlInput) {
        try {
            JarURLConnection jarURLConnection = (JarURLConnection) (new URL(path)).openConnection();
            JarFile jarFile = jarURLConnection.getJarFile();
            Enumeration entry = jarFile.entries();
            while (entry.hasMoreElements()) {
                JarEntry jar = (JarEntry) entry.nextElement();
                String name = jar.getName();
                if (name.startsWith(mapperPath) && name.endsWith(".xml")) {
                    System.out.println("/" + name);
                    xmlInput.put(name, MybatisInit.class.getResourceAsStream("/" + name));
                }
            }
            jarFile.close();
        } catch (Exception var6) {
        }
    }
}
