//package io.github.stylesmile.zookeeper;
//
//import io.github.stylesmile.plugin.Plugin;
//import io.github.stylesmile.tool.PropertyUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.Properties;
//
///**
// * 参考文献 https://www.jianshu.com/p/2cd487f9560a
// *
// * @author Stylesmile
// */
//public class NacosPlugin implements Plugin {
//
//    private static final Logger log = LoggerFactory.getLogger(NacosPlugin.class);
//
//    @Override
//    public void start() {
//        String serverAddr = PropertyUtil.getProperty("nacos.serverAddr");
//        String namespace = PropertyUtil.getProperty("nacos.namespace");
//        String port = PropertyUtil.getProperty("nacos.port");
//        String name = PropertyUtil.getProperty("nacos.name");
//
//        Properties properties = new Properties();
//        properties.setProperty("serverAddr", "127.0.0.1");
//        properties.setProperty("namespace", "public");
//
//        try {
//            //创建名字服务
//            NamingService namingService = NamingFactory.createNamingService(properties);
//            //注册实例
//            namingService.registerInstance("nacos.test.3", "11.11.11.11", 8888, "TEST1");
//            namingService.registerInstance("nacos.test.3", "2.2.2.2", 9999, "DEFAULT");
//
//            System.out.println(namingService.getAllInstances("nacos.test.3"));
//            //注销实例
//            namingService.deregisterInstance("nacos.test.3", "2.2.2.2", 9999, "DEFAULT");
//
//            System.out.println(namingService.getAllInstances("nacos.test.3"));
//            //订阅服务事件
//            namingService.subscribe("nacos.test.3", new EventListener() {
//                @Override
//                public void onEvent(Event event) {
//                    System.out.println(((NamingEvent)event).getServiceName());
//                    System.out.println(((NamingEvent)event).getInstances());
//                }
//            });
//        } catch (NacosException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//
//    @Override
//    public void init() {
//
//    }
//
//    @Override
//    public void end() {
//
//    }
//
//}
