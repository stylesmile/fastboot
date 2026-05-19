package io.github.stylesmile.eureka.client;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.EurekaClientConfig;
import com.netflix.discovery.DefaultEurekaClientConfig;
import io.github.stylesmile.ioc.BeanContainer;
import io.github.stylesmile.plugin.Plugin;
import io.github.stylesmile.tool.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Eureka Client 插件
 * 用于 FastBoot 框架集成 Eureka 服务注册与发现
 * 
 * @author Stylesmile
 */
public class EurekaClientPlugin implements Plugin {
    
    private static final Logger logger = LoggerFactory.getLogger(EurekaClientPlugin.class);
    
    private DiscoveryClient discoveryClient;

    @Override
    public void start() {
        try {
            // 从配置文件读取 Eureka 配置
            String serviceUrl = PropertyUtil.getProperty("eureka.client.service-url.default-zone");
            String appName = PropertyUtil.getProperty("eureka.instance.appname", "fastboot-application");
            String instanceId = PropertyUtil.getProperty("eureka.instance.instance-id", appName);
            
            if (serviceUrl == null || serviceUrl.isEmpty()) {
                logger.warn("Eureka service URL not configured, skipping Eureka registration");
                return;
            }
            
            logger.info("Starting Eureka client with service URL: {}", serviceUrl);
            
            // 创建 Eureka Client 配置
            EurekaClientConfig clientConfig = new DefaultEurekaClientConfig();
            
            // 创建应用信息
            InstanceInfo instanceInfo = createInstanceInfo(appName, instanceId);
            
            // 创建 ApplicationInfoManager
            ApplicationInfoManager applicationInfoManager = createApplicationInfoManager(appName, instanceId, instanceInfo);
            
            // 创建 DiscoveryClient
            discoveryClient = new DiscoveryClient(applicationInfoManager, clientConfig);
            
            // 注册到 IOC 容器
            BeanContainer.setInstance(DiscoveryClient.class, discoveryClient);
            BeanContainer.setInstance(EurekaClient.class, discoveryClient);
            
            logger.info("Eureka client started successfully, registered as: {}", appName);
            
        } catch (Exception e) {
            logger.error("Failed to start Eureka client", e);
            throw new RuntimeException("Failed to start Eureka client", e);
        }
    }

    /**
     * 创建实例信息
     */
    private InstanceInfo createInstanceInfo(String appName, String instanceId) {
        String hostname = PropertyUtil.getProperty("eureka.instance.hostname", "localhost");
        int port = Integer.parseInt(PropertyUtil.getProperty("server.port", "8080"));
        
        return InstanceInfo.Builder.newBuilder()
                .setAppName(appName)
                .setInstanceId(instanceId)
                .setHostName(hostname)
                .setPort(port)
                .setStatus(InstanceInfo.InstanceStatus.UP)
                .build();
    }

    /**
     * 创建应用信息管理器
     */
    private ApplicationInfoManager createApplicationInfoManager(String appName, String instanceId, InstanceInfo instanceInfo) {
        return new ApplicationInfoManager(
                new com.netflix.appinfo.MyDataCenterInstanceConfig() {
                    @Override
                    public String getAppname() {
                        return appName;
                    }
                    
                    @Override
                    public String getInstanceId() {
                        return instanceId;
                    }
                },
                instanceInfo
        );
    }

    @Override
    public void init() {
        // 初始化逻辑（如果需要）
    }

    @Override
    public void end() {
        // 关闭时注销服务
        if (discoveryClient != null) {
            try {
                discoveryClient.shutdown();
                logger.info("Eureka client shutdown successfully");
            } catch (Exception e) {
                logger.error("Failed to shutdown Eureka client", e);
            }
        }
    }
}
