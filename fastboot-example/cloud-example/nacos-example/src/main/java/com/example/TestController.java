package com.example;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.client.config.listener.impl.PropertiesListener;
import io.github.stylesmile.annotation.AutoWired;
import io.github.stylesmile.annotation.Controller;
import io.github.stylesmile.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

@Controller
public class TestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @AutoWired
    ConfigService configService;

    @RequestMapping("/")
    public int hello2() throws NacosException {
        // 指定配置的 DataID 和 Group 只能用spring类型 有$会报错
//        String dataId = "${dataId}";
        String dataId = "dataId";
//        String group = "${group}";
        String group = "group";
        String content = "connectTimeoutInMills=5000";
        // 发布配置
        boolean publishConfig = configService.publishConfig(dataId, group, content);
        LOGGER.info("publishConfig: {}", publishConfig);
        // 查询配置
        String config = configService.getConfig(dataId, group, 5000);
        LOGGER.info("getConfig: {}", config);
        // 监听配置
        configService.addListener(dataId, group, new PropertiesListener() {
            @Override
            public void innerReceive(Properties properties) {
                LOGGER.info("innerReceive: {}", properties);
            }
        });
        return 1;
    }


}
