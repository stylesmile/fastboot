package io.github.stylesmile.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.zaxxer.hikari.HikariDataSource;
import io.github.stylesmile.ioc.BeanContainer;
import io.github.stylesmile.ioc.BeanFactory;
import io.github.stylesmile.plugin.Plugin;
import io.github.stylesmile.tool.PropertyUtil;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.ResolverUtil;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
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
 * 参考文献 https://blog.csdn.net/qq_42413011/article/details/118640420
 *
 * @author hxm
 */
public class MybatisPlugin implements Plugin {

    private static final Logger log = LoggerFactory.getLogger(MybatisPlugin.class);

    SqlSession session;

    @Override
    public void start() {
        BeanFactory.addBeanClasses(Mapper.class);
    }

    @Override
    public void init() {
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        //这是mybatis-plus的配置对象，对mybatis的Configuration进行增强
        MybatisConfiguration configuration = new MybatisConfiguration();
        //这是初始化配置，后面会添加这部分代码
        initConfiguration(configuration);
        //这是初始化连接器，如mybatis-plus的分页插件
//        configuration.addInterceptor(initInterceptor());
        //配置日志实现
        configuration.setLogImpl(Slf4jImpl.class);
        //扫描mapper接口所在包
        String packageName = PropertyUtil.getProperty("mybatis-plus.scanPackage");
        Set<Class> classSet = getMapperClass(packageName);
        classSet.forEach(cls -> {
            configuration.addMapper(cls);
        });
//        configuration.addMappers(packageName);
        //构建mybatis-plus需要的globalconfig
        GlobalConfig globalConfig = new GlobalConfig();
        //此参数会自动生成实现baseMapper的基础方法映射
        globalConfig.setSqlInjector(new DefaultSqlInjector());
        //设置id生成器
//        globalConfig.setIdentifierGenerator(new DefaultIdentifierGenerator());
        //设置超类mapper
        globalConfig.setSuperMapperClass(BaseMapper.class);
        //给configuration注入GlobalConfig里面的配置
        GlobalConfigUtils.setGlobalConfig(configuration, globalConfig);
        //设置数据源
        Environment environment = new Environment("1", new JdbcTransactionFactory(), initDataSource());
        configuration.setEnvironment(environment);

        try {
            this.registryMapperXml(configuration, "mapper");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //构建sqlSessionFactory
        SqlSessionFactory sqlSessionFactory = builder.build(configuration);
        //创建session
        this.session = sqlSessionFactory.openSession();
        classSet.forEach(cls -> {
            Object bean = session.getMapper(cls);
            //注入bean容器
            BeanContainer.setInstance(cls, bean);
        });
    }

    @Override
    public void end() {

    }

    /**
     * 初始化配置
     */
    private void initConfiguration(MybatisConfiguration configuration) {
        //开启驼峰大小写转换
        configuration.setMapUnderscoreToCamelCase(true);
        //配置添加数据自动返回数据主键
        configuration.setUseGeneratedKeys(true);
    }

    /**
     * 初始化数据源
     */
    private DataSource initDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(PropertyUtil.getProperty("fast.db.url"));
        dataSource.setDriverClassName(PropertyUtil.getProperty("fast.db.driverClassName"));
        dataSource.setUsername(PropertyUtil.getProperty("fast.db.username"));
        dataSource.setPassword(PropertyUtil.getProperty("fast.db.password"));
        dataSource.setIdleTimeout(60000);
        dataSource.setAutoCommit(true);
        dataSource.setMaximumPoolSize(5);
        dataSource.setMinimumIdle(1);
        dataSource.setMaxLifetime(60000 * 10);
        dataSource.setConnectionTestQuery("SELECT 1");
        return dataSource;
    }

    /**
     * 初始化拦截器
     *
     * @return
     */
//    private Interceptor initInterceptor() {
//        //创建mybatis-plus插件对象
//        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
//        //构建分页插件
//        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
//        paginationInnerInterceptor.setDbType(DbType.MYSQL);
//        paginationInnerInterceptor.setOverflow(true);
//        paginationInnerInterceptor.setMaxLimit(500L);
//        interceptor.addInnerInterceptor(paginationInnerInterceptor);
//        return interceptor;
//    }

    /**
     * 解析mapper.xml文件
     *
     * @param configuration
     * @param classPath
     * @throws IOException
     */
    private void registryMapperXml(MybatisConfiguration configuration, String classPath) throws IOException {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> mapper = contextClassLoader.getResources(classPath);
        while (mapper.hasMoreElements()) {
            URL url = mapper.nextElement();
            if (url.getProtocol().equals("file")) {
                String path = url.getPath();
                File file = new File(path);
                File[] files = file.listFiles();
                for (File f : files) {
                    try (FileInputStream in = new FileInputStream(f)) {
                        XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(in, configuration, f.getPath(), configuration.getSqlFragments());
                        xmlMapperBuilder.parse();
                    }
//                    in.close();
                }
            } else {
                JarURLConnection urlConnection = (JarURLConnection) url.openConnection();
                JarFile jarFile = urlConnection.getJarFile();
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry jarEntry = entries.nextElement();
                    if (jarEntry.getName().endsWith(".xml")) {
                        InputStream in = jarFile.getInputStream(jarEntry);
                        XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(in, configuration, jarEntry.getName(), configuration.getSqlFragments());
                        xmlMapperBuilder.parse();
                        in.close();
                    }
                }
            }
        }
    }

    public Set<Class> getMapperClass(String packageName) {
        Set<Class> classSet = new HashSet<>();
        ResolverUtil<Class<?>> resolverUtil = new ResolverUtil();
        resolverUtil.find(new ResolverUtil.IsA(Object.class), packageName);
        Set<Class<? extends Class<?>>> mapperSet = resolverUtil.getClasses();
        Iterator var5 = mapperSet.iterator();

        while (var5.hasNext()) {
            Class<?> mapperClass = (Class) var5.next();
            //this.addMapper(mapperClass);
            classSet.add(mapperClass);
        }
        return classSet;
    }
}
