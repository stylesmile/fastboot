package io.github.stylesmile.eureka.server;

import io.github.stylesmile.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Eureka Server 插件
 * 用于 FastBoot 框架启动 Eureka 服务注册中心
 * 
 * @author Stylesmile
 */
public class EurekaServerPlugin implements Plugin {
    
    private static final Logger logger = LoggerFactory.getLogger(EurekaServerPlugin.class);

    @Override
    public void start() {
        logger.info("Eureka Server plugin started");
        // Eureka Server 通过 Spring Cloud 自动启动，无需额外操作
    }

    @Override
    public void init() {
        // 初始化逻辑（如果需要）
    }

    @Override
    public void end() {
        logger.info("Eureka Server plugin shutdown");
    }
}
