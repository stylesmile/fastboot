package io.github.stylesmile.nacos;


import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import io.github.stylesmile.ioc.BeanContainer;
import io.github.stylesmile.plugin.Plugin;
import io.github.stylesmile.tool.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Nacos 配置管理插件
 * 参考文献 https://nacos.io/en-us/docs/quick-start.html
 * https://help.aliyun.com/document_detail/94562.html
 *
 * @author Stylesmile
 */
public class NacosPlugin implements Plugin {
    
    private static final Logger logger = LoggerFactory.getLogger(NacosPlugin.class);


    @Override
    public void start() {
        String serverAddr = PropertyUtil.getProperty("nacos.serverAddr");
        
        // 验证必需的配置
        if (serverAddr == null || serverAddr.isEmpty()) {
            logger.warn("Nacos server address not configured, skipping Nacos initialization");
            return;
        }
        
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
        
        String username = PropertyUtil.getProperty("nacos.username");
        String password = PropertyUtil.getProperty("nacos.password");
        
        if (username != null && !username.isEmpty()) {
            properties.put(PropertyKeyConst.USERNAME, username);
        }
        if (password != null && !password.isEmpty()) {
            properties.put(PropertyKeyConst.PASSWORD, password);
        }
        
        try {
            logger.info("Connecting to Nacos server: {}", serverAddr);
            ConfigService configService = NacosFactory.createConfigService(properties);
            BeanContainer.setInstance(ConfigService.class, configService);
            logger.info("Nacos client initialized successfully");
        } catch (NacosException e) {
            logger.error("Failed to initialize Nacos client", e);
            throw new RuntimeException("Failed to initialize Nacos client: " + e.getMessage(), e);
        }
    }

    @Override
    public void init() {

    }

    @Override
    public void end() {

    }

}
