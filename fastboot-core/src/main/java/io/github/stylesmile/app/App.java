package io.github.stylesmile.app;

import io.github.stylesmile.annotation.Fastboot;
import io.github.stylesmile.handle.HandlerManager;
import io.github.stylesmile.ioc.BeanFactory;
import io.github.stylesmile.jlhttpserver.JdkHttpContextHandler;
import io.github.stylesmile.plugin.ServerPlugsManager;
import io.github.stylesmile.plugin.StartPlugsManager;
import io.github.stylesmile.server.JdkHTTPServer;
import io.github.stylesmile.server.MethodType;
import io.github.stylesmile.tool.ClassScanner;
import io.github.stylesmile.tool.PropertyUtil;
import io.github.stylesmile.tool.StringUtil;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class App {
    private static JdkHTTPServer httpServer = null;
    private static final StartPlugsManager BEAN_PLUGS_MANAGER = new StartPlugsManager();
    private static final ServerPlugsManager SERVER_PLUGS_MANAGER = new ServerPlugsManager();
    public static List<Class<?>> classList = null;

    /**
     * cpu count
     */
    private static int corePoolSize = Runtime.getRuntime().availableProcessors();

    /**
     * 创建线程池  调整队列数 拒绝服务
     */
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, corePoolSize * 2, 50L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(2000));

    public static void start(Class applicationClass, String[] args) {
        long startTime = System.currentTimeMillis();
        // 需要扫描的包
        String scanPackage = null;
        // 需要添加添加的类
        Class[] includeClass = null;
        // 需要排除的类
        Class[] excludeClass = null;
        if (applicationClass.isAnnotationPresent(Fastboot.class)) {
            Fastboot annotation = (Fastboot) applicationClass.getAnnotation(Fastboot.class);
            scanPackage = annotation.scanPackage();
            includeClass = annotation.include();
            excludeClass = annotation.exclude();
        }
        // 默认为8080端口
        Integer port = 8080;
        PropertyUtil.loadProps(applicationClass, "application.properties", args);
        String portString = PropertyUtil.getProperty("server.port");
        if (StringUtil.isNotEmpty(portString)) {
            port = Integer.valueOf(portString);
        }
        try {
            // sun httpServer = HTTPServer.create(new InetSocketAddress(port), 0)
            if (StringUtil.isEmpty(scanPackage)) {
                scanPackage = applicationClass.getPackage().getName();
            }
            //扫描所有的类，
            classList = ClassScanner.scanClasses(scanPackage);
            if (includeClass != null && includeClass.length != 0) {
                for (Class aClass : includeClass) {
                    classList.add(aClass);
                }
            }
            if (excludeClass != null && excludeClass.length != 0) {
                for (Class aClass : excludeClass) {
                    classList.remove(aClass);
                }
            }
            // 容器插件
            SERVER_PLUGS_MANAGER.start(applicationClass, args);
            BEAN_PLUGS_MANAGER.start();

            BEAN_PLUGS_MANAGER.init();
            //创建Bean工厂,扫描Class，创建被注解标注的类
            BeanFactory.initBean(classList);
            //找到所有Controller，建立Controller中每个方法和Url的映射关系
            HandlerManager.resolveMappingHandler(classList);
            BEAN_PLUGS_MANAGER.end();
        } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        httpServer = new JdkHTTPServer();
        JdkHTTPServer.VirtualHost host = JdkHTTPServer.getVirtualHost(null);
        httpServer.setPort(port);
//        host.setDirectoryIndex(null);
        JdkHttpContextHandler jdkHttpContextHandler = new JdkHttpContextHandler();
        host.addContext("/", jdkHttpContextHandler,
                MethodType.HEAD.name,
                MethodType.GET.name,
                MethodType.POST.name,
                MethodType.PUT.name,
                MethodType.DELETE.name,
                MethodType.PATCH.name,
                MethodType.OPTIONS.name);
//         HttpContext httpContext = httpServer.createContext("/", new MyHttpHandler())
//         httpContext.getFilters().add(new MyFilter())
        httpServer.setExecutor(executor);
//        JlHttpServerStart.start(port,jdkHttpContextHandler);

        try {
            httpServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("start server  port : " + port + ",started in : " + (endTime - startTime) + "ms");
    }

    public static void addClass(Class clz) {
        classList.add(clz);
    }
}
