package com.example.demo;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

public class TomcatServer {
    /**
     * Tomcat实例
     */
    private Tomcat tomcat;
    /**
     * 启动参数，后续可以获取启动参数来进行配置
     */
    private String[] args;

    public TomcatServer(String[] args) {
        this.args = args;
    }

    /**
     * 开启Tomcat服务
     */
    public void startServer() throws LifecycleException {
        tomcat = new Tomcat();
        int port = 8080;
        tomcat.setPort(port);
        System.out.println("start port : " + port);
        tomcat.start();
        //初始化容器
        Context context = new StandardContext();
        context.setPath("");
        context.addLifecycleListener(new Tomcat.FixContextListener());
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        //注册DispatcherServlet
        Tomcat.addServlet(context, "dispatcherServlet", dispatcherServlet)
                //设置支持异步
                .setAsyncSupported(true);
        //设置Servlet和URI的映射
        context.addServletMappingDecoded("/", "dispatcherServlet");
        //注册默认Host容器
        tomcat.getHost().addChild(context);

        //声明等待线程
        Thread awaitThread = new Thread(new Runnable() {
            @Override
            public void run() {
                //让Tomcat一直在等待
                tomcat.getServer().await();
            }
        }, "tomcat_await_thread");
        //设置为非守护进程
        awaitThread.setDaemon(false);
        awaitThread.start();
    }
}