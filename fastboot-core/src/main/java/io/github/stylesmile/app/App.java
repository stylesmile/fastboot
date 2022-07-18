package io.github.stylesmile.app;

import io.github.stylesmile.handle.HandlerManager;
import io.github.stylesmile.plugin.PlugsManager;
import io.github.stylesmile.ioc.BeanFactory;
import io.github.stylesmile.tool.ClassScanner;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import io.github.stylesmile.tool.PropertyUtil;
import io.github.stylesmile.tool.StringUtil;
//import io.github.stylesmile.tool.resource.PropertiesUtils;
//import io.github.stylesmile.tool.resource.ResourceUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class App {
    private static HttpServer httpServer = null;
    private static final PlugsManager PLUGS_MANAGER = new PlugsManager();
    public static List<Class<?>> classList = null;

    private static ExecutorService executor = new ThreadPoolExecutor(5, 20,
            60L, TimeUnit.SECONDS,
            new ArrayBlockingQueue(10));

    public static void start(Class applicationClass, String[] args) {
        long startTime = System.currentTimeMillis();
        Integer port = 8080;
        PropertyUtil.loadProps(applicationClass, "application.properties");
//        PropertiesUtils.loadConfProperties();
//        PropertyUtil.loadProps("classpath:application.properties");
//        String portString = PropertyUtil.props.getProperty("server.port","8080");
        String portString = PropertyUtil.props.getProperty("server.port");
//        URL url = ResourceUtil.getResource("application.properties");
        if (StringUtil.isNotEmpty(portString)) {
            port = Integer.valueOf(portString);
        }
        System.out.println("start server  port :" + port);
        try {
//            httpServer = HttpsServer.create(new InetSocketAddress("localhost", port), 0);
//            httpServer = HttpServer.create(new InetSocketAddress("localhost", port), 0);
            httpServer = HttpServer.create(new InetSocketAddress(port), 0);
            String package1 = applicationClass.getPackage().getName();
            //扫描所有的类，
            classList = ClassScanner.scanClasses(package1);
            //创建Bean工厂,扫描Class，创建被注解标注的类
            BeanFactory.initBean(classList);
            //找到所有Controller，建立Controller中每个方法和Url的映射关系
            HandlerManager.resolveMappingHandler(classList);
            PLUGS_MANAGER.start();
            PLUGS_MANAGER.init();
            PLUGS_MANAGER.end();
        } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        //创建Bean工厂,扫描Class，创建被注解标注的类
        HttpContext httpContext = httpServer.createContext("/", new MyHttpHandler());
        httpContext.getFilters().add(new MyFilter());
        httpServer.setExecutor(executor);
        httpServer.start();
        long endTime = System.currentTimeMillis();
        System.out.println("started in : " + (endTime - startTime) + "ms");
    }
}
