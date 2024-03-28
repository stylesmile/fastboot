package io.github.stylesmile.reactor.server;

import io.github.stylesmile.handle.HandlerManager;
import io.github.stylesmile.handle.MappingHandler;
import io.github.stylesmile.plugin.ServerPlugin;
import io.github.stylesmile.tool.PropertyUtil;
import io.github.stylesmile.tool.StringUtil;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;
import reactor.netty.http.server.HttpServerRoutes;

import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 参考文献 https://blog.csdn.net/qq_42413011/article/details/118640420
 *
 * @author Stylesmile
 */
public class ReactorNettyServerPlugin implements ServerPlugin {
    DisposableServer server = null;
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
        PropertyUtil.loadProps(applicationClass, "application.properties", args);
        String portString = PropertyUtil.getProperty("server.port");
        if (StringUtil.isNotEmpty(portString)) {
            port = Integer.valueOf(portString);
        }
        DisposableServer server = HttpServer.create()
                .route(ReactorNettyServerPlugin::configureRoutes)
                .port(port)
                .bindNow();
        System.out.println("start server port :" + port);
        server.onDispose().block();

        long endTime = System.currentTimeMillis();

        System.out.println("started in : " + (endTime - startTime) + "ms");
    }

    private static void configureRoutes(HttpServerRoutes routes) {
        Map<String, MappingHandler> handlerMap = HandlerManager.getAllMappingHandler();
        for (String key : handlerMap.keySet()) {
//            routes.get("/", handlerMap.get(key).getMethod().invoke());
        }
    }
}
