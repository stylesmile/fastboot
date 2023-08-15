package io.github.stylesmile.jdkhttpserver;

import io.github.stylesmile.jlhttpserver.JdkHttpContextHandler;
import io.github.stylesmile.server.MethodType;
import io.github.stylesmile.server.ServerPlugin;
import io.github.stylesmile.tool.PropertyUtil;
import io.github.stylesmile.tool.StringUtil;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 参考文献 https://blog.csdn.net/qq_42413011/article/details/118640420
 *
 * @author Stylesmile
 */
public class JdkHttpServerPlugin implements ServerPlugin {
    private static JdkHTTPServer httpServer = null;

    /**
     * cpu count
     */
    private static int corePoolSize = Runtime.getRuntime().availableProcessors();
    /**
     * 创建线程池  调整队列数 拒绝服务
     */
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, corePoolSize * 2, 50L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(2000));

    public void start(Class applicationClass, String[] args) {
        long startTime = System.currentTimeMillis();
        Integer port = 8080;
        httpServer = new JdkHTTPServer();;
        PropertyUtil.loadProps(applicationClass, "application.properties");
        String portString = PropertyUtil.getProperty("server.port");
        if (StringUtil.isNotEmpty(portString)) {
            port = Integer.valueOf(portString);
        }
        System.out.println("start server  port : " + port);
        JdkHTTPServer.VirtualHost host = JdkHTTPServer.getVirtualHost(null);
        httpServer.setPort(port);
//        HTTPServer.VirtualHost host = null;
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
        System.out.println("started in : " + (endTime - startTime) + "ms");
    }
}
