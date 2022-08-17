package io.github.stylesmile.app;

import io.github.stylesmile.handle.HandlerManager;
import io.github.stylesmile.ioc.BeanFactory;
import io.github.stylesmile.plugin.PlugsManager;
import io.github.stylesmile.server.HTTPServer;
import io.github.stylesmile.server.JlHttpContextHandler;
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
    private static HTTPServer httpServer = null;
    private static final PlugsManager PLUGS_MANAGER = new PlugsManager();
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
        Integer port = 8080;
        httpServer = new HTTPServer();
        PropertyUtil.loadProps(applicationClass, "application.properties");
        String portString = PropertyUtil.getProperty("server.port");
        if (StringUtil.isNotEmpty(portString)) {
            port = Integer.valueOf(portString);
        }
        System.out.println("start server  port : " + port);
        HTTPServer.VirtualHost host = httpServer.getVirtualHost(null);
        httpServer.setPort(port);
        try {
            // sun httpServer = HTTPServer.create(new InetSocketAddress(port), 0)
            String package1 = applicationClass.getPackage().getName();
            //扫描所有的类，
            classList = ClassScanner.scanClasses(package1);
            PLUGS_MANAGER.start();
            PLUGS_MANAGER.init();
            PLUGS_MANAGER.end();
            //创建Bean工厂,扫描Class，创建被注解标注的类
            BeanFactory.initBean(classList);
            //找到所有Controller，建立Controller中每个方法和Url的映射关系
            HandlerManager.resolveMappingHandler(classList);
        } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        host.setDirectoryIndex(null);
        JlHttpContextHandler jlHttpContextHandler = new JlHttpContextHandler();
        host.addContext("/", jlHttpContextHandler,
                MethodType.HEAD.name,
                MethodType.GET.name,
                MethodType.POST.name,
                MethodType.PUT.name,
                MethodType.DELETE.name,
                MethodType.PATCH.name,
                MethodType.OPTIONS.name);
        // HttpContext httpContext = httpServer.createContext("/", new MyHttpHandler())
        // httpContext.getFilters().add(new MyFilter())
        httpServer.setExecutor(executor);
        try {
            httpServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("started in : " + (endTime - startTime) + "ms");
    }
}
